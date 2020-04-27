package com.neu.test.entity;

import java.util.Date;

public class DailySchedule extends BaseTshcedule{
  private String INSPECT_CYCLE;
  private String LASTDOTIME;
  private String NEXTDOTIME;
  private String JIANCHAXIANG;
  private String RWBZ;
  public String getINSPECT_CYCLE() {
    return INSPECT_CYCLE;
  }
  public void setINSPECT_CYCLE(String iNSPECT_CYCLE) {
    INSPECT_CYCLE = iNSPECT_CYCLE;
  }
  public String getJIANCHAXIANG() {
    return JIANCHAXIANG;
  }
  public void setJIANCHAXIANG(String jIANCHAXIANG) {
    JIANCHAXIANG = jIANCHAXIANG;
  }
  public String getRWBZ() {
    return RWBZ;
  }

  public String getLASTDOTIME() {
    return LASTDOTIME;
  }

  public void setLASTDOTIME(String LASTDOTIME) {
    this.LASTDOTIME = LASTDOTIME;
  }

  public String getNEXTDOTIME() {
    return NEXTDOTIME;
  }

  public void setNEXTDOTIME(String NEXTDOTIME) {
    this.NEXTDOTIME = NEXTDOTIME;
  }

  public void setRWBZ(String rWBZ) {
    RWBZ = rWBZ;
  }
  @Override
  public String toString() {
    return super.toString()+"DailySchedule [INSPECT_CYCLE=" + INSPECT_CYCLE + ", LASTDOTIME=" + LASTDOTIME + ", NEXTDOTIME="
      + NEXTDOTIME + ", JIANCHAXIANG=" + JIANCHAXIANG + ", RWBZ=" + RWBZ + "]";
  }


}
