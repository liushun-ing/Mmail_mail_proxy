package com.example.smtpclient.config;

import com.example.smtpclient.utils.JWTFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

  @Resource
  private UserRealm userRealm;

  @Bean("securityManager")
  public DefaultWebSecurityManager getManager(UserRealm realm) {
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    // 使用自己的realm
    manager.setRealm(realm);

    /*
     * 关闭shiro自带的session，详情见文档
     */
    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
    DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
    defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
    subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
    manager.setSubjectDAO(subjectDAO);

    return manager;
  }

  @Bean("shiroFilter")
  public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

    // 添加自己的过滤器并且取名为jwt
    Map<String, Filter> filterMap = new HashMap<>();
    filterMap.put("jwt", new JWTFilter());
    factoryBean.setFilters(filterMap);

    factoryBean.setSecurityManager(securityManager);
    factoryBean.setUnauthorizedUrl("/401");

    /*
     * 自定义url规则
     * http://shiro.apache.org/web.html#urls-
     */
    Map<String, String> filterRuleMap = new HashMap<>();
    // 所有请求通过我们自己的JWT Filter
    filterRuleMap.put("/**", "jwt");
    // 访问401和404页面不通过我们的Filter
    filterRuleMap.put("/401", "anon");
    filterRuleMap.put("/login", "anon");
    // 代码map.put("/**", "authc")使得所有请求都需要认证才能请求成功，否则就跳转至setLoginUrl中指定的页面，
    // 同时为了使登录页面可以在未登陆时请求成功，需要用代码map.put("/login","anon")进行设置
    factoryBean.setFilterChainDefinitionMap(filterRuleMap);

//    factoryBean.setLoginUrl("/login");
    return factoryBean;
  }

  /**
   * 添加注解支持
   */
  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }

  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }

  @Bean
  public UserRealm adminRealm() {
    return new UserRealm();
  }

}
