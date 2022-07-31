package com.example.smtpclient.pojo;

import com.example.smtpclient.entity.Email;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetResult {

  private boolean flag;
  private String msg;
  List<Email> emailList;
}
