package com.jinglun.guard.devicemanage.domain;

import java.util.List;

import com.jinglun.guard.dataservice.DataService.HintConfigInfo;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;

public class TermInfo {
	List<ParamConfigInfo> paramInfo;
	List<HintConfigInfo> hintInfo;
	public List<ParamConfigInfo> getParamInfo() {
		return paramInfo;
	}
	public void setParamInfo(List<ParamConfigInfo> paramInfo) {
		this.paramInfo = paramInfo;
	}
	public List<HintConfigInfo> getHintInfo() {
		return hintInfo;
	}
	public void setHintInfo(List<HintConfigInfo> hintInfo) {
		this.hintInfo = hintInfo;
	}
}
