package com.jinglun.guard.dataservice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageResult implements Serializable{
  /**状态*/
  private int code=0;
  /**状态信息*/
  private String msg="";
  /**数据总数*/
  private long count;

  private List<?> data;

  private Map<String, Integer> map;
  
  public PageResult() {
  }

  public PageResult(long count, List<?> data, Map<String, Integer> map) {
	    this.count = count;
	    this.data = data;
	    this.map = map;
  }
  
  public PageResult(long count, List<?> data) {
    this.count = count;
    this.data = data;
  }

  public PageResult(List<?> data) {
    this.data = data;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public List<?> getData() {
    return data;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

public Map<String, Integer> getMap() {
	return map;
}

public void setMap(Map<String, Integer> map) {
	this.map = map;
}
}
