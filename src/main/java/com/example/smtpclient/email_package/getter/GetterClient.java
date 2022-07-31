package com.example.smtpclient.email_package.getter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

public class GetterClient {
  private static String host = "110.40.230.26";
  private static NioEventLoopGroup group;
  private static Bootstrap bootstrap;
  // 日志
  private static Logger logger = Logger.getLogger(GetterClient.class);

  /**
   * 利用netty异步线程，开启smtp服务
   *
   * @throws Exception
   */
  public static void startGetterClient() {
    try {
      group = new NioEventLoopGroup();
      // 创建启动助手来配置netty参数
      bootstrap = new Bootstrap();
      bootstrap
          .group(group)
          .channel(NioSocketChannel.class) // 设置使用NioSocketChannel作为通道的实现
          .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列中等待连接的个数
          .handler(new GetterClientInitializer()); // 添加编码解码和处理器
    } catch (Exception e) {
      logger.debug(e.getMessage());
      e.printStackTrace();
    }
  }

  public static Channel getConnection(int port) {
    return bootstrap.connect(host, port).addListener(new ChannelFutureListener() {

      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        /*
         * 这里就不是主线程了，这里是 netty 线程中执行
         */
        if (future.isSuccess()) {
          logger.info(host + " " + port + "连接pop3成功");
        } else {
          logger.debug(host + " " + port + "连接pop3失败");
//          // 连接不成功，5秒后重新连接
//          future.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//              System.out.println("4444444444444  reconnect " + host + " " + Thread.currentThread());
//              connect(host, port);
//            }
//          }, 5, TimeUnit.SECONDS);
        }
      }
    }).channel();
  }

  /** 终止netty相关数据，关闭sender服务 但是由于服务器一直运行，所以此函数暂时不会调用 */
  public static void shutDownGetterClient() {
    if ( group != null ) {
      group.shutdownGracefully();
    }
  }

  // 可以用于测试
  public static void main(String[] args) {
    try {
      startGetterClient();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
