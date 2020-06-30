package com.spring.demo2.common.login.dto;

import com.google.gson.JsonObject;
import com.spring.demo2.common.dto.DBInformation;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenDto extends DBInformation {
  private String accessToken;
  private String refreshToken;
  private Date refreshTokenExpiredDttm;

  @Builder
  public TokenDto(String regSeq, Date regDttm, String chgSeq, Date chgDttm,
      String accessToken, String refreshToken, Date refreshTokenExpiredDttm) {
    super(regSeq, regDttm, chgSeq, chgDttm);
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.refreshTokenExpiredDttm = refreshTokenExpiredDttm;
  }

  public JsonObject toJson(){
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("access-token", this.accessToken);
    jsonObj.addProperty("refresh-token", this.refreshToken);
    return jsonObj;
  }
}
