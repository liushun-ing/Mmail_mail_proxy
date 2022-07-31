package com.example.smtpclient.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTToken implements AuthenticationToken {
  private String token;


  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}
