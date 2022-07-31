package com.example.smtpclient.email_package.sender;

import com.example.smtpclient.entity.User;
import com.example.smtpclient.pojo.MailDTO;
import com.example.smtpclient.pojo.MailHelper;
import com.example.smtpclient.pojo.SendResult;
import com.example.smtpclient.utils.PasswordMD5;
import com.example.smtpclient.utils.Utils;
import io.netty.channel.Channel;

import java.util.HashMap;

public class Sender {

  private static HashMap<String, MailHelper> mailMap = new HashMap<>();

  public static SendResult sendEmail(int port, MailDTO mailDTO, User user) {
    Channel channel = SenderClient.getConnection(port);
    String channelId = channel.id().asLongText();
    mailMap.put(channelId, MailHelper.builder()
        .fromEmail(mailDTO.getFromEmail())
        .toEmail(mailDTO.getToEmail())
        .subject(mailDTO.getSubject())
        .content(mailDTO.getContent())
        .password(user.getPassword()).build());
    System.out.println(mailMap);
    while (channel.isOpen() || channel.isRegistered()) {
      // 等待邮件发送
    }
    SendResult sendResult = null;
    if (mailMap.containsKey(channelId)) {
      sendResult = mailMap.get(channelId).getSendResult();
      mailMap.remove(channelId);
    }
    return sendResult;
  }

  public static MailHelper getMailHelper(String channelId) {
    if (mailMap.containsKey(channelId)) {
      return mailMap.get(channelId);
    } else {
      System.out.println("mailHelper不存在");
      return null;
    }
  }

}
