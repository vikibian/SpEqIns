package com.neu.test.entity;

public class User {
    private int USERID;  //用户ID
    private String LOGINNAME; //账户名
    private String LOGINPWD;  //登录密码
    private String UNITID;  //企业ID
    private String PERMISSION;  //业务权限
    private String DEVCLASS;  // 设备类别
    private int ROLEID;  //角色
    private String USERNAME;   //用户名
    private String REGISTATUS;   //用户状态
    private boolean IS_DEL;  //删除标志
    private String ACCOUNTSTAUTS;  //账号状态
    private String USEUNITNAME;  //单位名
    private String PHONE;  //手机
    private String ZGZTYPE;  //资格证类型
//    private String ZHENGJIANHAO;  //证件号

    private String IDCARD;  //证件号
    private String UNITTYPE;  //单位类别
    private String APPROVENAME;  //审核人
    private String APPROVETIME;  //审核时间
    private String EMAIL;  //邮箱
    private String UNITCODE; //统一社会信用代码
    private String PROVINCE; // 省级
    private String CITY;   //市级
    private String DISTRICT; // 区级
    private String SUPUNIT; // 上级监察单位（使用单位）

    public int getUSERID() {
        return USERID;
    }

    public void setUSERID(int USERID) {
        this.USERID = USERID;
    }

    public String getLOGINNAME() {
        return LOGINNAME;
    }

    public void setLOGINNAME(String LOGINNAME) {
        this.LOGINNAME = LOGINNAME;
    }

    public String getLOGINPWD() {
        return LOGINPWD;
    }

    public void setLOGINPWD(String LOGINPWD) {
        this.LOGINPWD = LOGINPWD;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getPERMISSION() {
        return PERMISSION;
    }

    public void setPERMISSION(String PERMISSION) {
        this.PERMISSION = PERMISSION;
    }

    public String getDEVCLASS() {
        return DEVCLASS;
    }

    public void setDEVCLASS(String DEVCLASS) {
        this.DEVCLASS = DEVCLASS;
    }

    public int getROLEID() {
        return ROLEID;
    }

    public void setROLEID(int ROLEID) {
        this.ROLEID = ROLEID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getREGISTATUS() {
        return REGISTATUS;
    }

    public void setREGISTATUS(String REGISTATUS) {
        this.REGISTATUS = REGISTATUS;
    }

    public boolean isIS_DEL() {
        return IS_DEL;
    }

    public void setIS_DEL(boolean IS_DEL) {
        this.IS_DEL = IS_DEL;
    }

    public String getACCOUNTSTAUTS() {
        return ACCOUNTSTAUTS;
    }

    public void setACCOUNTSTAUTS(String ACCOUNTSTAUTS) {
        this.ACCOUNTSTAUTS = ACCOUNTSTAUTS;
    }

    public String getUSEUNITNAME() {
        return USEUNITNAME;
    }

    public void setUSEUNITNAME(String USEUNITNAME) {
        this.USEUNITNAME = USEUNITNAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getZGZTYPE() {
        return ZGZTYPE;
    }

    public void setZGZTYPE(String ZGZTYPE) {
        this.ZGZTYPE = ZGZTYPE;
    }

//    public String getZHENGJIANHAO() {
//        return ZHENGJIANHAO;
//    }

//    public void setZHENGJIANHAO(String ZHENGJIANHAO) {
//        this.ZHENGJIANHAO = ZHENGJIANHAO;
//    }


    public String getIDCARD() {
        return IDCARD;
    }

    public void setIDCARD(String IDCARD) {
        this.IDCARD = IDCARD;
    }

    public String getUNITTYPE() {
        return UNITTYPE;
    }

    public void setUNITTYPE(String UNITTYPE) {
        this.UNITTYPE = UNITTYPE;
    }

    public String getAPPROVENAME() {
        return APPROVENAME;
    }

    public void setAPPROVENAME(String APPROVENAME) {
        this.APPROVENAME = APPROVENAME;
    }

    public String getAPPROVETIME() {
        return APPROVETIME;
    }

    public void setAPPROVETIME(String APPROVETIME) {
        this.APPROVETIME = APPROVETIME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getUNITCODE() {
        return UNITCODE;
    }

    public void setUNITCODE(String UNITCODE) {
        this.UNITCODE = UNITCODE;
    }

    public String getPROVINCE() {
        return PROVINCE;
    }

    public void setPROVINCE(String PROVINCE) {
        this.PROVINCE = PROVINCE;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public void setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
    }

    public String getSUPUNIT() {
        return SUPUNIT;
    }

    public void setSUPUNIT(String SUPUNIT) {
        this.SUPUNIT = SUPUNIT;
    }
}
