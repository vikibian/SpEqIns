package com.neu.test.entity;

public class ResultForTaskAndUser<T,M> {
    public String message;
    //请求中存放  的返回数据
    public T content;

    public M UserInfo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public M getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(M userInfo) {
        UserInfo = userInfo;
    }
}
