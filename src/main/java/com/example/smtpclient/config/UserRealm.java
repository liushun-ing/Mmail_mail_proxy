package com.example.smtpclient.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smtpclient.entity.User;
import com.example.smtpclient.mapper.UserMapper;
import com.example.smtpclient.utils.JWTToken;
import com.example.smtpclient.utils.JWTUtil;
import com.example.smtpclient.utils.Roles;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class UserRealm extends AuthorizingRealm {

  @Resource private UserMapper userMapper;

  Logger logger = Logger.getLogger(UserRealm.class);

  /** 重写此方法，不然Shiro会报错 */
  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JWTToken;
  }

  // 授权
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String account = JWTUtil.getAccount(principals.toString());
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    switch (user.getAuthority()) {
      case 0:
        simpleAuthorizationInfo.addRole(Roles.Admin.getRole());
        break;
      case 1:
        simpleAuthorizationInfo.addRole(Roles.User.getRole());
      default:
        break;
    }
    return simpleAuthorizationInfo;
  }

  // 验证
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
      throws AuthenticationException {
    String token = (String) auth.getCredentials();
    // 解密获得account，用于和数据库进行对比
    String account = JWTUtil.getAccount(token);
    if (account == null) {
      throw new AuthenticationException("token invalid");
    }

    User admin = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    if (admin == null) {
      throw new AuthenticationException("User didn't existed!");
    }

    //    if (! Token.verify(token, username, userBean.getPassword())) {
    //      throw new AuthenticationException("Username or password error");
    //    }

    return new SimpleAuthenticationInfo(token, token, "userRealm");
  }
}
