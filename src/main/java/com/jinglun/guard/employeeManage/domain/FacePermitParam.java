package com.jinglun.guard.employeeManage.domain;

import java.util.List;

public class FacePermitParam {
    private FaceInfo faceInfo;
    private List<Integer> termIdList;
    private int recId;

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public List<Integer> getTermIdList() {
        return termIdList;
    }

    public void setTermIdList(List<Integer> termIdList) {
        this.termIdList = termIdList;
    }
}
