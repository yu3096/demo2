package com.spring.demo2.config.spring.loginConfig;

import com.spring.demo2.common.login.dto.LoginUserPrincipal;
import com.spring.demo2.common.login.dto.LoginHst;
import com.spring.demo2.common.login.service.LoginService;
import com.spring.demo2.common.utils.RequestContextHolder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider implements
    org.springframework.security.authentication.AuthenticationProvider {

  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private LoginService loginService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (null == authentication) {
      throw new InternalAuthenticationServiceException("Authentication is null");
    }

    if (null == authentication.getCredentials()) {
      throw new AuthenticationCredentialsNotFoundException("Credentials is null");
    }
    String userId = authentication.getName();
    String userPw = authentication.getCredentials().toString();
    LoginUserPrincipal loadedUser = loginService.loadUserByUsername(userId);
    List<String> authorities = loginService.selectRoleByLoginInfoSeq(loadedUser.getLoginInfoSeq());

    if (null == loadedUser) {
      throw new InternalAuthenticationServiceException(
          "UserDetailsService returned null, which is an interface contract violation");
    }

    if (!loadedUser.isAccountNonLocked()) {
      throw new LockedException("User account is locked");
    }
    if (!loadedUser.isAccountNonExpired()) {
      throw new AccountExpiredException("User account has expired");
    }
    if (!loadedUser.isEnabled()) {
      throw new DisabledException("User is disabled");
    }

    if (!passwordEncoder.matches(userPw, loadedUser.getPassword())) {
      throw new BadCredentialsException("Password does not match stored value");
    }

    if (!loadedUser.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException("User credentials have expired");
    }

    Set<GrantedAuthority> listAuthorities = new HashSet<>();

    for( String auth : authorities ){
      listAuthorities.add(new SimpleGrantedAuthority(auth));
    }

    UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser,
        userPw, listAuthorities);
    result.setDetails(authentication.getDetails());

    return result;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
