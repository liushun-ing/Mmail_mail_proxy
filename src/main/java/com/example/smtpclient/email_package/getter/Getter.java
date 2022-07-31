package com.example.smtpclient.email_package.getter;

import com.example.smtpclient.entity.User;
import com.example.smtpclient.pojo.*;
import io.netty.channel.Channel;

import java.util.HashMap;

public class Getter {

  private static HashMap<String, GetterHelper> getMap = new HashMap<>();

  public static GetResult getEmail(int port, User user) {
    Channel channel = GetterClient.getConnection(port);
    String channelId = channel.id().asLongText();
    getMap.put(channelId, GetterHelper.builder()
        .user(user)
        .build());
    System.out.println("Getter: getMap加入成员：" + channelId + ":" + user + "size:" + getMap.size());
    while (channel.isOpen() || channel.isRegistered()) {
      // 等待邮件发送
    }
    GetResult getResult = null;
    if (getMap.containsKey(channelId)) {
      getResult = getMap.get(channelId).getGetResult();
      getMap.remove(channelId);
      System.out.println(channelId + "从getMap中移除");
    }
    return getResult;
  }

  public static GetterHelper getGetterHelper(String channelId) {
    System.out.println(getMap.size());
    if (getMap.containsKey(channelId)) {
      return getMap.get(channelId);
    } else {
      System.out.println("getterHelper不存在");
      return null;
    }

  }

}
