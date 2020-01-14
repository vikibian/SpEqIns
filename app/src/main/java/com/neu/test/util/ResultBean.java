package com.neu.test.util;

import java.io.Serializable;


/*
    这个类主要用来在查询结果后详细查看某一项结果时 进行数据传递
 */

public class ResultBean implements Serializable {
    private String time;
    private String deviceType;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }

    public String getIschecked() {
        return ischecked;
    }

    public void setIschecked(String ischecked) {
        this.ischecked = ischecked;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String qualify;
    private String ischecked;
    private String user;
}
