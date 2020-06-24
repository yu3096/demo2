package com.spring.demo2.common.login.dto;

import com.spring.demo2.common.dto.DBInformation;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class LoginHst extends DBInformation {
  private String loginHstSeq;
  private String loginInfoSeq;
  private Date loginDttm;
  private String loginIp;
}
