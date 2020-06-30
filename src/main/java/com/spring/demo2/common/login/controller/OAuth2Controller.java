package com.spring.demo2.common.login.controller;

import com.google.gson.JsonObject;
import com.spring.demo2.common.login.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class OAuth2Controller {
  @Autowired private RestTemplate restTemplate;

  @PostMapping("/token/{accessToken}/{refreshToken}")
  public String getToken(@PathVariable("accessToken") String accessToken, @PathVariable("refreshToken") String refreshToken){
    return TokenDto.builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build()
                   .toJson().toString();
  }

  @PostMapping("/test")
  public String aa(){
    return "확인";
  }
}