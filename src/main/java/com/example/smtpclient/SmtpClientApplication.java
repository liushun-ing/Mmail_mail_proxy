package com.example.smtpclient;

import com.example.smtpclient.email_package.getter.GetterClient;
import com.example.smtpclient.email_package.sender.SenderClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.smtpclient.mapper")
public class SmtpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmtpClientApplication.class, args);

    new Thread(GetterClient::startGetterClient).start();
    new Thread(SenderClient::startSenderClient).start();
  }

}
