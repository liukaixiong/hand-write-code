package com.lkx.code.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class Server {

    public static void main(String[] args) throws Exception {
        //1 创建2个线程，一个是负责接收客户端的连接。一个是负责进行数据传输的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup wordGroup = new NioEventLoopGroup();
        try {
            //2 创建服务器辅助类
            ServerBootstrap serverBootStrap = new ServerBootstrap();
            serverBootStrap.group(bossGroup, wordGroup)
                    // JavaNIO网络编程主类
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // handler 针对 bossGroup 线程的 | childHandler 针对于wordGroup 的
                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            //1 设置特殊分隔符
                            ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                            //2
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                            //3 设置字符串形式的解码
                            sc.pipeline().addLast(new StringDecoder());
                            // 空闲心跳检测
                            sc.pipeline().addLast(new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS));
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });

            //4 绑定连接
            ChannelFuture cf = serverBootStrap.bind(8765).sync();

            //等待服务器监听端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            wordGroup.shutdownGracefully();
        }
    }

}