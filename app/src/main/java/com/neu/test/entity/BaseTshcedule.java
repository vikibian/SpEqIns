package com.neu.test.entity;

public class BaseTshcedule {

  private int ID;
  private String ORGANIZATIONCODE;
  private long TASKID;
  private long DEVID;
  private String WHODO;
  private String TASKTYPE;
  private String TASKBORNTIME;
  private String TASKBYWHO;
  private String ISARRANGE;
  private String PLACE;
  public int getID() {
    return ID;
  }
  public void setID(int iD) {
    ID = iD;
  }
  public String getORGANIZATIONCODE() {
    return ORGANIZATIONCODE;
  }
  public void setORGANIZATIONCODE(String oRGANIZATIONCODE) {
    ORGANIZATIONCODE = oRGANIZATIONCODE;
  }

  public long getTASKID() {
    return TASKID;
  }

  public void setTASKID(long TASKID) {
    this.TASKID = TASKID;
  }

  public long getDEVID() {
    return DEVID;
  }

  public void setDEVID(long DEVID) {
    this.DEVID = DEVID;
  }

  public String getWHODO() {
    return WHODO;
  }
  public void setWHODO(String wHODO) {
    WHODO = wHODO;
  }
  public String getTASKTYPE() {
    return TASKTYPE;
  }
  public void setTASKTYPE(String tASKTYPE) {
    TASKTYPE = tASKTYPE;
  }
  public String getTASKBORNTIME() {
    return TASKBORNTIME;
  }
  public void setTASKBORNTIME(String tASKBORNTIME) {
    TASKBORNTIME = tASKBORNTIME;
  }
  public String getTASKBYWHO() {
    return TASKBYWHO;
  }
  public void setTASKBYWHO(String tASKBYWHO) {
    TASKBYWHO = tASKBYWHO;
  }
  public String getISARRANGE() {
    return ISARRANGE;
  }
  public void setISARRANGE(String iSARRANGE) {
    ISARRANGE = iSARRANGE;
  }

  public String getPLACE() {
    return PLACE;
  }
  public void setPLACE(String pLACE) {
    PLACE = pLACE;
  }
  @Override
  public String toString() {
    return "BaseTshcedule [ID=" + ID + ", ORGANIZATIONCODE=" + ORGANIZATIONCODE + ", TASKID=" + TASKID + ", DEVID="
      + DEVID + ", WHODO=" + WHODO + ", TASKTYPE=" + TASKTYPE + ", TASKBORNTIME=" + TASKBORNTIME
      + ", TASKBYWHO=" + TASKBYWHO + ", ISARRANGE=" + ISARRANGE + ", PLACE=" + PLACE + "]";
  }




}

