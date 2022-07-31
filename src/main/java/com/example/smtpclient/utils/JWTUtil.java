package com.example.smtpclient.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.smtpclient.config.TokenConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
  @Resource
  private TokenConfig tokenConfig;

  public String sign(String account) {
    // 过期时间
    Date date = new Date(System.currentTimeMillis() + tokenConfig.getExpireDate());
    // 密钥及加密算法
    Algorithm algorithm = Algorithm.HMAC256(tokenConfig.getSecret());
    // 设置 header 信息
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256");

    // 携带 username, password 信息，生成签名
    return JWT.create()
            .withHeader(header)
            .withClaim("account", account)
            .withExpiresAt(date)
            .sign(algorithm);
  }

  public Integer verify(String token) {
    Algorithm algorithm = Algorithm.HMAC256(tokenConfig.getSecret());
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT jwt = verifier.verify(token);
    Claim claim = jwt.getClaim("id");
    return claim.asInt();
  }

  public static String getAccount(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getClaim("account").asString();
    } catch (JWTDecodeException e) {
      return null;
    }
  }

}
