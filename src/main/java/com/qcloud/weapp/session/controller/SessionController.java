package com.qcloud.weapp.session.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.weapp.session.entity.Para;
import com.qcloud.weapp.session.entity.Result;
import com.qcloud.weapp.session.service.AuthService;
import com.qcloud.weapp.session.utils.MethodKey;
import com.qcloud.weapp.session.utils.RequestUtil;
import com.qcloud.weapp.session.utils.ReturnCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author zhong
 * @date 2018/5/3
 */
@RestController
public class SessionController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = "/mina_auth",method = RequestMethod.POST)
    public void session(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result result = new Result();
        try {
            String jsonStr = RequestUtil.getInputStreamStr(request);
            if (StringUtils.isBlank(jsonStr)){
                result = new Result(ReturnCode.MA_REQUEST_ERR,"REQUEST_IS_NOT_JSON");
            }else {
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                if (null == jsonObject.getJSONObject("interface").get("interfaceName")){
                    result = new Result(ReturnCode.MA_NO_INTERFACE,"NO_INTERFACENAME_PARA");
                }else if (null == jsonObject.getJSONObject("interface").get("para")){
                    result = new Result(ReturnCode.MA_NO_PARA,"NO_PARA");
                }else {
                    Para para = JSON.parseObject(jsonObject.getJSONObject("interface").get("para").toString(),Para.class);
                    String method = jsonObject.getJSONObject("interface").get("interfaceName").toString();
                    if (method.equals(MethodKey.IDSKEY)){
                        /**
                         * 处理用户登录请求
                         */
                        if (StringUtils.isNotBlank(para.getCode()) && StringUtils.isNotBlank(para.getEncryptData()) && StringUtils.isNotBlank(para.getIv())){
                            result = authService.getIdSkey(para.getCode(),para.getEncryptData(),para.getIv());
                        }else if (StringUtils.isNotBlank(para.getCode())){
                            result = authService.getIdSkey(para.getCode());
                        }else {
                            result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                        }
                    }else if (method.equals(MethodKey.AUTH)){
                        /**
                         * 检查用户登陆状态
                         */
                        if (StringUtils.isNotBlank(para.getId()) && StringUtils.isNotBlank(para.getSkey())) {
                            result =  authService.auth(para.getId(),para.getSkey());
                        }else {
                            result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                        }
                    }else if (method.equals(MethodKey.DECRYPT)){
                        /**
                         * 小程序敏感数据解密
                         */
                        if (StringUtils.isNotBlank(para.getId()) && StringUtils.isNotBlank(para.getSkey()) && StringUtils.isNotBlank(para.getIv()) && StringUtils.isNotBlank(para.getEncryptData())) {
                            result =  authService.decrypt(para.getId(),para.getSkey(),para.getIv(),para.getEncryptData());
                        }else {
                            result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                        }
                    }else if (method.equals(MethodKey.INITDATA)){
                        /**
                         * 小程序信息初始化
                         */
                        if (StringUtils.isNotBlank(para.getAppid()) && StringUtils.isNotBlank(para.getSecret()) &&
                                StringUtils.isNotBlank(para.getQcloudAppid()) && StringUtils.isNotBlank(para.getId()) &&
                                StringUtils.isNotBlank(para.getCdbIp()) && StringUtils.isNotBlank(para.getCdbPort()) &&
                                StringUtils.isNotBlank(para.getCdbUserName()) && StringUtils.isNotBlank(para.getCdbPassWd())) {

                        }else {
                            result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                        }
                    }else {
                        result = new Result(ReturnCode.MA_INTERFACE_ERR,"MA_INTERFACE_ERR");
                    }
                }
            }
        } catch (Exception e) {
            result = new Result(ReturnCode.MA_INIT_APPINFO_ERR,e.getMessage());
        } finally {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }
}
