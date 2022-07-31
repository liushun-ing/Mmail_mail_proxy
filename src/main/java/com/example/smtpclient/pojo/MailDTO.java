package com.example.smtpclient.pojo;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MailDTO {

  private String fromEmail;
  private List<String> toEmail;
  private String subject;
  private String content;
}
