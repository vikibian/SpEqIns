package com.neu.test.entity;

import java.io.Serializable;

public class JianChaItem implements Serializable {

  private int XIANGMUID;
  private String XIANGMUMINGCHENG;
  private boolean SHIFOUQUZHENG;
  private boolean SHIFOUHEGEQUZHENG;
  private boolean SHIFOUBUHEGEQUZHENG;

  public int getXIANGMUID() {
    return XIANGMUID;
  }

  public void setXIANGMUID(int XIANGMUID) {
    this.XIANGMUID = XIANGMUID;
  }

  public String getXIANGMUMINGCHENG() {
    return XIANGMUMINGCHENG;
  }

  public void setXIANGMUMINGCHENG(String XIANGMUMINGCHENG) {
    this.XIANGMUMINGCHENG = XIANGMUMINGCHENG;
  }

  public boolean isSHIFOUQUZHENG() {
    return SHIFOUQUZHENG;
  }

  public void setSHIFOUQUZHENG(boolean SHIFOUQUZHENG) {
    this.SHIFOUQUZHENG = SHIFOUQUZHENG;
  }

  public boolean isSHIFOUHEGEQUZHENG() {
    return SHIFOUHEGEQUZHENG;
  }

  public void setSHIFOUHEGEQUZHENG(boolean SHIFOUHEGEQUZHENG) {
    this.SHIFOUHEGEQUZHENG = SHIFOUHEGEQUZHENG;
  }

  public boolean isSHIFOUBUHEGEQUZHENG() {
    return SHIFOUBUHEGEQUZHENG;
  }

  public void setSHIFOUBUHEGEQUZHENG(boolean SHIFOUBUHEGEQUZHENG) {
    this.SHIFOUBUHEGEQUZHENG = SHIFOUBUHEGEQUZHENG;
  }



}
