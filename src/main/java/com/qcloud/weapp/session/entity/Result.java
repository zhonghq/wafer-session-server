package com.qcloud.weapp.session.entity;

/**
 * 验证返回对象
 * @author zhonghongqiang
 *         Created on 2018-04-28.
 */
public class Result {

    private Integer returnCode;

    private String returnMessage;

    private Object returnData;

    private Integer version;

    private String componentName;


    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Result() {
    }

    public Result(Integer returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.returnData = "";
        this.version = 1;
        this.componentName = "MA";
    }

    public Result(Integer returnCode, String returnMessage, Object returnData) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.returnData = returnData;
        this.version = 1;
        this.componentName = "MA";
    }
}
