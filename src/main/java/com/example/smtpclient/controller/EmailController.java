package com.example.smtpclient.controller;


import com.example.smtpclient.entity.Email;
import com.example.smtpclient.pojo.MailDTO;
import com.example.smtpclient.pojo.SendResult;
import com.example.smtpclient.pojo.request.EmailRequestBody;
import com.example.smtpclient.pojo.response.EmailResponseBody;
import com.example.smtpclient.service.IEmailService;
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
@RequestMapping("/email")
public class EmailController {

  @Resource
  IEmailService emailService;

  Logger logger = Logger.getLogger(EmailController.class);

  @RequestMapping("/sendEmail")
  public Result<Void> sendEmail(MailDTO mailDTO) {
    logger.info("sendEmail");
    try{
      SendResult sendResult = emailService.sendEmail(mailDTO);
      if (sendResult.isFlag()) {
        return Result.success();
      } else {
        return Result.error(sendResult.getMsg());
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/getEmail")
  public Result<?> getEmail(EmailRequestBody.GetEmailReq getEmailReq) {
    logger.info("getEmail");
    try {
      EmailResponseBody.GetEmailRes email = emailService.getEmail(getEmailReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", email.getTotal());
      res.put("emailList", email.getEmailList());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/setSeen")
  public Result<Void> setSeen(String emailId) {
    logger.info("setSeen");
    try {
      int i = emailService.setSeen(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/setStared")
  public Result<Void> setStared(String emailId) {
    logger.info("setStared");
    try {
      int i = emailService.setStared(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/clearStared")
  public Result<Void> clearStared(String emailId) {
    logger.info("clearStared");
    try {
      int i = emailService.clearStared(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/getStared")
  public Result<?> getStared(EmailRequestBody.GetStaredReq getStaredReq) {
    logger.info("getStared");
    try {
      EmailResponseBody.GetStaredRes email = emailService.getStared(getStaredReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", email.getTotal());
      res.put("staredList", email.getStaredList());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/deleteEmail")
  public Result<Void> deleteEmail(String emailId) {
    logger.info("deleteEmail");
    try {
      int i = emailService.deleteEmail(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/clearDelete")
  public Result<Void> clearDelete(String emailId) {
    logger.info("clearDelete");
    try {
      int i = emailService.clearDelete(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/getDeleted")
  public Result<?> getDeleted(EmailRequestBody.GetDeletedReq getDeletedReq) {
    logger.info("getDeleted");
    try {
      EmailResponseBody.GetDeletedRes email = emailService.getDeleted(getDeletedReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", email.getTotal());
      res.put("deletedList", email.getDeletedList());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/addDraft")
  public Result<Void> addDraft(Email email) {
    logger.info("addDraft");
    try {
      int i = emailService.addDraft(email);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/clearDraft")
  public Result<Void> clearDraft(String emailId) {
    logger.info("clearDraft");
    try {
      int i = emailService.clearDraft(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/deleteEmailFinally")
  public Result<Void> deleteEmailFinally(String emailId) {
    logger.info("deleteEmailFinally");
    try {
      int i = emailService.deleteEmailFinally(emailId);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/getDraft")
  public Result<?> getDraft(EmailRequestBody.GetDraftReq getDraftReq) {
    logger.info("getDraft");
    try {
      EmailResponseBody.GetDraftRes email = emailService.getDraft(getDraftReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", email.getTotal());
      res.put("draftList", email.getDraftList());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/updateDraft")
  public Result<Void> updateDraft(Email email) {
    logger.info("/updateDraft");
    try {
      int i = emailService.updateDraft(email);
      if (i == 1) {
        return Result.success();
      } else {
        return Result.error();
      }
    } catch (Exception e) {
      return Result.error();
    }
  }

  @RequestMapping("/getSend")
  public Result<?> getSend(EmailRequestBody.GetSendReq getSendReq) {
    logger.info("getSend");
    try {
      EmailResponseBody.GetSendRes email = emailService.getSend(getSendReq);
      HashMap<String, Object> res = new HashMap<>();
      res.put("total", email.getTotal());
      res.put("sendList", email.getSendList());
      return Result.success(res);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }

  @RequestMapping("/getEmailDetail")
  public Result<?> getEmailDetail(String emailId) {
    logger.info("getEmailDetail");
    try {
      Email email = emailService.getEmailDetail(emailId);
      if (email == null) {
        return Result.error("邮件不存在");
      } else {
        HashMap<String, Object> res = new HashMap<>();
        res.put("emailDetail", email);
        return Result.success(res);
      }
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().length() != 0) {
        return Result.error(e.getMessage());
      } else {
        return Result.error();
      }
    }
  }



}

