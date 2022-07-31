package com.example.smtpclient.email_package.sender;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class SenderClientInitializer extends ChannelInitializer<SocketChannel> {

  // 每个连接建立都需要初始化
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    // 往pipeline中添加配置
    socketChannel
        .pipeline()
        // TCP/IP数据包的传输方式，包在传输的过程中会分片和重组
        // 确保数据会按行读取，以换行符作为解码依据，上面的原因导致粘包，正确解包
        .addLast("framer", new DelimiterBasedFrameDecoder(8 * 1024, Delimiters.lineDelimiter()))
        // 添加字符串解码器，用于接受客户端数据
        .addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
        // 添加字符串编码器，用于向客户端发送数据
        .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
        // 添加处理器，每个连接一个handler对象
        .addLast("handler", new SenderClientHandler());
  }
}