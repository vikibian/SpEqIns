package com.neu.test.entity;

import java.io.Serializable;

public class Device implements Serializable {


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    private String deviceName;//设备名
    private String deviceAddress;//地点
    private String id;//任务名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String status;//任务状态
    private String startTime;//开始时间
    private String endTime;//截至时间

    public Device(String deviceName,String deviceAddress,String endTime){

        setDeviceName(deviceName);
        setDeviceAddress(deviceAddress);
        setEndTime(endTime);
    }



}
