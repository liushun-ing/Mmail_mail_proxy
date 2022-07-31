package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpclient.entity.User;
import com.example.smtpclient.exception.PasswordErrorException;
import com.example.smtpclient.exception.SignTokenException;
import com.example.smtpclient.exception.UserInvalidException;
import com.example.smtpclient.exception.UserNotFoundException;
import com.example.smtpclient.mapper.UserMapper;
import com.example.smtpclient.pojo.request.LoginRequestBody;
import com.example.smtpclient.pojo.response.LoginResponseBody;
import com.example.smtpclient.service.LoginService;
import com.example.smtpclient.utils.JWTUtil;
import com.example.smtpclient.utils.PasswordMD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

  @Resource
  private UserMapper userMapper;

  @Resource
  private JWTUtil JWTUtil;

  @Override
  public LoginResponseBody login(LoginRequestBody loginRequestBody) {

    // 查询用户
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, loginRequestBody.getAccount()));
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (user.getIsDisabled() == 1) {
      throw new UserInvalidException();
    }

    // 需要加密后进行判断
    if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(loginRequestBody.getPassword()))) {
      throw new PasswordErrorException();
    }

    String token;
    try {
      // 生成token
      token = this.JWTUtil.sign(user.getAccount());
    } catch (Exception e) {
      e.printStackTrace();
      throw new SignTokenException();
    }

    return LoginResponseBody.builder()
        .userId(user.getUserId())
        .account(user.getAccount())
        .nickname(user.getNickname())
        .authority(user.getAuthority())
        .type(user.getType())
        .token(token)
        .build();
  }
}
