package com.spring.demo2.common.login.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
  private final Gson gson;
  private final RestTemplate restTemplate;

  @PostMapping("/oauth2/callback")
  public String callbackSocial(@RequestParam String code){
    String credentials = "testClientId:testSecret";
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("Authorization", "Basic " + encodedCredentials);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("grant_type", "authorization_code");
    params.add("redirect_uri", "http://localhost:8080/oauth2/callback");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      return gson.fromJson(response.getBody(), String.class);
    }
    return null;
  }
}

