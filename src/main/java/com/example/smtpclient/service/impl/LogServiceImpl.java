package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.entity.Log;
import com.example.smtpclient.mapper.LogMapper;
import com.example.smtpclient.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
