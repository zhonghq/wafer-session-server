package com.qcloud.weapp.session.model;

public class CAppInfo {
    private String appid;

    private String secret;

    private Integer loginDuration;

    private Integer sessionDuration;

    private String qcloudAppid;

    private String ip;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    public Integer getLoginDuration() {
        return loginDuration;
    }

    public void setLoginDuration(Integer loginDuration) {
        this.loginDuration = loginDuration;
    }

    public Integer getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(Integer sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getQcloudAppid() {
        return qcloudAppid;
    }

    public void setQcloudAppid(String qcloudAppid) {
        this.qcloudAppid = qcloudAppid == null ? null : qcloudAppid.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", appid=").append(appid);
        sb.append(", secret=").append(secret);
        sb.append(", loginDuration=").append(loginDuration);
        sb.append(", sessionDuration=").append(sessionDuration);
        sb.append(", qcloudAppid=").append(qcloudAppid);
        sb.append(", ip=").append(ip);
        sb.append("]");
        return sb.toString();
    }
}