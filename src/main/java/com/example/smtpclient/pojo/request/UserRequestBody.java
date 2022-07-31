package com.example.smtpclient.pojo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestBody {

  @Data
  @Builder
  public static class GetUserListReq {
    private long pageSize;
    private long currentPage;
  }

  @Data
  @Builder
  public static class VerifyPasswordReq {
    private String userId;
    private String password;
  }

  @Data
  @Builder
  public static class ChangePasswordReq {
    private String userId;
    private String newPassword;
  }

  @Data
  @Builder
  public static class RegisterUserReq {
    private String account;
    private String password;
  }


}
