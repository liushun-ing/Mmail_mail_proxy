package com.example.smtpclient.controller;


import com.example.smtpclient.entity.Server;
import com.example.smtpclient.service.IServerService;
import com.example.smtpclient.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/server")
public class ServerController {
  @Resource
  IServerService serverService;

  Logger logger = Logger.getLogger(ServerController.class);

  /** 获取服务器配置 */
  @RequestMapping("/getServerParams")
  public Result<?> getServerParams() {
    logger.info("getServerParams");
    try {
      Server serverParams = serverService.getServerParams();
      HashMap<String, Object> res = new HashMap<>();
      res.put("serverParams", serverParams);
      return Result.success(res);
    } catch (Exception e) {
      return Result.error();
    }
  }
}

