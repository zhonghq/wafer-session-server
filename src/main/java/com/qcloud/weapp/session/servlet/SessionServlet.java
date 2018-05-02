package com.qcloud.weapp.session.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.weapp.session.entity.Para;
import com.qcloud.weapp.session.entity.Result;
import com.qcloud.weapp.session.factory.GetSqlSession;
import com.qcloud.weapp.session.utils.Consants;
import com.qcloud.weapp.session.utils.MethodKey;
import com.qcloud.weapp.session.utils.RequestUtil;
import com.qcloud.weapp.session.utils.ReturnCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import javax.naming.AuthenticationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 回话管理器管理服务器入口方法
 * @author zhonghongqiang
 *         Created on 2018-04-28.
 */
public class SessionServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try {
            JSONObject jsonObject = JSON.parseObject(RequestUtil.getInputStreamStr(req));
            Result result = new Result();
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
                    if (StringUtils.isNotBlank(para.getCode()) && StringUtils.isNotBlank(para.getEncryptData())){
                        Auth auth = new Auth();
                        if (StringUtils.isBlank(para.getIv())) {
                            result =  auth.getIdSkey(para.getCode(),para.getEncryptData(),"old");
                        }else {
                            result = auth.getIdSkey(para.getCode(),para.getEncryptData(),para.getIv());
                        }
                    }else {
                        result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                    }
                }else if (method.equals(MethodKey.AUTH)){
                    /**
                     * 检查用户登陆状态
                     */
                    if (StringUtils.isNotBlank(para.getId()) && StringUtils.isNotBlank(para.getSkey())) {
                        Auth auth = new Auth();
                        result =  auth.auth(para.getId(),para.getSkey());
                    }else {
                        result = new Result(ReturnCode.MA_PARA_ERR,"PARA_ERR");
                    }
                }else if (method.equals(MethodKey.DECRYPT)){
                    /**
                     * 小程序敏感数据解密
                     */
                    if (StringUtils.isNotBlank(para.getId()) && StringUtils.isNotBlank(para.getSkey()) && StringUtils.isNotBlank(para.getEncryptData())) {

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
            SqlSession sqlSession = GetSqlSession.getSqlSession();
            sqlSession.commit();
            resp.getWriter().write(JSON.toJSONString(result));
        } catch (Exception ex){
            GetSqlSession.rollback();
        } finally{

        }

    }

}
