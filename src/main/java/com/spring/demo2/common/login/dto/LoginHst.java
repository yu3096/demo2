package com.spring.demo2.common.login.dto;

import com.spring.demo2.common.dto.DBInformation;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginHst extends DBInformation {
  private String loginHstSeq;
  private String loginInfoSeq;
  private Date loginDttm;
  private String loginIp;
  private String accessToken;
  private String refreshToken;

  @Builder
  public LoginHst(String regSeq, Date regDttm, String chgSeq, Date chgDttm,
      String loginHstSeq, String loginInfoSeq, Date loginDttm, String loginIp,
      String accessToken, String refreshToken) {
    super(regSeq, regDttm, chgSeq, chgDttm);
    this.loginHstSeq = loginHstSeq;
    this.loginInfoSeq = loginInfoSeq;
    this.loginDttm = loginDttm;
    this.loginIp = loginIp;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
