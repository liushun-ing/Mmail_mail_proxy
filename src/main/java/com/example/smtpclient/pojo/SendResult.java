package com.example.smtpclient.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendResult {

  private boolean flag;
  private String msg;

}
