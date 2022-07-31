package com.example.smtpclient.email_package.sender;

import com.example.smtpclient.pojo.MailHelper;
import com.example.smtpclient.pojo.SendResult;
import com.example.smtpclient.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class SenderClientHandler extends SimpleChannelInboundHandler<String> {

  private MailHelper mailHelper;
  private int commandIndex;
  // 日志
  private Logger logger = Logger.getLogger(SenderClientHandler.class);

  /** 新连接建立 */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    // 打印日志
    logger.info(Utils.getCurrentTime() + ": " + channel.id().asLongText() + "与服务器建立连接");
    mailHelper = Sender.getMailHelper(channel.id().asLongText());
    commandIndex = 0;
  }

  /** 读取数据，并进行处理 */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
      throws Exception {
    Channel channel = channelHandlerContext.channel();
    // 处理逻辑
    if (commandIndex == 0) {
      if (s.startsWith("220")) {
        commandIndex++;
        channel.writeAndFlush("ehlo localhost\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 1) {
      if (s.startsWith("250")) {
        commandIndex++;
        channel.writeAndFlush("auth login\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 2) {
      if (s.startsWith("334")) {
        commandIndex++;
        channel.writeAndFlush(Utils.getBase64FromUtf8(mailHelper.getFromEmail()) + "\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 3) {
      if (s.startsWith("334")) {
        commandIndex++;
        channel.writeAndFlush(Utils.getBase64FromUtf8(mailHelper.getPassword()) + "\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 4) {
      if (s.startsWith("235")) {
        commandIndex++;
        channel.writeAndFlush("mail from:<" + mailHelper.getFromEmail() + ">\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 5) {
      if (s.startsWith("250")) {
        if (mailHelper.getToEmail().size() > 1) {
          channel.writeAndFlush("rcpt to:<" + mailHelper.getToEmail().get(0) + ">\r\n");
          mailHelper.getToEmail().remove(0);
        } else if (mailHelper.getToEmail().size() == 1) {
          commandIndex++;
          channel.writeAndFlush("rcpt to:<" + mailHelper.getToEmail().get(0) + ">\r\n");
        } else {
          mailHelper.setSendResult(SendResult.builder().flag(false).msg("收件人处理错误").build());
        }
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 6) {
      if (s.startsWith("250")) {
        commandIndex++;
        channel.writeAndFlush("data\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 7) {
      if (s.startsWith("354")) {
        commandIndex++;
        channel.writeAndFlush(
            "Subject:" + mailHelper.getSubject() + "\r\n" + mailHelper.getContent() + "\r\n" + ".\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 8) {
      if (s.startsWith("250")) {
        commandIndex++;
        channel.writeAndFlush("quit\r\n");
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 9) {
      if (s.startsWith("221")) {
        // 发送成功
        mailHelper.setSendResult(SendResult.builder().flag(true).msg(s).build());
        channel.close();
      } else {
        mailHelper.setSendResult(SendResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    }
  }

  /** 连接断开 */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    // 打印日志
    logger.info(Utils.getCurrentTime() + ": " + channel.id() + "断开连接");
  }

  /** 出现异常 */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Channel channel = ctx.channel();
    channel.close();
    logger.debug("连接出现异常：" + cause.getMessage());
  }
}
