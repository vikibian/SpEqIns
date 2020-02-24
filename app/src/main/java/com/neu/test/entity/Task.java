package com.neu.test.entity;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String TASKID;  //任务ID
    private String DEVCLASS;  //设备种类
    private String DEVID;  //设备ID
    private String LOGINNAME;  //登录名
    private String RESULT;  //结果
    private String NEXT_INSSEIFTIME;  //下次检测时间
    private String DEADLINE;  //截止时间

    public String getTASKID() {
        return TASKID;
    }

    public void setTASKID(String TASKID) {
        this.TASKID = TASKID;
    }

    public String getDEVCLASS() {
        return DEVCLASS;
    }

    public void setDEVCLASS(String DEVCLASS) {
        this.DEVCLASS = DEVCLASS;
    }

    public String getDEVID() {
        return DEVID;
    }

    public void setDEVID(String DEVID) {
        this.DEVID = DEVID;
    }

    public String getLOGINNAME() {
        return LOGINNAME;
    }

    public void setLOGINNAME(String LOGINNAME) {
        this.LOGINNAME = LOGINNAME;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getNEXT_INSSEIFTIME() {
        return NEXT_INSSEIFTIME;
    }

    public void setNEXT_INSSEIFTIME(String NEXT_INSSEIFTIME) {
        this.NEXT_INSSEIFTIME = NEXT_INSSEIFTIME;
    }

    public String getDEADLINE() {
        return DEADLINE;
    }

    public void setDEADLINE(String DEADLINE) {
        this.DEADLINE = DEADLINE;
    }

    public String getCHECKDATE() {
        return CHECKDATE;
    }

    public void setCHECKDATE(String CHECKDATE) {
        this.CHECKDATE = CHECKDATE;
    }

    public String getTASKTYPE() {
        return TASKTYPE;
    }

    public void setTASKTYPE(String TASKTYPE) {
        this.TASKTYPE = TASKTYPE;
    }

    private String CHECKDATE;  //检测日期
    private String TASKTYPE;  //任务类型

    private String USEUNITNAME;
    private  String PLACE; //地址
    private String LONGITUDE;  //经度
    private String LATITUDE;  //纬度

    public String getUSEUNITNAME() {
        return USEUNITNAME;
    }

    public void setUSEUNITNAME(String USEUNITNAME) {
        this.USEUNITNAME = USEUNITNAME;
    }

    public String getPLACE() {
        return PLACE;
    }

    public void setPLACE(String PLACE) {
        this.PLACE = PLACE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }
}
