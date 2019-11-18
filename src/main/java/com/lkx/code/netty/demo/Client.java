package com.lkx.code.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Client {

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        //1
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        //2
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        //3
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();

        for (int i = 0; i < 100; i++) {
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer((i + "$_").getBytes()));
            Thread.sleep(1000);
        }


        //等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();

    }
}