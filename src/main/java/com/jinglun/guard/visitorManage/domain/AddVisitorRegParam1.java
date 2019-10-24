package com.jinglun.guard.visitorManage.domain;

import java.util.List;

import com.jinglun.guard.employeeManage.domain.FaceInfo;

public class AddVisitorRegParam1 {
	private FaceInfo faceInfo;
    private FaceVisitInfo faceVisitInfo;
    private List<Integer> deviceList;
   

	public List<Integer> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Integer> deviceList) {
		this.deviceList = deviceList;
	}

	public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    

    public FaceVisitInfo getFaceVisitInfo() {
        return faceVisitInfo;
    }

    public void setFaceVisitInfo(FaceVisitInfo faceVisitInfo) {
        this.faceVisitInfo = faceVisitInfo;
    }
}
