package com.neu.test.entity;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {


    @Override
    public String toString() {
        return "Task{" +
                "TASKID='" + TASKID + '\'' +
                ", DEVCLASS='" + DEVCLASS + '\'' +
                ", DEVID='" + DEVID + '\'' +
                ", LOGINNAME='" + LOGINNAME + '\'' +
                ", RESULT='" + RESULT + '\'' +
                ", NEXT_INSSEIFTIME='" + NEXT_INSSEIFTIME + '\'' +
                ", DEADLINE='" + DEADLINE + '\'' +
                ", FANGANBIANHAO='" + FANGANBIANHAO + '\'' +
                ", JIANCHARENYUANDANWEI='" + JIANCHARENYUANDANWEI + '\'' +
                ", JIANCHADANWEIZUZHIDAIMA='" + JIANCHADANWEIZUZHIDAIMA + '\'' +
                ", LASTRUNWATERNUMBER='" + LASTRUNWATERNUMBER + '\'' +
                ", DEVZHUCEMA='" + DEVZHUCEMA + '\'' +
                ", DANWEIZUZHIJIGOUDAIMA='" + DANWEIZUZHIJIGOUDAIMA + '\'' +
                ", SHENHERENYUAN='" + SHENHERENYUAN + '\'' +
                ", SHENHERIQI='" + SHENHERIQI + '\'' +
                ", JIANCHARESULTSTATE='" + JIANCHARESULTSTATE + '\'' +
                ", JIANCHARENYUANNAME='" + JIANCHARENYUANNAME + '\'' +
                ", RUNWATERNUM='" + RUNWATERNUM + '\'' +
                ", CHECKDATE='" + CHECKDATE + '\'' +
                ", TASKTYPE='" + TASKTYPE + '\'' +
                ", USEUNITNAME='" + USEUNITNAME + '\'' +
                ", PLACE='" + PLACE + '\'' +
                ", LONGITUDE='" + LONGITUDE + '\'' +
                ", LATITUDE='" + LATITUDE + '\'' +
                '}';
    }
    private long TASKID;  //任务ID

    public String getTASKNAME() {
        return TASKNAME;
    }

    public void setTASKNAME(String TASKNAME) {
        this.TASKNAME = TASKNAME;
    }

    private String TASKNAME;  //任务ID
    private String DEVCLASS;  //设备种类
    private long DEVID;  //设备ID
    private String LOGINNAME;  //登录名
    private String RESULT;  //结果
    private String NEXT_INSSEIFTIME;  //下次检测时间
    private String DEADLINE;  //截止时间

    private String FANGANBIANHAO; //方案编号
    private String JIANCHARENYUANDANWEI; //检查人员单位
    private String JIANCHADANWEIZUZHIDAIMA; //检查单位组册代码

    private String LASTRUNWATERNUMBER;

    public String getLASTRUNWATERNUMBER() {
        return LASTRUNWATERNUMBER;
    }

    public void setLASTRUNWATERNUMBER(String LASTRUNWATERNUMBER) {
        this.LASTRUNWATERNUMBER = LASTRUNWATERNUMBER;
    }

    public String getFANGANBIANHAO() {
        return FANGANBIANHAO;
    }

    public void setFANGANBIANHAO(String FANGANBIANHAO) {
        this.FANGANBIANHAO = FANGANBIANHAO;
    }

    public String getJIANCHARENYUANDANWEI() {
        return JIANCHARENYUANDANWEI;
    }

    public void setJIANCHARENYUANDANWEI(String JIANCHARENYUANDANWEI) {
        this.JIANCHARENYUANDANWEI = JIANCHARENYUANDANWEI;
    }

    public String getJIANCHADANWEIZUZHIDAIMA() {
        return JIANCHADANWEIZUZHIDAIMA;
    }

    public void setJIANCHADANWEIZUZHIDAIMA(String JIANCHADANWEIZUZHIDAIMA) {
        this.JIANCHADANWEIZUZHIDAIMA = JIANCHADANWEIZUZHIDAIMA;
    }

    public String getDEVZHUCEMA() {
        return DEVZHUCEMA;
    }

    public void setDEVZHUCEMA(String DEVZHUCEMA) {
        this.DEVZHUCEMA = DEVZHUCEMA;
    }

    public String getDANWEIZUZHIJIGOUDAIMA() {
        return DANWEIZUZHIJIGOUDAIMA;
    }

    public void setDANWEIZUZHIJIGOUDAIMA(String DANWEIZUZHIJIGOUDAIMA) {
        this.DANWEIZUZHIJIGOUDAIMA = DANWEIZUZHIJIGOUDAIMA;
    }

    public String getSHENHERENYUAN() {
        return SHENHERENYUAN;
    }

    public void setSHENHERENYUAN(String SHENHERENYUAN) {
        this.SHENHERENYUAN = SHENHERENYUAN;
    }

    public String getSHENHERIQI() {
        return SHENHERIQI;
    }

    public void setSHENHERIQI(String SHENHERIQI) {
        this.SHENHERIQI = SHENHERIQI;
    }

    public String getJIANCHARESULTSTATE() {
        return JIANCHARESULTSTATE;
    }

    public void setJIANCHARESULTSTATE(String JIANCHARESULTSTATE) {
        this.JIANCHARESULTSTATE = JIANCHARESULTSTATE;
    }

    public String getJIANCHARENYUANNAME() {
        return JIANCHARENYUANNAME;
    }

    public void setJIANCHARENYUANNAME(String JIANCHARENYUANNAME) {
        this.JIANCHARENYUANNAME = JIANCHARENYUANNAME;
    }

    public String getRUNWATERNUM() {
        return RUNWATERNUM;
    }

    public void setRUNWATERNUM(String RUNWATERNUMBER) {
        this.RUNWATERNUM = RUNWATERNUMBER;
    }

    private String DEVZHUCEMA; //设备注册码
    private String DANWEIZUZHIJIGOUDAIMA; //单位组织机构代码
    private String SHENHERENYUAN; //审核人员
    private String SHENHERIQI; //审核日期
    private String JIANCHARESULTSTATE; //检查结果状态
    private String JIANCHARENYUANNAME; //检察人员姓名
    private String RUNWATERNUM; //流水号

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

    public String getDEVCLASS() {
        return DEVCLASS;
    }

    public void setDEVCLASS(String DEVCLASS) {
        this.DEVCLASS = DEVCLASS;
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
