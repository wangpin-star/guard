
package com.jinglun.guard.employeeManage.domain;

import java.util.List;

public class AddFaceInfoParam1 {
    private FaceInfo faceInfo;

    private List<FaceInfo.Picture> pictureList;

    public AddFaceInfoParam1() {
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


    public List<Integer> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Integer> deviceList) {
		this.deviceList = deviceList;
	}


	private List<Integer> deviceList;

}
