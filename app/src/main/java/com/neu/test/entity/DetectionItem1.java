package com.neu.test.entity;

import java.io.Serializable;

public class DetectionItem1 implements Serializable {

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    private String itemContent;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    private String resultStatus;

    public DetectionItem1(){

    }
    public DetectionItem1(String itemContent){
        setItemContent(itemContent);
    }
    public DetectionItem1(String itemContent,String resultStatus){
        setItemContent(itemContent);
        setResultStatus(resultStatus);
    }
}
