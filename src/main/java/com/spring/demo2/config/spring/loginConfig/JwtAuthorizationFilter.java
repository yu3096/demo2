package com.spring.demo2.config.spring.loginConfig;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    log.debug("JwtAuthorizationFilter.doFilterInternal ::::");
    super.doFilterInternal(request, response, chain);
  }

  @Override
  protected void onSuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, Authentication authResult) throws IOException {
    super.onSuccessfulAuthentication(request, response, authResult);
  }

  @Override
  protected void onUnsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {
    super.onUnsuccessfulAuthentication(request, response, failed);
  }
}
