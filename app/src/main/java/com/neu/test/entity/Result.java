package com.neu.test.entity;

public class Result<T> {
    public String message;
    //请求中存放  的返回数据
    public T content;

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
}
