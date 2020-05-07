package com.neu.test.entity;

import com.neu.test.tree.TreeNodeGroup;
import com.neu.test.tree.TreeNodeId;
import com.neu.test.tree.TreeNodePid;

import java.io.Serializable;

public class CheckLists  implements Serializable {



  @TreeNodeId
  private int id = 0;
  @TreeNodePid
  private int parentId = 0;
  @TreeNodeGroup
  private boolean isGroup = false;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }


  public boolean isGroup() {
    return isGroup;
  }

  public void setGroup(boolean group) {
    isGroup = group;
  }


  public CheckLists(int id, int parentId, boolean isGroup) {
    this.id = id;
    this.parentId = parentId;
    this.isGroup = isGroup;
  }

  public CheckLists(){

  }

  private String STARANDNAME = "";//方案名称
  private String LEIXING = "";//类型（专项/日常）
  private String MONITIME = "";
  private String STATE = "";
  private String UNITNAME = "";
  private int ID;
  private String  MONITORCONTENT = "";//项目标题
  private String LAW = "";//法律法规
  private String MONITORITEMS = "";//检测项目
  private String MONITORTIME = "";//排查周期
  private String SHENGXIAOTIME = "";//生效时间
  private String DEVCLASS = "";//设备种类
  private String PAICHATUJING;
  private String CRATETIME;
  private String CRATETPERSON;

  private String STARANDMIAOSHU;
  private int QINGDANID;

  public String getSTARANDNAME() {
    return STARANDNAME;
  }

  public void setSTARANDNAME(String STARANDNAME) {
    this.STARANDNAME = STARANDNAME;
  }

  public String getLEIXING() {
    return LEIXING;
  }

  public void setLEIXING(String LEIXING) {
    this.LEIXING = LEIXING;
  }

  public String getMONITIME() {
    return MONITIME;
  }

  public void setMONITIME(String MONITIME) {
    this.MONITIME = MONITIME;
  }

  public String getSTATE() {
    return STATE;
  }

  public void setSTATE(String STATE) {
    this.STATE = STATE;
  }

  public String getUNITNAME() {
    return UNITNAME;
  }

  public void setUNITNAME(String UNITNAME) {
    this.UNITNAME = UNITNAME;
  }

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public String getMONITORCONTENT() {
    return MONITORCONTENT;
  }

  public void setMONITORCONTENT(String MONITORCONTENT) {
    this.MONITORCONTENT = MONITORCONTENT;
  }

  public String getLAW() {
    return LAW;
  }

  public void setLAW(String LAW) {
    this.LAW = LAW;
  }

  public String getMONITORITEMS() {
    return MONITORITEMS;
  }

  public void setMONITORITEMS(String MONITORITEMS) {
    this.MONITORITEMS = MONITORITEMS;
  }

  public String getMONITORTIME() {
    return MONITORTIME;
  }

  public void setMONITORTIME(String MONITORTIME) {
    this.MONITORTIME = MONITORTIME;
  }

  public String getSHENGXIAOTIME() {
    return SHENGXIAOTIME;
  }

  public void setSHENGXIAOTIME(String SHENGXIAOTIME) {
    this.SHENGXIAOTIME = SHENGXIAOTIME;
  }

  public String getDEVCLASS() {
    return DEVCLASS;
  }

  public void setDEVCLASS(String DEVCLASS) {
    this.DEVCLASS = DEVCLASS;
  }

  public String getPAICHATUJING() {
    return PAICHATUJING;
  }

  public void setPAICHATUJING(String PAICHATUJING) {
    this.PAICHATUJING = PAICHATUJING;
  }

  public String getCRATETIME() {
    return CRATETIME;
  }

  public void setCRATETIME(String CRATETIME) {
    this.CRATETIME = CRATETIME;
  }

  public String getCRATETPERSON() {
    return CRATETPERSON;
  }

  public void setCRATETPERSON(String CRATETPERSON) {
    this.CRATETPERSON = CRATETPERSON;
  }

  public String getSTARANDMIAOSHU() {
    return STARANDMIAOSHU;
  }

  public void setSTARANDMIAOSHU(String STARANDMIAOSHU) {
    this.STARANDMIAOSHU = STARANDMIAOSHU;
  }

  public int getQINGDANID() {
    return QINGDANID;
  }

  public void setQINGDANID(int QINGDANID) {
    this.QINGDANID = QINGDANID;
  }
}
