package com.example.smtpclient.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MailHelper {

  private String password;
  private String fromEmail;
  private List<String> toEmail;
  private String subject;
  private String content;
  private SendResult sendResult;
}
