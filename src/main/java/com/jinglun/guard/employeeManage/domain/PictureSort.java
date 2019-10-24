package com.jinglun.guard.employeeManage.domain;

import java.util.List;

public class PictureSort {
    private Integer resultCode;
    private List<FaceInfo.Picture> comparePictureList;
    private List<FaceInfo.Picture> headPictureList;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public List<FaceInfo.Picture> getComparePictureList() {
        return comparePictureList;
    }

    public void setComparePictureList(List<FaceInfo.Picture> comparePictureList) {
        this.comparePictureList = comparePictureList;
    }

    public List<FaceInfo.Picture> getHeadPictureList() {
        return headPictureList;
    }

    public void setHeadPictureList(List<FaceInfo.Picture> headPictureList) {
        this.headPictureList = headPictureList;
    }
}
