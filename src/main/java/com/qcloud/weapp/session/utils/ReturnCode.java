package com.qcloud.weapp.session.utils;

/**
 * @author zhonghongqiang
 *         Created on 2018-04-28.
 */
public interface ReturnCode {
    Integer MA_OK = 0;       //成功返回码
    Integer MA_MYSQL_ERR = 1001;    // Mysql错误等
    Integer MA_NO_INTERFACE = 1002;    // 接口参数不存在
    Integer MA_PARA_ERR = 1003;    //参数错误
    Integer MA_DECRYPT_ERR = 60021;   //解密失败
    Integer MA_WEIXIN_NET_ERR = 1005;    //连接微信服务器失败
    Integer MA_WEIXIN_CODE_ERR = 40029;   //CODE无效
    Integer MA_CHANGE_SESSION_ERR = 1006;    //新增修改SESSION失败
    Integer MA_WEIXIN_RETURN_ERR = 1007;    //微信返回值错误
    Integer MA_AUTH_ERR = 60012;   //鉴权失败
    Integer MA_UPDATE_LASTVISITTIME_ERR = 1008;    //更新最近访问时间失败
    Integer MA_REQUEST_ERR = 1009;    //请求包不是json
    Integer MA_INTERFACE_ERR = 1010;    //接口名称错误
    Integer MA_NO_PARA = 1011;    //不存在参数
    Integer MA_NO_APPID = 1012;    //不能获取AppID
    Integer MA_INIT_APPINFO_ERR = 1013;    //初始化AppID失败
}
