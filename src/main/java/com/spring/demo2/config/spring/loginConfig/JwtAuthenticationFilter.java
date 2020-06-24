package com.spring.demo2.config.spring.loginConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private boolean postOnly = true;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (this.postOnly && !"POST".equals(request.getMethod())) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    } else {
      log.debug("--- login call -> ");

      setUsernameParameter("userId");
      setPasswordParameter("userPw");
      String userId = obtainUsername(request);
      String userPw = obtainPassword(request);

      log.debug("    - userId : {}", userId);
      log.debug("    - userPw : {}", userPw);

      if (StringUtils.isEmpty(userId)) {
        userId = "";
      }
      if (StringUtils.isEmpty(userPw)) {
        userPw = "";
      }

      userId = userId.trim();
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, userPw);

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    }
  }
}
