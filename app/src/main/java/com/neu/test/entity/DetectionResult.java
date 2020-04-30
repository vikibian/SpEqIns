package com.neu.test.entity;

import com.neu.test.tree.TreeNodeGroup;
import com.neu.test.tree.TreeNodeId;
import com.neu.test.tree.TreeNodePid;

import java.io.Serializable;

public class DetectionResult implements Serializable {


  @TreeNodeId
  private int id = 0;
  @TreeNodePid
  private int parentId = 0;
  @TreeNodeGroup
  private boolean isGroup = false;

  private String name = "";

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



  @Override
  public String toString() {
    return "DetectionResult{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", isGroup=" + isGroup +
            ", name='" + name + '\'' +
            ", DEVCLASS='" + DEVCLASS + '\'' +
            ", DEVID='" + DEVID + '\'' +
            ", CHECKCONTENT='" + CHECKCONTENT + '\'' +
            ", JIANCHAXIANGBIANHAO='" + JIANCHAXIANGBIANHAO + '\'' +
            ", STATUS='" + STATUS + '\'' +
            ", REFJIM='" + REFJIM + '\'' +
            ", REFJVI='" + REFJVI + '\'' +
            ", TASKID='" + TASKID + '\'' +
            ", ISCHANGED='" + ISCHANGED + '\'' +
            ", CHANGEDIMAGE='" + CHANGEDIMAGE + '\'' +
            ", CHANGEDVIDEO='" + CHANGEDVIDEO + '\'' +
            ", JIANCHAXIANGTITLE='" + JIANCHAXIANGTITLE + '\'' +
            ", LAW='" + LAW + '\'' +
            ", REGISTERCODE='" + REGISTERCODE + '\'' +
            ", SUGGESTION='" + SUGGESTION + '\'' +
            ", LOGINNAME='" + LOGINNAME + '\'' +
            ", RUNWATERNUM='" + RUNWATERNUM + '\'' +
            ", ISHAVEDETAIL='" + ISHAVEDETAIL + '\'' +
            ", CHANGEDWAY='" + CHANGEDWAY + '\'' +
            ", CHANGEDACTION='" + CHANGEDACTION + '\'' +
            ", CHANGEDFINISHTIME='" + CHANGEDFINISHTIME + '\'' +
            ", CHANGEDRESULT='" + CHANGEDRESULT + '\'' +
            ", YINHUANLEVEL='" + YINHUANLEVEL + '\'' +
            ", PHONE='" + PHONE + '\'' +
            ", LONGITUDE='" + LONGITUDE + '\'' +
            ", LATITUDE='" + LATITUDE + '\'' +
            '}';
  }

  public boolean isGroup() {
    return isGroup;
  }

  public void setGroup(boolean group) {
    isGroup = group;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DetectionResult(int id, int parentId, boolean isGroup, String name) {
    this.id = id;
    this.parentId = parentId;
    this.isGroup = isGroup;
    this.name = name;
  }


  public DetectionResult(){

  }

  private String DEVCLASS = "";  //设备种类
  private long DEVID ;  //设备名
  private String CHECKCONTENT;  //检测内容
  private String JIANCHAXIANGBIANHAO;  //检测ID
  private String STATUS = "2";  //检查项合格情况
  private String REFJIM = "";  //图片地址
  private String REFJVI = "";  //视频地址
  private long TASKID;  //任务ID

  private String ISCHANGED = "0";  //是否整改
  private String QIYEMINGCHENG;

  public String getISCHANGED() {
    return ISCHANGED;
  }

  public void setISCHANGED(String ISCHANGED) {
    this.ISCHANGED = ISCHANGED;
  }

  public String getCHANGEDIMAGE() {
    return CHANGEDIMAGE;
  }

  public void setCHANGEDIMAGE(String CHANGEDIMAGE) {
    this.CHANGEDIMAGE = CHANGEDIMAGE;
  }

  public String getCHANGEDVIDEO() {
    return CHANGEDVIDEO;
  }

  public void setCHANGEDVIDEO(String CHANGEDVIDEO) {
    this.CHANGEDVIDEO = CHANGEDVIDEO;
  }

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

  private String CHANGEDIMAGE = "";  //整改的图片地址
  private String CHANGEDVIDEO = "";  //整改的视频地址
  private String JIANCHAXIANGTITLE;  //
  private String LAW;  //整改的视频地址

  public String getJIANCHAXIANGBIANHAO() {
    return JIANCHAXIANGBIANHAO;
  }

  public void setJIANCHAXIANGBIANHAO(String JIANCHAXIANGID) {
    this.JIANCHAXIANGBIANHAO = JIANCHAXIANGID;
  }

  private String REGISTERCODE = "";  //注册代码
  private String SUGGESTION =  "";  //检查情况说明

  public String getLOGINNAME() {
    return LOGINNAME;
  }

  public void setLOGINNAME(String USERNAME) {
    this.LOGINNAME = USERNAME;
  }

  private String LOGINNAME =  "";  //检查人员

  public String getRUNWATERNUM() {
    return RUNWATERNUM;
  }

  public void setRUNWATERNUM(String RUNWATERNUM) {
    this.RUNWATERNUM = RUNWATERNUM;
  }

  private String RUNWATERNUM =  "";  //流水号

  private String ISHAVEDETAIL = "0";

  public String getISHAVEDETAIL() {
    return ISHAVEDETAIL;
  }

  public void setISHAVEDETAIL(String ISHAVEDETAIL) {
    this.ISHAVEDETAIL = ISHAVEDETAIL;
  }

  public String getDEVCLASS() {
    return DEVCLASS;
  }

  public void setDEVCLASS(String DEVCLASS) {
    this.DEVCLASS = DEVCLASS;
  }

  public long getDEVID() {
    return DEVID;
  }

  public void setDEVID(long DEVID) {
    this.DEVID = DEVID;
  }

  public long getTASKID() {
    return TASKID;
  }

  public void setTASKID(long TASKID) {
    this.TASKID = TASKID;
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

  //测试
  private String CHANGEDWAY = "" ;
  private String CHANGEDACTION;
  private String CHANGEDFINISHTIME;
  private String CHANGEDRESULT;

  public String getCHANGEDWAY() {
    return CHANGEDWAY;
  }

  public void setCHANGEDWAY(String CHANGEDWAY) {
    this.CHANGEDWAY = CHANGEDWAY;
  }

  public String getCHANGEDACTION() {
    return CHANGEDACTION;
  }

  public void setCHANGEDACTION(String CHANGEDACTION) {
    this.CHANGEDACTION = CHANGEDACTION;
  }

  public String getCHANGEDFINISHTIME() {
    return CHANGEDFINISHTIME;
  }

  public void setCHANGEDFINISHTIME(String CHANGEDFINISHTIME) {
    this.CHANGEDFINISHTIME = CHANGEDFINISHTIME;
  }

  public String getCHANGEDRESULT() {
    return CHANGEDRESULT;
  }

  public void setCHANGEDRESULT(String CHANGEDRESULT) {
    this.CHANGEDRESULT = CHANGEDRESULT;
  }

  private String YINHUANLEVEL = "";//隐患等级
  private String PHONE = "";//手机号
  private String LONGITUDE = "";//经度
  private String LATITUDE = "";//纬度

  public String getYINHUANLEVEL() {
    return YINHUANLEVEL;
  }

  public void setYINHUANLEVEL(String YINHUANLEVEL) {
    this.YINHUANLEVEL = YINHUANLEVEL;
  }

  public String getPHONE() {
    return PHONE;
  }

  public void setPHONE(String PHONE) {
    this.PHONE = PHONE;
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

  public String getQIYEMINGCHENG() {
    return QIYEMINGCHENG;
  }

  public void setQIYEMINGCHENG(String QIYEMINGCHENG) {
    this.QIYEMINGCHENG = QIYEMINGCHENG;
  }
}
