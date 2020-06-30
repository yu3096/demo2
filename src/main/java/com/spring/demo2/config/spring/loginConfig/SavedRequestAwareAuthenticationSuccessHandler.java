package com.spring.demo2.config.spring.loginConfig;

import com.spring.demo2.common.login.dto.LoginHst;
import com.spring.demo2.common.login.dto.LoginUserPrincipal;
import com.spring.demo2.common.login.dto.TokenDto;
import com.spring.demo2.common.login.service.LoginService;
import com.spring.demo2.common.utils.JwtTokenProvider;
import com.spring.demo2.common.utils.RequestContextHolder;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Slf4j
public class SavedRequestAwareAuthenticationSuccessHandler extends
    org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired private JwtTokenProvider jwtTokenProvider;
  @Autowired private LoginService loginService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {

    LoginUserPrincipal loginUserPrincipal = (LoginUserPrincipal) authentication.getPrincipal();
    String accessToken = jwtTokenProvider.generatorAccessToken(loginUserPrincipal.getLoginId(), authentication.getAuthorities());
    String refreshToken = jwtTokenProvider.generatorRefreshToken(accessToken);

    log.info("access-token: [{}] / refresh-token: [{}]", accessToken, refreshToken);

    response.addHeader("Authorization", "Bearer " + accessToken);
    String targetUrl = getDefaultTargetUrl() + "/" + accessToken + "/" + refreshToken;

    TokenDto tokenDto = TokenDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .refreshTokenExpiredDttm(jwtTokenProvider.getTokenClaims(refreshToken).getExpiration())
                                .regSeq(loginUserPrincipal.getLoginInfoSeq())
                                .chgSeq(loginUserPrincipal.getLoginInfoSeq())
                                .build();
    loginService.insertTokenInfo(tokenDto);

    LoginHst userLoginHst = LoginHst.builder()
                                    .loginInfoSeq(loginUserPrincipal.getLoginInfoSeq())
                                    .loginIp(RequestContextHolder.getRequest().getRemoteAddr())
                                    .regSeq(loginUserPrincipal.getLoginInfoSeq())
                                    .chgSeq(loginUserPrincipal.getLoginInfoSeq())
                                    .build();

    loginService.insertLoginHst(userLoginHst);

    log.debug("Redirecting to DefaultSavedRequest Url: {}", targetUrl);
    RequestContextHolder.getRequest().getRequestDispatcher(targetUrl).forward(request, response);
  }
}
