package com.jinglun.guard.employeeManage.domain;

public class ExcelResult {
    private String result;
    private Integer totalCount;
    private Integer emptyCount;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getEmptyCount() {
        return emptyCount;
    }

    public void setEmptyCount(Integer emptyCount) {
        this.emptyCount = emptyCount;
    }
}
