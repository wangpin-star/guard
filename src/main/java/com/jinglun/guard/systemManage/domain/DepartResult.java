package com.jinglun.guard.systemManage.domain;

import java.util.List;

public class DepartResult {
    private String msg;
    private int code;
    private List<Depart> data;
    private int count;
    private boolean is;
    private String tip;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Depart> getData() {
        return data;
    }

    public void setData(List<Depart> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
