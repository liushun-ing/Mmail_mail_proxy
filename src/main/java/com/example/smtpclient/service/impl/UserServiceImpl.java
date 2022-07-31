package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.entity.User;
import com.example.smtpclient.exception.UserNotFoundException;
import com.example.smtpclient.mapper.ServerMapper;
import com.example.smtpclient.mapper.UserMapper;
import com.example.smtpclient.pojo.request.UserRequestBody;
import com.example.smtpclient.pojo.response.UserResponseBody;
import com.example.smtpclient.service.IUserService;
import com.example.smtpclient.utils.PasswordMD5;
import com.example.smtpclient.utils.Utils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Resource UserMapper userMapper;

  @Resource ServerMapper serverMapper;

  /** 判断account是否合法 */
  public UserResponseBody.JudgeAccountValidRes judgeAccountValid(String account) {
    if (account == null || "".equals(account)) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名为空").build();
    }
    if (!account.endsWith("@" + serverMapper.selectById(1).getDomainName())) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名格式错误").build();
    }
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    if (user != null) {
      return UserResponseBody.JudgeAccountValidRes.builder().isValid(false).msg("用户名已存在").build();
    }
    return UserResponseBody.JudgeAccountValidRes.builder().isValid(true).msg("合法").build();
  }

  /** 注册用户 */
  @Override
  public int registerUser(UserRequestBody.RegisterUserReq registerUser) {
    if (registerUser == null
        || "".equals(registerUser.getAccount())
        || "".equals(registerUser.getPassword())) {
      throw new RuntimeException("参数有误");
    }
    return userMapper.insert(
        User.builder()
            .account(registerUser.getAccount())
            .password(PasswordMD5.getPasswordMD5(registerUser.getPassword()))
            .type(0)
            .registerTime(new Date())
            .authority(0)
            .isDisabled(0)
            .build());
  }

  /** 验证原密码是否正确 */
  public boolean verifyPassword(UserRequestBody.VerifyPasswordReq verifyPasswordReq) {
    User user = userMapper.selectById(verifyPasswordReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(verifyPasswordReq.getPassword()))) {
      return false;
    }
    return true;
  }

  /** 修改密码 */
  public int changePassword(UserRequestBody.ChangePasswordReq changePasswordReq) {
    User user = userMapper.selectById(changePasswordReq.getUserId());
    if (user == null) {
      throw new UserNotFoundException();
    }

    if (changePasswordReq.getNewPassword() == null
        || "".equals(changePasswordReq.getNewPassword())) {
      throw new RuntimeException("参数错误");
    }

    return userMapper.updateById(
        User.builder()
            .userId(changePasswordReq.getUserId())
            .password(PasswordMD5.getPasswordMD5(changePasswordReq.getNewPassword()))
            .build());
  }

  @Override
  public int updateUser(User user) {
    return userMapper.updateById(user);
  }

  @Override
  public int uploadAvatar(String userId, String avatar) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new UserNotFoundException();
    }
    return userMapper.updateById(User.builder().userId(userId).avatar(avatar).build());
  }

  @Override
  public UserResponseBody.GetUserInfoRes getUserInfo(String userId) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new UserNotFoundException();
    }

    return UserResponseBody.GetUserInfoRes.builder()
        .userId(user.getUserId())
        .account(user.getAccount())
        .avatar(user.getAvatar())
        .nickname(user.getNickname())
        .phone(user.getPhone())
        .build();
  }
}
