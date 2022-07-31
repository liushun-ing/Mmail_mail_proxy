package com.example.smtpclient.email_package.getter;

import com.example.smtpclient.entity.Email;
import com.example.smtpclient.pojo.GetResult;
import com.example.smtpclient.pojo.GetterHelper;
import com.example.smtpclient.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetterClientHandler extends SimpleChannelInboundHandler<String> {

  private GetterHelper getterHelper;
  private int commandIndex;
  private List<Email> emailList;
  private int count;
  private int index;
  // 日志
  private Logger logger = Logger.getLogger(GetterClientHandler.class);

  /** 新连接建立 */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    // 打印日志
    logger.info(Utils.getCurrentTime() + ": " + channel.id().asLongText() + "与服务器建立连接");
    getterHelper = Getter.getGetterHelper(channel.id().asLongText());
//    logger.debug(getterHelper.toString());
    commandIndex = 0;
    count = 0;
    index = 0;
    emailList = new ArrayList<Email>();
  }

  /** 读取数据，并进行处理 */
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
      throws Exception {
    Channel channel = channelHandlerContext.channel();
    logger.debug(channel.id().asShortText() + "command:" + commandIndex + "rev:" + s);
    // 处理逻辑
    if (commandIndex == 0) {
      if (s.startsWith("+OK")) {
        commandIndex++;
        channel.writeAndFlush("user " + getterHelper.getUser().getAccount() + "\r\n");
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 1) {
      if (s.startsWith("+OK")) {
        commandIndex++;
        channel.writeAndFlush("pass " + getterHelper.getUser().getPassword() + "\r\n");
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 2) {
      if (s.startsWith("+OK")) {
        commandIndex++;
        channel.writeAndFlush("stat\r\n");
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 3) {
      if (s.startsWith("+OK")) {
        String[] res = s.split(" ");
        count = Integer.parseInt(res[1]);
        if (count > 0) {
          index++;
          channel.writeAndFlush("retr " + index + "\r\n");
          commandIndex++;
        } else if (count == 0) {
          channel.writeAndFlush("quit\r\n");
          commandIndex++;
        }
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 4) {
      if (s.startsWith("+OK")) {
        if (count == 0) {
          // 没有新邮件
          getterHelper.setGetResult(
              GetResult.builder().flag(true).msg(s).emailList(emailList).build());
          channel.close();
        } else if (count >= index) {
          // 在这里要解析邮件
          String[] res = s.split(" ", 5);
          emailList.add(
              Email.builder()
                  .fromEmail(res[1].substring(res[1].indexOf(":") + 1))
                  .sendTime(Utils.getDate(res[2].substring(res[2].indexOf(":") + 1)))
                  .size(Integer.parseInt(res[3].substring(res[3].indexOf(":") + 1)))
                  .data(Utils.getUtf8FromBase64(res[4].substring(res[4].indexOf(":") + 1)))
                  .build());
          channel.writeAndFlush("dele " + index + "\r\n");
          commandIndex++;
        }
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 5) {
      // 获取
      if (s.startsWith("+OK")) {
        if (count > index) {
          index++;
          // 获取邮件内容
          channel.writeAndFlush("retr " + index + "\r\n");
          // 往上标记删除
          commandIndex--;
        } else if (count == index) {
          // 退出
          channel.writeAndFlush("quit\r\n");
          commandIndex++;
        }
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
        channel.close();
      }
    } else if (commandIndex == 6) {
      // 结束了
      if (s.startsWith("+OK")) {
        getterHelper.setGetResult(
            GetResult.builder().flag(true).msg(s).emailList(emailList).build());
        channel.close();
      } else {
        getterHelper.setGetResult(GetResult.builder().flag(false).msg(s).build());
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
    cause.printStackTrace();
    logger.debug("连接出现异常");
  }
}
