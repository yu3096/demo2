package com.spring.demo2.common.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DBInformation {
  private String regSeq;
  private Date regDttm;
  private String chgSeq;
  private Date chgDttm;

  public DBInformation(String regSeq, Date regDttm, String chgSeq, Date chgDttm) {
    this.regSeq = regSeq;
    this.regDttm = regDttm;
    this.chgSeq = chgSeq;
    this.chgDttm = chgDttm;
  }

  public DBInformation(String regSeq, String chgSeq) {
    this.regSeq = regSeq;
    this.chgSeq = chgSeq;
  }
}
