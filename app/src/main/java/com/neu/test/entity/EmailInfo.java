package com.neu.test.entity;

/**
 * created by Viki on 2020/4/21
 * system login name : lg
 * created time : 18:15
 * email : 710256138@qq.com
 */
public class EmailInfo {
    private String ID = "";
    private String HOST = "";
    private String PORT = "";
    private String FROMADD = "";
    private String FROMPSW = "";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getFROMADD() {
        return FROMADD;
    }

    public void setFROMADD(String FROMADD) {
        this.FROMADD = FROMADD;
    }

    public String getFROMPSW() {
        return FROMPSW;
    }

    public void setFROMPSW(String FROMPSW) {
        this.FROMPSW = FROMPSW;
    }
}
