package com.neu.test.entity;

import java.io.Serializable;

public class DetectionItem implements Serializable {

    private String ID;  //ID  任务
    private String CHECKITEM;  //检测项
    private String CHECKCONTENT;  //检测内容
    private String DEVCLASS;  //设备种类


    private String itemContent;


    public String getResultStatus() {
        return ResultStatus;
    }

    public void setResultStatus(String resultStatus) {
        ResultStatus = resultStatus;
    }

    private String ResultStatus = null;

    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getCHECKITEM() {
        return CHECKITEM;
    }
    public void setCHECKITEM(String cHECKITEM) {
        CHECKITEM = cHECKITEM;
    }
    public String getCHECKCONTENT() {
        return CHECKCONTENT;
    }
    public void setCHECKCONTENT(String cHECKCONTENT) {
        CHECKCONTENT = cHECKCONTENT;
    }
    public String getDEVCLASS() {
        return DEVCLASS;
    }
    public void setDEVCLASS(String dEVCLASS) {
        DEVCLASS = dEVCLASS;
    }


    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
}
