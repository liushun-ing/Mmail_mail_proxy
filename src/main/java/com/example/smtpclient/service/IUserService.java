package com.example.smtpclient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpclient.entity.User;
import com.example.smtpclient.pojo.request.UserRequestBody;
import com.example.smtpclient.pojo.response.UserResponseBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IUserService extends IService<User> {

  UserResponseBody.JudgeAccountValidRes judgeAccountValid(String account);

  int registerUser(UserRequestBody.RegisterUserReq registerUser);

  boolean verifyPassword(UserRequestBody.VerifyPasswordReq verifyPasswordReq);

  int changePassword(UserRequestBody.ChangePasswordReq changePasswordReq);

  int updateUser(User user);

  int uploadAvatar(String userId, String avatar);

  UserResponseBody.GetUserInfoRes getUserInfo(String userId);
}
