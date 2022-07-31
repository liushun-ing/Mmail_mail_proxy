package com.example.smtpclient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpclient.entity.Server;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IServerService extends IService<Server> {

  Server getServerParams();
}
