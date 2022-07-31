package com.example.smtpclient.controller;


import com.example.smtpclient.entity.User;
import com.example.smtpclient.exception.UserNotFoundException;
import com.example.smtpclient.pojo.request.UserRequestBody;
import com.example.smtpclient.pojo.response.UserResponseBody;
import com.example.smtpclient.service.IUserService;
import com.example.smtpclient.utils.Result;
import com.example.smtpclient.utils.ResultEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


  @Resource
  IUserService userService;

  Logger logger = Logger.getLogger(UserController.class);

  /** 判断用户名是否合法 */
  @RequestMapping("/judgeAccountValid")
  public Result<?> judgeAccountValid(String account) {
    logger.info("judgeAccountValid");
    try {
      UserResponseBody.JudgeAccountValidRes judgeAccountValidRes =
          userService.judgeAccountValid(account);
      HashMap<String, Object> res = new HashMap<>();
      res.put("isValid", judgeAccountValidRes.getIsValid());
      res.put("msg", judgeAccountValidRes.getMsg());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  @RequestMapping("/registerUser")
  public Result<Void> registerUser(UserRequestBody.RegisterUserReq registerUser) {
    logger.info("/registerUser");
    try {
      int i = userService.registerUser(registerUser);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  /** 验证原密码是否正确 */
  @RequestMapping("/verifyPassword")
  public Result<Void> deleteUser(UserRequestBody.VerifyPasswordReq verifyPasswordReq) {
    logger.info("/deleteUser");
    try {
      boolean i = userService.verifyPassword(verifyPasswordReq);
      if (i) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      return Result.error();
    }
  }

  /** 修改密码 */
  @RequestMapping("/changePassword")
  public Result<Void> deleteUser(UserRequestBody.ChangePasswordReq changePasswordReq) {
    logger.info("/changePassword");
    try {
      int i = userService.changePassword(changePasswordReq);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      if (e.getMessage()== null || e.getMessage().length() == 0) {
        return Result.error();
      } else {
        return Result.error(e.getMessage());
      }
    }
  }

  @RequestMapping("/updateUser")
  public Result<Void> updateUser(User user) {
    logger.info("updateUser");
    try {
      int i = userService.updateUser(user);
      if (i == 1) {
        return Result.success();
      } else  {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  @RequestMapping("/uploadAvatar")
  public Result<Void> uploadAvatar(String userId, String avatar) {
    logger.info("uploadAvatar");
    try {
      int i = userService.uploadAvatar(userId, avatar);
      if (i == 1) {
        return Result.success();
      } else  {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  @RequestMapping("/getUserInfo")
  public Result<?> getUserIfo(String userId) {
    logger.info("/getUserInfo");
    try{
      UserResponseBody.GetUserInfoRes userInfo = userService.getUserInfo(userId);
      return Result.success(userInfo);
    } catch (UserNotFoundException e) {
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (Exception e) {
      return Result.error();
    }

  }
}

