package com.spring.demo2.common.utils;

import com.spring.demo2.common.login.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private Header header;
  @Value("server.name")
  private String issuer;
  private String secretKey = Base64.getEncoder().encodeToString("134675y-hjbj1j3ner12j3enl1k3jn98-rut89apg".getBytes());
  private int accessTokenValidSec = 30 * 60;  //30분
  private int refreshTokenValidSec = 60 * 60 * 24; //1일
  private LoginService loginService;

  @PostConstruct
  public void init(){
     header = Jwts.header().setType(Header.JWT_TYPE);
  }

  /**
   * Access Token 생성
   * @param userId
   * @param roles
   * @return
   */
  public String generatorAccessToken(String userId, Collection<? extends GrantedAuthority> roles){
    Claims claims = Jwts.claims()
                        .setSubject(userId);

    claims.put("roles", roles);

    Date now = new Date();

    return Jwts.builder()
               .setHeader((Map<String, Object>) header)
               .setClaims(claims)
               .setIssuedAt(now)
               .setIssuer(this.issuer)
               .setExpiration(new Date(now.getTime() + accessTokenValidSec * 1000))
               .signWith(SignatureAlgorithm.HS512, secretKey)
               .compact();
  }

  public String generatorRefreshToken(String accessToken){
    String subject = this.getTokenSubject(accessToken);
    Claims claims = Jwts.claims()
                        .setSubject(subject);
    Date now = new Date();
    return Jwts.builder()
               .setHeader((Map<String, Object>) header)
               .setClaims(claims)
               .setIssuedAt(now)
               .setIssuer(this.issuer)
               .setExpiration(new Date(now.getTime() + refreshTokenValidSec * 1000))
               .signWith(SignatureAlgorithm.HS512, secretKey)
               .compact();
  }

  /**
   * Token Subejct 조회
   * @param token
   * @return
   */
  public String getTokenSubject(String token){
    return this.getTokenClaims(token)
               .getSubject();
  }

  public Object getTokenObject(String token, String key){
    return this.getTokenClaims(token)
               .get(key);
  }

  public Claims getTokenClaims(String token){
    return Jwts.parser()
               .setSigningKey(secretKey)
               .parseClaimsJws(token)
               .getBody();
  }

  public String resolveToken(HttpServletRequest request){
    return request.getHeader("Authorization");
  }
  public Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
    String token = this.resolveToken(request).replace("Bearer ", "");

    if(token != null){
      String username = this.getTokenSubject(token);
      ArrayList<LinkedHashMap<String, String>> roles = (ArrayList<LinkedHashMap<String, String>>) this.getTokenObject(token, "roles");

      if(username != null){
        Set<GrantedAuthority> listAuthorities = new HashSet<>();

        roles.forEach(k -> {
          listAuthorities.add(new SimpleGrantedAuthority(k.get("authority")));
        });

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, listAuthorities);
        return auth;
      }

      return null;
    }
    return null;
  }
}
