package com.qcloud.weapp.session.servlet;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.weapp.session.crypto.WXBizDataCrypt;
import com.qcloud.weapp.session.entity.Result;
import com.qcloud.weapp.session.factory.GetSqlSession;
import com.qcloud.weapp.session.model.CAppInfo;
import com.qcloud.weapp.session.model.CSessionInfo;
import com.qcloud.weapp.session.utils.Consants;
import com.qcloud.weapp.session.utils.ReturnCode;
import com.qcloud.weapp.session.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zhong
 * @date 2018/4/28
 */
public class Auth {

    /**
     * 处理用户登录请求
     * @param code
     * @param encryptData
     * @param iv
     * @return
     */
    public Result getIdSkey(String code,String encryptData,String iv){
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        List<CAppInfo> appInfoList = sqlSession.selectList("CAppInfoMapper.selectAll");
        if (null != appInfoList && appInfoList.size() == 1){
            CAppInfo appInfo = appInfoList.get(0);
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" +appInfo.getAppid()+ "&secret=" +appInfo.getSecret()+ "&js_code=" +code+ "&grant_type=authorization_code";
            JSONObject jsonObject = JSONObject.parseObject(HttpUtil.request(url,"POST",null));
            if (jsonObject.containsKey("openid") && jsonObject.containsKey("session_key") && jsonObject.containsKey("expires_in")){
                CSessionInfo sessionInfo = new CSessionInfo();
                sessionInfo.setUuid(UUIDUtils.getUUID());
                sessionInfo.setSkey(UUIDUtils.getUUID());
                sessionInfo.setCreateTime(new Date());
                sessionInfo.setLastVisitTime(new Date());
                sessionInfo.setOpenId(jsonObject.getString("openid"));
                sessionInfo.setSessionKey(jsonObject.getString("session_key"));
                WXBizDataCrypt bizDataCrypt = new WXBizDataCrypt(appInfo.getAppid(),sessionInfo.getSessionKey());
                try {
                    String userInfo = bizDataCrypt.decrypt(encryptData,iv);
                    BASE64Encoder base64Encoder = new BASE64Encoder();
                    String userInfoBase64 = base64Encoder.encode(userInfo.getBytes());
                    sessionInfo.setUserInfo(userInfoBase64);
                    CSessionInfo cSessionInfo = sqlSession.selectOne("CSessionInfoMapper.selectByOpenId",sessionInfo.getOpenId());
                    int cnt = 0;
                    if (null != cSessionInfo){
                        sessionInfo.setUuid(cSessionInfo.getUuid());
                        cnt = sqlSession.update("CSessionInfoMapper.updateByPrimaryKeySelective",sessionInfo);
                        if (cnt == 1){
                            Map<String,Object> returnMap = new HashMap<>();
                            returnMap.put("id",sessionInfo.getUuid());
                            returnMap.put("skey",sessionInfo.getSkey());
                            returnMap.put("user_info",userInfo);
                            returnMap.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"UPDATE_SESSION_SUCCESS",returnMap);
                        }
                    }else {
                        cnt = sqlSession.insert("CSessionInfoMapper.insertSelective",sessionInfo);
                        if (cnt == 1){
                            Map<String,Object> returnMap = new HashMap<>();
                            returnMap.put("id",sessionInfo.getUuid());
                            returnMap.put("skey",sessionInfo.getSkey());
                            returnMap.put("user_info",userInfo);
                            returnMap.put("duration",jsonObject.get("expires_in"));
                            return new Result(ReturnCode.MA_OK,"NEW_SESSION_SUCCESS",returnMap);
                        }
                    }
                    if (cnt == 0){
                        return new Result(ReturnCode.MA_CHANGE_SESSION_ERR,"CHANGE_SESSION_ERR");
                    }
                    
                } catch (Exception e) {
                    return new Result(ReturnCode.MA_DECRYPT_ERR,"DECRYPT_FAIL");
                }

            }else if (jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
                return new Result(ReturnCode.MA_WEIXIN_CODE_ERR,"WEIXIN_CODE_ERR");
            }else {
                return new Result(ReturnCode.MA_WEIXIN_RETURN_ERR,"WEIXIN_RETURN_ERR");
            }
            return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
        }else {
            return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
        }
    }

    /**
     * 检查用户登陆状态
     * @param id
     * @param skey
     * @return
     */
    public Result auth(String id, String skey) throws IOException {
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        List<CAppInfo> appInfoList = sqlSession.selectList("CAppInfoMapper.selectAll");
        if (null != appInfoList && appInfoList.size() == 1){
            CAppInfo cAppInfo = appInfoList.get(0);
            CSessionInfo param = new CSessionInfo();
            param.setUuid(id);
            param.setSkey(skey);
            CSessionInfo cSessionInfo = sqlSession.selectOne("CSessionInfoMapper.selectByAuth",param);
            Date now = new Date();
            if (((now.getTime() - cSessionInfo.getCreateTime().getTime())/86400) > cAppInfo.getLoginDuration()){
                //超时
                return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
            }else if ((now.getTime() - cSessionInfo.getLastVisitTime().getTime()) > cAppInfo.getSessionDuration()){
                //超时
                return new Result(ReturnCode.MA_AUTH_ERR,"AUTH_FAIL");
            }else {
                BASE64Decoder base64Decoder = new BASE64Decoder();
                Map<String,Object> returnMap = new HashMap<>();
                returnMap.put("user_info",base64Decoder.decodeBuffer(cSessionInfo.getUserInfo()).toString());
                return new Result(ReturnCode.MA_OK,"AUTH_SUCCESS",returnMap);
            }
        }else {
            return new Result(ReturnCode.MA_NO_APPID,"NO_APPID");
        }
    }
}
