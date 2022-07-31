package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.entity.Server;
import com.example.smtpclient.mapper.ServerMapper;
import com.example.smtpclient.service.IServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements IServerService {

  @Resource
  ServerMapper serverMapper;

  @Override
  public Server getServerParams() {
    return serverMapper.selectById(1);
  }
}
