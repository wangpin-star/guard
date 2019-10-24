package com.jinglun.guard.systemManage.domain;

public class BusinessParam {
    private String paramKey;
    private String paramValue;
    private String paramName;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public BusinessParam(String paramKey, String paramValue, String paramName) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.paramName = paramName;
    }

    public BusinessParam() {
    }
}
