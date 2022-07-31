package com.example.smtpclient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpclient.entity.Email;
import com.example.smtpclient.pojo.GetResult;
import com.example.smtpclient.pojo.MailDTO;
import com.example.smtpclient.pojo.SendResult;
import com.example.smtpclient.pojo.request.EmailRequestBody;
import com.example.smtpclient.pojo.response.EmailResponseBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
public interface IEmailService extends IService<Email> {

  SendResult sendEmail(MailDTO mailDTO);

  EmailResponseBody.GetEmailRes getEmail(EmailRequestBody.GetEmailReq getEmailReq);

  int setSeen(String emailId);

  int setStared(String emailId);

  int clearStared(String emailId);

  EmailResponseBody.GetStaredRes getStared(EmailRequestBody.GetStaredReq getStaredReq);

  int deleteEmail(String emailId);

  int clearDelete(String emailId);

  EmailResponseBody.GetDeletedRes getDeleted(EmailRequestBody.GetDeletedReq getDeletedReq);

  int deleteEmailFinally(String emailId);

  int addDraft(Email email);

  int clearDraft(String emailId);

  EmailResponseBody.GetDraftRes getDraft(EmailRequestBody.GetDraftReq getDraftReq);

  int updateDraft(Email email);

  EmailResponseBody.GetSendRes getSend(EmailRequestBody.GetSendReq getSendReq);

  Email getEmailDetail(String emailId);
}
