package com.neu.test.entity;

public class DoubleData<T,M> {
  private T cycle;
  private M FAName;

  public T getCycle() {
    return cycle;
  }

  public void setCycle(T cycle) {
    this.cycle = cycle;
  }

  public M getFAName() {
    return FAName;
  }

  public void setFAName(M FAName) {
    this.FAName = FAName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;

}
