package com.example.smtpclient.pojo.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class LoginRequestBody {
  @NotEmpty
  private String account;
  @NotEmpty
  private String password;
}
