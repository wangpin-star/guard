package com.jinglun.guard.visitorManage.domain;

import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.employeeManage.domain.FaceInfo;

import java.util.List;

public class AddVisitorRegParam {
    private FaceInfo faceInfo;
    private List<Device> deviceList;
    private FaceVisitInfo faceVisitInfo;

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public FaceVisitInfo getFaceVisitInfo() {
        return faceVisitInfo;
    }

    public void setFaceVisitInfo(FaceVisitInfo faceVisitInfo) {
        this.faceVisitInfo = faceVisitInfo;
    }
}
