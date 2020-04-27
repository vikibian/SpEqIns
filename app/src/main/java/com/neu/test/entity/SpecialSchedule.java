package com.neu.test.entity;
import java.util.Date;

public class SpecialSchedule extends BaseTshcedule {

  private String STARTTIME;
  private String ENDTIME;
  private String DEADTIME;
  private String TASKNAME;

  public String getSTARTTIME() {
    return STARTTIME;
  }

  public void setSTARTTIME(String STARTTIME) {
    this.STARTTIME = STARTTIME;
  }

  public String getENDTIME() {
    return ENDTIME;
  }

  public void setENDTIME(String ENDTIME) {
    this.ENDTIME = ENDTIME;
  }

  public String getDEADTIME() {
    return DEADTIME;
  }

  public void setDEADTIME(String DEADTIME) {
    this.DEADTIME = DEADTIME;
  }

  public String getTASKNAME() {
    return TASKNAME;
  }

  public void setTASKNAME(String TASKNAME) {
    this.TASKNAME = TASKNAME;
  }

  @Override
  public String toString() {
    return super.toString()+"SpecialSchedule [STARTTIME=" + STARTTIME + ", ENDTIME=" + ENDTIME + ", DEADTIME=" + DEADTIME
      + ", TASKNAME=" + TASKNAME + "]";
  }


}
