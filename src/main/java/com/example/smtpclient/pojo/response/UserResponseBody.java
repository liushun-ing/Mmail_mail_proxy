package com.example.smtpclient.pojo.response;

import com.example.smtpclient.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public class UserResponseBody {

  @Data
  @Builder
  public static class GetUserListRes {
    private List<User> userList;
    private long total;
  }

  @Data
  @Builder
  public static class JudgeAccountValidRes {
    private Boolean isValid;
    private String msg;
  }

  @Data
  @Builder
  public static class GetUserInfoRes {
    private String userId;
    private String account;
    private String avatar;
    private String phone;
    private String nickname;
  }

}
