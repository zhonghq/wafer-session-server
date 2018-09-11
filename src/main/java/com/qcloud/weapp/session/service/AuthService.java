package com.qcloud.weapp.session.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.weapp.session.crypto.WXBizDataCrypt;
import com.qcloud.weapp.session.entity.Result;
import com.qcloud.weapp.session.mapper.CAppInfoMapper;
import com.qcloud.weapp.session.mapper.CSessionInfoMapper;
import com.qcloud.weapp.session.model.CAppInfo;
import com.qcloud.weapp.session.model.CSessionInfo;
import com.qcloud.weapp.session.utils.HttpUtil;
import com.qcloud.weapp.session.utils.ReturnCode;
import com.qcloud.weapp.session.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhong on 2018/5/3.
 */
@Service
public class AuthService {

    @Autowired
    CAppInfoMapper cAppInfoMapper;
    @Autowired
    CSessionInfoMapper cSessionInfoMapper;

    @Value("${qcloud.appid}")
    private String appId;

    /**
     * 废除getUserInfo直接调用后的
     * @param code
     * @return
     */
    public Result getIdSkey(String code) {
        try {
            CAppInfo cAppInfo = cAppInfoMapper.selectByPrimaryKey(appId);
            if (null != cAppInfo){
                String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" +cAppInfo.getAppid()+ "&secret=" +cAppInfo.getSecret()+ "&js_code=" +code+ "&grant_type=authorization_code";
                JSONObject jsonObject = JSONObject.parseObject(HttpUtil.request(url,"POST",null));
                if (jsonObject.containsKey("openid") && jsonObject.containsKey("session_key") && jsonObject.containsKey("expires_in")){
                    CSessionInfo sessionInfo = new CSessionInfo();
                    sessionInfo.setUuid(UUIDUtils.getUUID());
                    sessionInfo.setSkey(UUIDUtils.getUUID());
                    sessionInfo.setCreateTime(new Date());
                    sessionInfo.setLastVisitTime(new Date());
                    sessionInfo.setOpenId(jsonObject.getString("openid"));
                    sessionInfo.setSessionKey(jsonObject.getString("session_key"));
                    try {
                        CSessionInfo cSessionInfo = cSessionInfoMapper.selectByOpenId(sessionInfo.getOpenId());
                        BASE64Decoder base64Decoder = new BASE64Decoder();
                        JSONObject userInfo = JSON.parseObject(new String(base64Decoder.decodeBuffer(cSessionInfo.getUserInfo()),"utf-8"));
                        if (null == userInfo){
                            userInfo = new JSONObject();
                        }
                        userInfo.put("openId",jsonObject.get("openid"));
                        JSONObject waterMark = new JSONObject();
                        waterMark.put("timestamp",(new Date()).getTime()/1000);
                        waterMark.put("appid",cAppInfo.getAppid());
                        userInfo.put("watermark",waterMark);
                        BASE64Encoder base64Encoder = new BASE64Encoder();
                        String userInfoBase64 = base64Encoder.encode(userInfo.toJSONString().getBytes("utf-8"));
                        sessionInfo.setUserInfo(userInfoBase64);
                        if (null == cSessionInfo.getUuid()){
                            cSessionInfoMapper.insertSelective(sessionInfo);
                            Map<String,Object> returnMap = new HashMap<String, Object>();
                            returnMap.put("id",sessionInfo.getUuid());
                            returnMap.put("skey",sessionInfo.getSkey());
                            returnMap.put("user_info",userInfo);
                            returnMap.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"NEW_SESSION_SUCCESS",returnMap);
                        }else {
                            sessionInfo.setUuid(cSessionInfo.getUuid());
                            cSessionInfoMapper.updateByPrimaryKeySelective(sessionInfo);
                            JSONObject dataJsonObject = new JSONObject();
                            dataJsonObject.put("id",sessionInfo.getUuid());
                            dataJsonObject.put("skey",sessionInfo.getSkey());
                            dataJsonObject.put("user_info", userInfo);
                            dataJsonObject.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"UPDATE_SESSION_SUCCESS",dataJsonObject);
                        }

                    } catch (Exception e) {
                        return new Result(ReturnCode.MA_DECRYPT_ERR,"DECRYPT_FAIL");
                    }

                }else if (jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
                    return new Result(ReturnCode.MA_WEIXIN_CODE_ERR,"WEIXIN_CODE_ERR");
                }else {
                    return new Result(ReturnCode.MA_WEIXIN_RETURN_ERR,"WEIXIN_RETURN_ERR");
                }
            }else {
                return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
            }
        }catch (Exception ex){
            return new Result(ReturnCode.MA_NO_APPID,ex.getMessage());
        }
    }

    /**
     * 处理用户登录请求
     * @param code
     * @param encryptData
     * @param iv
     * @return
     */
    public Result getIdSkey(String code,String encryptData,String iv){
        try {
            CAppInfo cAppInfo = cAppInfoMapper.selectByPrimaryKey(appId);
            if (null != cAppInfo){
                String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" +cAppInfo.getAppid()+ "&secret=" +cAppInfo.getSecret()+ "&js_code=" +code+ "&grant_type=authorization_code";
                JSONObject jsonObject = JSONObject.parseObject(HttpUtil.request(url,"POST",null));
                if (jsonObject.containsKey("openid") && jsonObject.containsKey("session_key") && jsonObject.containsKey("expires_in")){
                    CSessionInfo sessionInfo = new CSessionInfo();
                    sessionInfo.setUuid(UUIDUtils.getUUID());
                    sessionInfo.setSkey(UUIDUtils.getUUID());
                    sessionInfo.setCreateTime(new Date());
                    sessionInfo.setLastVisitTime(new Date());
                    sessionInfo.setOpenId(jsonObject.getString("openid"));
                    sessionInfo.setSessionKey(jsonObject.getString("session_key"));
                    WXBizDataCrypt bizDataCrypt = new WXBizDataCrypt(cAppInfo.getAppid(),sessionInfo.getSessionKey());
                    try {
                        JSONObject userInfo = bizDataCrypt.decrypt(encryptData,iv);
                        BASE64Encoder base64Encoder = new BASE64Encoder();
                        String userInfoBase64 = base64Encoder.encode(userInfo.toJSONString().getBytes("utf-8"));
                        sessionInfo.setUserInfo(userInfoBase64);
                        CSessionInfo cSessionInfo = cSessionInfoMapper.selectByOpenId(sessionInfo.getOpenId());
                        if (null == cSessionInfo.getUuid()){
                            cSessionInfoMapper.insertSelective(sessionInfo);
                            Map<String,Object> returnMap = new HashMap<String, Object>();
                            returnMap.put("id",sessionInfo.getUuid());
                            returnMap.put("skey",sessionInfo.getSkey());
                            returnMap.put("user_info",userInfo);
                            returnMap.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"NEW_SESSION_SUCCESS",returnMap);
                        }else {
                            sessionInfo.setUuid(cSessionInfo.getUuid());
                            cSessionInfoMapper.updateByPrimaryKeySelective(sessionInfo);
                            JSONObject dataJsonObject = new JSONObject();
                            dataJsonObject.put("id",sessionInfo.getUuid());
                            dataJsonObject.put("skey",sessionInfo.getSkey());
                            dataJsonObject.put("user_info", userInfo);
                            dataJsonObject.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"UPDATE_SESSION_SUCCESS",dataJsonObject);
                        }

                    } catch (Exception e) {
                        return new Result(ReturnCode.MA_DECRYPT_ERR,"DECRYPT_FAIL");
                    }

                }else if (jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
                    return new Result(ReturnCode.MA_WEIXIN_CODE_ERR,"WEIXIN_CODE_ERR");
                }else {
                    return new Result(ReturnCode.MA_WEIXIN_RETURN_ERR,"WEIXIN_RETURN_ERR");
                }
            }else {
                return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
            }
        }catch (Exception ex){
            return new Result(ReturnCode.MA_NO_APPID,ex.getMessage());
        }
    }



    /**
     * 检查用户登陆状态
     * @param id
     * @param skey
     * @return
     */
    public Result auth(String id, String skey) throws IOException {
        try {
            CAppInfo cAppInfo = cAppInfoMapper.selectByPrimaryKey(appId);
            if (null != cAppInfo){
                CSessionInfo cSessionInfo = cSessionInfoMapper.selectByPrimaryKey(id);
                if (null == cSessionInfo || !skey.equals(cSessionInfo.getSkey())){
                    //鉴权失败
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }
                Date now = new Date();
                if (((now.getTime() - cSessionInfo.getCreateTime().getTime())/86400000) > cAppInfo.getLoginDuration()){
                    //超时
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }else if ((now.getTime() - cSessionInfo.getLastVisitTime().getTime()) > cAppInfo.getSessionDuration()){
                    //超时
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }else {
                    cSessionInfo.setLastVisitTime(new Date());
                    cSessionInfoMapper.updateLastVisitTime(cSessionInfo);
                    BASE64Decoder base64Decoder = new BASE64Decoder();
                    String userInfo = new String(base64Decoder.decodeBuffer(cSessionInfo.getUserInfo()),"utf-8");
                    JSONObject dataJsonObject = new JSONObject();
                    dataJsonObject.put("user_info", JSONObject.parseObject(userInfo));
                    return new Result(ReturnCode.MA_OK,"AUTH_SUCCESS",dataJsonObject);
                }
            }else {
                return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
            }
        }catch (Exception ex){
            return new Result(ReturnCode.MA_NO_APPID,ex.getMessage());
        }
    }

    /**
     * 新的解密方法
     * @param id
     * @param skey
     * @param iv
     * @param encryptData
     * @return
     */
    public Result decrypt(String id, String skey, String iv, String encryptData) {
        try {
            CAppInfo cAppInfo = cAppInfoMapper.selectByPrimaryKey(appId);
            if (null != cAppInfo){
                CSessionInfo cSessionInfo = cSessionInfoMapper.selectByPrimaryKey(id);
                if (null == cSessionInfo || !skey.equals(cSessionInfo.getSkey())){
                    //鉴权失败
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }
                Date now = new Date();
                if (((now.getTime() - cSessionInfo.getCreateTime().getTime())/86400000) > cAppInfo.getLoginDuration()){
                    //超时
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }else if ((now.getTime() - cSessionInfo.getLastVisitTime().getTime()) > cAppInfo.getSessionDuration()){
                    //超时
                    return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
                }else {
                    WXBizDataCrypt bizDataCrypt = new WXBizDataCrypt(cAppInfo.getAppid(),cSessionInfo.getSessionKey());
                    JSONObject userInfo = bizDataCrypt.decrypt(encryptData,iv);
                    BASE64Encoder base64Encoder = new BASE64Encoder();
                    String userInfoBase64 = base64Encoder.encode(userInfo.toJSONString().getBytes("utf-8"));
                    cSessionInfo.setUserInfo(userInfoBase64);
                    cSessionInfoMapper.updateByPrimaryKeySelective(cSessionInfo);
                    JSONObject dataJsonObject = new JSONObject();
                    dataJsonObject.put("user_info",userInfo);
                    return new Result(ReturnCode.MA_OK,"DECRYPT_SUCCESS",dataJsonObject);
                }
            }else {
                return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
            }
        }catch (Exception ex){
            return new Result(ReturnCode.MA_NO_APPID,ex.getMessage());
        }
    }
}
