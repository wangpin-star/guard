package com.jinglun.guard.employeeManage.domain;

import com.jinglun.guard.devicemanage.domain.Device;

import java.util.List;

public class AddFaceInfoParam {
    private FaceInfo faceInfo;

    private List<FaceInfo.Picture> pictureList;

    public AddFaceInfoParam() {
    }

    public List<FaceInfo.Picture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<FaceInfo.Picture> pictureList) {
        this.pictureList = pictureList;
    }

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

    private List<Device> deviceList;

    public AddFaceInfoParam(FaceInfo faceInfo, List<Device> deviceList) {
        this.faceInfo = faceInfo;
        this.deviceList = deviceList;
    }
}
