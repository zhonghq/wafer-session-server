package com.qcloud.weapp.session.entity;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 接收参数
 * @author zhonghongqiang
 *         Created on 2018-04-28.
 */
public class Para {

    private String code;
    @JSONField(name = "encrypt_data")
    private String encryptData;
    private String iv;
    private String id;
    private String skey;
    private String appid;
    private String secret;
    @JSONField(name = "qcloud_appid")
    private String qcloudAppid;
    private String ip;
    @JSONField(name = "cdb_ip")
    private String cdbIp;
    @JSONField(name = "cdb_port")
    private String cdbPort;
    @JSONField(name = "cdb_user_name")
    private String cdbUserName;
    @JSONField(name = "cdb_pass_wd")
    private String cdbPassWd;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptData() {
        return encryptData;
    }

    public void setEncryptData(String encryptData) {
        this.encryptData = encryptData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getQcloudAppid() {
        return qcloudAppid;
    }

    public void setQcloudAppid(String qcloudAppid) {
        this.qcloudAppid = qcloudAppid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCdbIp() {
        return cdbIp;
    }

    public void setCdbIp(String cdbIp) {
        this.cdbIp = cdbIp;
    }

    public String getCdbPort() {
        return cdbPort;
    }

    public void setCdbPort(String cdbPort) {
        this.cdbPort = cdbPort;
    }

    public String getCdbUserName() {
        return cdbUserName;
    }

    public void setCdbUserName(String cdbUserName) {
        this.cdbUserName = cdbUserName;
    }

    public String getCdbPassWd() {
        return cdbPassWd;
    }

    public void setCdbPassWd(String cdbPassWd) {
        this.cdbPassWd = cdbPassWd;
    }
}
