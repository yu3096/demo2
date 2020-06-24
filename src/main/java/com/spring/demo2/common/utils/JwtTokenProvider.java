package com.spring.demo2.common.utils;

import com.spring.demo2.common.login.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private String secretKey = Base64.getEncoder().encodeToString("ysm-jwt-secretkey".getBytes());
  private int tokenValidSec = 30;
  private LoginService loginService;

  //JWT Token 생성
  public String generatorToken(String userId, List<String> roles){
    Claims claims = Jwts.claims()
                        .setSubject(userId);
    claims.put("roles", roles);

    Date now = new Date();

    return Jwts.builder()
               .setClaims(claims)
               .setIssuedAt(now)
               .setExpiration(new Date(now.getTime() + tokenValidSec * 60))
               .signWith(SignatureAlgorithm.HS512, secretKey)
               .compact();
  }

  //JWT Token으로 인증정보 조회
  public Authentication getAuthentication(String token){
    UserDetails userDetails = loginService.loadUserByUsername(this.getTokenSubject(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  //JWT Token에서 값 추출
  public String getTokenSubject(String token){
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request){
    return request.getHeader("X-AUTH-TOKEN");
  }

  //Token이 유효한지 확인
  public boolean validateToken(String token){
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    }
    catch(Exception e){
      e.printStackTrace();
      return false;
    }
  }
}
