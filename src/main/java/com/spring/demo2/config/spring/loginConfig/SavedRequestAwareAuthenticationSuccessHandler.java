package com.spring.demo2.config.spring.loginConfig;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class SavedRequestAwareAuthenticationSuccessHandler extends
    org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    log.debug("--> SavedRequestAwareAuthenticationSuccessHandler");
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
