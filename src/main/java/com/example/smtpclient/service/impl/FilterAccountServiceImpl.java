package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.entity.FilterAccount;
import com.example.smtpclient.mapper.FilterAccountMapper;
import com.example.smtpclient.service.IFilterAccountService;
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
public class FilterAccountServiceImpl extends ServiceImpl<FilterAccountMapper, FilterAccount> implements IFilterAccountService {

}
