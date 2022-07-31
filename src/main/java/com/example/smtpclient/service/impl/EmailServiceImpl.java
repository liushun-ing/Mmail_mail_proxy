package com.example.smtpclient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpclient.email_package.getter.Getter;
import com.example.smtpclient.email_package.sender.Sender;
import com.example.smtpclient.entity.Email;
import com.example.smtpclient.entity.User;
import com.example.smtpclient.exception.UserNotFoundException;
import com.example.smtpclient.mapper.EmailMapper;
import com.example.smtpclient.mapper.ServerMapper;
import com.example.smtpclient.mapper.UserMapper;
import com.example.smtpclient.pojo.GetResult;
import com.example.smtpclient.pojo.MailDTO;
import com.example.smtpclient.pojo.SendResult;
import com.example.smtpclient.pojo.request.EmailRequestBody;
import com.example.smtpclient.pojo.response.EmailResponseBody;
import com.example.smtpclient.service.IEmailService;
import com.example.smtpclient.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 服务实现类
 *
 * @author ls
 * @since 2022-04-30
 */
@Service
@EnableScheduling
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements IEmailService {

  @Resource EmailMapper emailMapper;

  @Resource UserMapper userMapper;

  @Resource ServerMapper serverMapper;

  Logger logger = Logger.getLogger(EmailServiceImpl.class);

  /** 删除30天邮件的定时任务 */
  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public void deleteEmailSchedule() {
    logger.info("定时任务：自动清理删除达三十天的邮件");
    try {
      emailMapper.delete(
          new LambdaQueryWrapper<Email>()
              .eq(Email::getDeleted, 1)
              .lt(Email::getDeleteTime, Utils.get30Date()));
    } catch (Exception e) {
      // 清理失败就等下一次在清理
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
  }

  @Override
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public SendResult sendEmail(MailDTO mailDTO) {
    if (mailDTO.getToEmail() == null || mailDTO.getToEmail().size() == 0) {
      throw new RuntimeException("参数不合法");
    }
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, mailDTO.getFromEmail()));
    if (user == null) {
      throw new RuntimeException("发件人信息错误");
    }
    SendResult sendResult = null;
    try {
      for (String to : mailDTO.getToEmail()) {
        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, to));
        if (user1 == null) {
          throw new RuntimeException("收件人信息错误");
        }
        emailMapper.insert(
            Email.builder()
                .fromEmail(mailDTO.getFromEmail())
                .fromName(user.getNickname())
                .toName(user1.getNickname())
                .toEmail(to)
                .sendTime(new Date())
                .content(mailDTO.getContent())
                .subject(mailDTO.getSubject())
                .stared(0)
                .deleted(0)
                .seen(0)
                .draft(0)
                .isSend(1)
                .build());
      }
      sendResult = Sender.sendEmail(serverMapper.selectById(1).getSmtpPort(), mailDTO, user);
      if (!sendResult.isFlag()) {
        throw new RuntimeException(sendResult.getMsg());
      }
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw e;
    }
    return sendResult;
  }

  @Override
  @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
  public EmailResponseBody.GetEmailRes getEmail(EmailRequestBody.GetEmailReq getEmailReq) {
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, getEmailReq.getAccount()));
    if (user == null) {
      throw new UserNotFoundException();
    }

    try {
      if (getEmailReq.getCurrentPage() == 1) {
        GetResult getResult = Getter.getEmail(serverMapper.selectById(1).getPop3Port(), user);
        if (!getResult.isFlag()) {
          throw new RuntimeException("拉取新邮件失败");
        }
        for (Email email : getResult.getEmailList()) {
          User fromUser =
              userMapper.selectOne(
                  new LambdaQueryWrapper<User>().eq(User::getAccount, email.getFromEmail()));
          // 拿到的时候，如果用户删了，就直接忽略该邮件
          if (fromUser == null) {
            continue;
          }
          // 已有data,收件人邮箱，发件人邮箱，大小，时间，
          // 还要完善，两个名字，主体，内容，各个标志位
          email.setFromName(fromUser.getNickname());
          email.setToEmail(user.getAccount());
          email.setToName(user.getNickname());
          email.setDraft(0);
          email.setIsSend(0);
          email.setDeleted(0);
          email.setSeen(0);
          email.setStared(0);
          // 从data解析subject和内容
          String data = email.getData();
          String[] split = data.split("\r\n", 2);
          email.setSubject(split[0].substring(8));
          email.setContent(split[1]);
          emailMapper.insert(email);
        }
      }

      // 然后再查询
      Page<Email> pageHelper = new Page<>(getEmailReq.getCurrentPage(), getEmailReq.getPageSize());
      Page<Email> emailPage =
          emailMapper.selectPage(
              pageHelper,
              new LambdaQueryWrapper<Email>()
                  .eq(Email::getToEmail, getEmailReq.getAccount())
                  .eq(Email::getDeleted, 0)
                  .eq(Email::getIsSend, 0)
                  .eq(Email::getDraft, 0)
                  .and(
                      getEmailReq.getSearchKey() != null
                          && getEmailReq.getSearchKey().length() != 0,
                      x ->
                          x.like(Email::getFromEmail, getEmailReq.getSearchKey())
                              .or()
                              .like(Email::getFromName, getEmailReq.getSearchKey())
                              .or()
                              .like(Email::getSubject, getEmailReq.getSearchKey()))
                  .orderByDesc(Email::getSendTime));
      return EmailResponseBody.GetEmailRes.builder()
          .total(emailPage.getTotal())
          .emailList(emailPage.getRecords())
          .build();
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw e;
    }
  }

  @Override
  public int setSeen(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.updateById(Email.builder().emailId(emailId).seen(1).build());
  }

  @Override
  public int setStared(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.updateById(Email.builder().emailId(emailId).stared(1).build());
  }

  @Override
  public int clearStared(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.updateById(Email.builder().emailId(emailId).stared(0).build());
  }

  @Override
  public EmailResponseBody.GetStaredRes getStared(EmailRequestBody.GetStaredReq getStaredReq) {
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, getStaredReq.getAccount()));
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    Page<Email> pageHelper = new Page<>(getStaredReq.getCurrentPage(), getStaredReq.getPageSize());
    Page<Email> emailPage =
        emailMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<Email>()
                .eq(Email::getToEmail, getStaredReq.getAccount())
                .eq(Email::getStared, 1)
                .eq(Email::getDeleted, 0)
                .and(
                    getStaredReq.getSearchKey() != null
                        && getStaredReq.getSearchKey().length() != 0,
                    x ->
                        x.like(Email::getToEmail, getStaredReq.getSearchKey())
                            .or()
                            .like(Email::getContent, getStaredReq.getSearchKey())
                            .or()
                            .like(Email::getSubject, getStaredReq.getSearchKey())));
    return EmailResponseBody.GetStaredRes.builder()
        .total(emailPage.getTotal())
        .staredList(emailPage.getRecords())
        .build();
  }

  @Override
  public int deleteEmail(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.updateById(
        Email.builder().emailId(emailId).deleted(1).deleteTime(new Date()).build());
  }

  @Override
  public int clearDelete(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.updateById(Email.builder().emailId(emailId).deleted(0).build());
  }

  @Override
  public EmailResponseBody.GetDeletedRes getDeleted(EmailRequestBody.GetDeletedReq getDeletedReq) {
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, getDeletedReq.getAccount()));
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    Page<Email> pageHelper =
        new Page<>(getDeletedReq.getCurrentPage(), getDeletedReq.getPageSize());
    Page<Email> emailPage =
        emailMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<Email>()
                .eq(Email::getToEmail, getDeletedReq.getAccount())
                .eq(Email::getDeleted, 1)
                .or(
                    x ->
                        x.eq(Email::getFromEmail, getDeletedReq.getAccount())
                            .eq(Email::getIsSend, 1)
                            .eq(Email::getDeleted, 1))
                .and(
                    getDeletedReq.getSearchKey() != null
                        && getDeletedReq.getSearchKey().length() != 0,
                    x ->
                        x.like(Email::getToEmail, getDeletedReq.getSearchKey())
                            .or()
                            .like(Email::getContent, getDeletedReq.getSearchKey())
                            .or()
                            .like(Email::getSubject, getDeletedReq.getSearchKey())));
    return EmailResponseBody.GetDeletedRes.builder()
        .total(emailPage.getTotal())
        .deletedList(emailPage.getRecords())
        .build();
  }

  @Override
  public int deleteEmailFinally(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.deleteById(emailId);
  }

  @Override
  public int addDraft(Email email) {
    User from =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, email.getFromEmail()));
    if (from == null) {
      throw new RuntimeException("发件人不存在");
    }
    //    User to =
    //        userMapper.selectOne(
    //            new LambdaQueryWrapper<User>().eq(User::getAccount, email.getToEmail()));
    //    if (to == null) {
    //      throw new RuntimeException("收件人不存在");
    //    }
    return emailMapper.insert(
        Email.builder()
            .fromEmail(email.getFromEmail())
            .fromName(from.getNickname())
            .toEmail(email.getToEmail())
            //            .toName(to.getNickname())
            .subject(email.getSubject())
            .content(email.getContent())
            .draft(1)
            .isSend(1)
            .seen(0)
            .stared(0)
            .deleted(0)
            .sendTime(new Date())
            .build());
  }

  @Override
  public int clearDraft(String emailId) {
    Email email = emailMapper.selectById(emailId);
    if (email == null) {
      throw new RuntimeException("邮件不存在");
    }

    return emailMapper.deleteById(emailId);
  }

  @Override
  public EmailResponseBody.GetDraftRes getDraft(EmailRequestBody.GetDraftReq getDraftReq) {
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, getDraftReq.getAccount()));
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    Page<Email> pageHelper = new Page<>(getDraftReq.getCurrentPage(), getDraftReq.getPageSize());
    Page<Email> emailPage =
        emailMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<Email>()
                .eq(Email::getFromEmail, getDraftReq.getAccount())
                .eq(Email::getDraft, 1)
                .eq(Email::getDeleted, 0)
                .and(
                    getDraftReq.getSearchKey() != null && getDraftReq.getSearchKey().length() != 0,
                    x ->
                        x.like(Email::getToEmail, getDraftReq.getSearchKey())
                            .or()
                            .like(Email::getContent, getDraftReq.getSearchKey())
                            .or()
                            .like(Email::getSubject, getDraftReq.getSearchKey())));
    return EmailResponseBody.GetDraftRes.builder()
        .total(emailPage.getTotal())
        .draftList(emailPage.getRecords())
        .build();
  }

  @Override
  public int updateDraft(Email email) {
    return emailMapper.updateById(email);
  }

  @Override
  public EmailResponseBody.GetSendRes getSend(EmailRequestBody.GetSendReq getSendReq) {
    User user =
        userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getAccount, getSendReq.getAccount()));
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    Page<Email> pageHelper = new Page<>(getSendReq.getCurrentPage(), getSendReq.getPageSize());
    Page<Email> emailPage =
        emailMapper.selectPage(
            pageHelper,
            new LambdaQueryWrapper<Email>()
                .eq(Email::getFromEmail, getSendReq.getAccount())
                .eq(Email::getDeleted, 0)
                .eq(Email::getIsSend, 1)
                .and(
                    getSendReq.getSearchKey() != null && getSendReq.getSearchKey().length() != 0,
                    x ->
                        x.like(Email::getToEmail, getSendReq.getSearchKey())
                            .or()
                            .like(Email::getToName, getSendReq.getSearchKey())
                            .or()
                            .like(Email::getSubject, getSendReq.getSearchKey())));
    return EmailResponseBody.GetSendRes.builder()
        .total(emailPage.getTotal())
        .sendList(emailPage.getRecords())
        .build();
  }

  @Override
  public Email getEmailDetail(String emailId) {
    return emailMapper.selectById(emailId);
  }
}
