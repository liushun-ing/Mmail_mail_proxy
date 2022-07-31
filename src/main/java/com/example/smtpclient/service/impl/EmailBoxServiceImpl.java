package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.entity.EmailBox;
import com.example.smtpclient.mapper.EmailBoxMapper;
import com.example.smtpclient.service.IEmailBoxService;
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
public class EmailBoxServiceImpl extends ServiceImpl<EmailBoxMapper, EmailBox> implements IEmailBoxService {

}
