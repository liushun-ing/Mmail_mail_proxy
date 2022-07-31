package com.example.smtpclient.service;


import com.example.smtpclient.pojo.request.LoginRequestBody;
import com.example.smtpclient.pojo.response.LoginResponseBody;

public interface LoginService {
  LoginResponseBody login(LoginRequestBody loginRequestBody);
}
