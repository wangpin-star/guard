package com.jinglun.guard.employeeManage.domain;

public class TermVO {
    private String termName;
    private String termMode;
    private Integer termLength;

    public Integer getTermLength() {
        return termLength;
    }

    public void setTermLength(Integer termLength) {
        this.termLength = termLength;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermMode() {
        return termMode;
    }

    public void setTermMode(String termMode) {
        this.termMode = termMode;
    }
}
