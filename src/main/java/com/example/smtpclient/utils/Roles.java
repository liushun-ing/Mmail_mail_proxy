package com.example.smtpclient.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

  // 管理员
  Admin(0, "admin"),
  // 用户
  User(1, "user");

  private int authority;
  private String role;
}
