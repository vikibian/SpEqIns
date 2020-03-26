package com.neu.test.entity;

import java.io.Serializable;

public class DetailTask implements Serializable {

  private String JIANCHAXIANGID;
  private String JIANCHAXIANGCONTENT;
  private String TASKID;
  private String DEVID;
  private String SHIFOUHEGEQUZHENG = "1";
  private String FANGANID;
  private String JIANCHAXIANGTITLE;  //

  public String getJIANCHAXIANGTITLE() {
    return JIANCHAXIANGTITLE;
  }

  public void setJIANCHAXIANGTITLE(String JIANCHAXIANGTITLE) {
    this.JIANCHAXIANGTITLE = JIANCHAXIANGTITLE;
  }

  public String getLAW() {
    return LAW;
  }

  public void setLAW(String LAW) {
    this.LAW = LAW;
  }

  private String LAW;  //

  public String getJIANCHAXIANGID() {
    return JIANCHAXIANGID;
  }

  public void setJIANCHAXIANGID(String JIANCHAXIANGID) {
    this.JIANCHAXIANGID = JIANCHAXIANGID;
  }

  public String getJIANCHAXIANGCONTENT() {
    return JIANCHAXIANGCONTENT;
  }

  public void setJIANCHAXIANGCONTENT(String JIANCHAXIANGCONTENT) {
    this.JIANCHAXIANGCONTENT = JIANCHAXIANGCONTENT;
  }

  public String getTASKID() {
    return TASKID;
  }

  public void setTASKID(String TASKID) {
    this.TASKID = TASKID;
  }

  public String getDEVID() {
    return DEVID;
  }

  public void setDEVID(String DEVID) {
    this.DEVID = DEVID;
  }

  public String getSHIFOUHEGEQUZHENG() {
    return SHIFOUHEGEQUZHENG;
  }

  public void setSHIFOUHEGEQUZHENG(String SHIFOUHEGEQUZHENG) {
    this.SHIFOUHEGEQUZHENG = SHIFOUHEGEQUZHENG;
  }

  public String getFANGANID() {
    return FANGANID;
  }

  public void setFANGANID(String FANGANID) {
    this.FANGANID = FANGANID;
  }

  public String getQIYEMINGCHENG() {
    return QIYEMINGCHENG;
  }

  public void setQIYEMINGCHENG(String QIYEMINGCHENG) {
    this.QIYEMINGCHENG = QIYEMINGCHENG;
  }

  public String getLOGINNAME() {
    return LOGINNAME;
  }

  public void setLOGINNAME(String LOGINNAME) {
    this.LOGINNAME = LOGINNAME;
  }

  public String getXIANGMUBIANHAO() {
    return XIANGMUBIANHAO;
  }

  public void setXIANGMUBIANHAO(String XIANGMUBIANHAO) {
    this.XIANGMUBIANHAO = XIANGMUBIANHAO;
  }

  public String getDEVCLASS() {
    return DEVCLASS;
  }

  public void setDEVCLASS(String DEVCLASS) {
    this.DEVCLASS = DEVCLASS;
  }

  private String QIYEMINGCHENG;
  private String LOGINNAME;
  private String XIANGMUBIANHAO;
  private String DEVCLASS;

}
