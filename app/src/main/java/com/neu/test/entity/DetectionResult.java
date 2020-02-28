package com.neu.test.entity;

import java.io.Serializable;

public class DetectionResult implements Serializable {


  private String DEVCLASS = "";  //设备种类
  private String DEVID = "";  //设备名
  private String CHECKCONTENT;  //检测内容
  private String STATUS;  //检查项合格情况
  private String REFJIM = "";  //图片地址
  private String REFJVI = "";  //视频地址
  private String TASKID;  //任务ID
  private String REGISTERCODE = "";  //注册代码
  private String SUGGESTION =  "";  //检查情况说明
  private boolean isHaveDetail = false;

  public boolean isHaveDetail() {
    return isHaveDetail;
  }

  public void setHaveDetail(boolean haveDetail) {
    isHaveDetail = haveDetail;
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

  public String getCHECKCONTENT() {
    return CHECKCONTENT;
  }

  public void setCHECKCONTENT(String CHECKCONTENT) {
    this.CHECKCONTENT = CHECKCONTENT;
  }

  public String getSTATUS() {
    return STATUS;
  }

  public void setSTATUS(String STATUS) {
    this.STATUS = STATUS;
  }

  public String getREFJIM() {
    return REFJIM;
  }

  public void setREFJIM(String REFJIM) {
    this.REFJIM = REFJIM;
  }

  public String getREFJVI() {
    return REFJVI;
  }

  public void setREFJVI(String REFJVI) {
    this.REFJVI = REFJVI;
  }

  public String getTASKID() {
    return TASKID;
  }

  public void setTASKID(String TASKID) {
    this.TASKID = TASKID;
  }

  public String getREGISTERCODE() {
    return REGISTERCODE;
  }

  public void setREGISTERCODE(String REGISTERCODE) {
    this.REGISTERCODE = REGISTERCODE;
  }

  public String getSUGGESTION() {
    return SUGGESTION;
  }

  public void setSUGGESTION(String SUGGESTION) {
    this.SUGGESTION = SUGGESTION;
  }





}
