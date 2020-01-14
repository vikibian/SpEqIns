package com.neu.test.entity;

import java.io.Serializable;

public class DetectionItem implements Serializable {

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

    public DetectionItem(String itemContent){
        setItemContent(itemContent);
    }
    public DetectionItem(String itemContent,String resultStatus){
        setItemContent(itemContent);
        setResultStatus(resultStatus);
    }
}
