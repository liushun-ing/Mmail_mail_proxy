package com.example.smtpclient.pojo;

import com.example.smtpclient.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetterHelper {

  private User user;
  private GetResult getResult;
}
