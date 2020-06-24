package com.spring.demo2.common.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
  @GetMapping("/index")
  public String root(){
    return "로그인 성공";
  }
}
