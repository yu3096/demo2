package com.spring.demo2.config.spring.loginConfig;

import com.spring.demo2.common.utils.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
  @Autowired JwtTokenProvider jwtTokenProvider;

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String token = jwtTokenProvider.resolveToken(request);

    // If header does not contain BEARER or is null delegate to Spring impl and exit
    if(token == null || !token.startsWith("Bearer ")){
      chain.doFilter(request, response);
      return;
    }

    Authentication authentication = jwtTokenProvider.getUsernamePasswordAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Continue filter execution
    chain.doFilter(request, response);
  }



  @Override
  protected void onSuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, Authentication authResult) throws IOException {
    log.debug("성공????");
    super.onSuccessfulAuthentication(request, response, authResult);
  }

  @Override
  protected void onUnsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {
    log.debug("실패????");
    super.onUnsuccessfulAuthentication(request, response, failed);
  }
}
