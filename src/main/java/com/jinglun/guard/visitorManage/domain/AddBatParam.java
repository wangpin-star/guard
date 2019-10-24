package com.jinglun.guard.visitorManage.domain;

import com.jinglun.guard.devicemanage.domain.Device;

import java.util.List;

public class AddBatParam {
    private BatVisitor batVisitor;
    private List<Device> deviceList;

    public BatVisitor getBatVisitor() {
        return batVisitor;
    }

    public void setBatVisitor(BatVisitor batVisitor) {
        this.batVisitor = batVisitor;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
