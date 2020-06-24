package com.spring.demo2.common.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestContextHolder {

  public static HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
        .getRequestAttributes()).getRequest();
  }
}
