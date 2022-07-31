package com.example.smtpclient.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
  SUCCESS(2000, "请求成功"),
  DEFAULT_ERROR(5000, "请求失败"),
  ARGUMENT_ERROR(10002, "参数错误"),
  SIGN_TOKEN_ERROR(10003, "生成签名失败"),
  PASSWORD_ERROR(20001, "密码错误"),
  USER_NOT_FOUND(20002, "用户未找到"),
  AUTH_ERROR(20003, "权限不满足"),
  PARSE_TOKEN_ERROR(20004, "Token 解析失败"),
  JSON_PARSE_ERROR(20005, "json解析失败"),
  USER_EXISTED_ERROR(20009, "用户已存在，请检查用户名"),
  PASSWORD_INCONSISTENT(20006, "两次密码不一致");


  private int code;
  private String msg;
}
