package com.lkx.code.netty.chat.server;

import com.lkx.code.netty.chat.protocol.ChatDecoder;
import com.lkx.code.netty.chat.protocol.ChatEncoder;
import com.lkx.code.netty.chat.server.handler.HttpServerHandler;
import com.lkx.code.netty.chat.server.handler.TerminalServerHandler;
import com.lkx.code.netty.chat.server.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动类
 *
 * @author ： liukx
 * @time ： 2019/9/4 - 13:54
 */
public class ChatServer {


    private Logger logger = LoggerFactory.getLogger(ChatServer.class);

    private int port = 8080;

    public void startServer() {
        startServer(port);
    }

    public void startServer(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 这里设定数据传输的编解码以及处理器
                            pipeline.addLast(new ChatDecoder());
                            pipeline.addLast(new ChatEncoder());
                            pipeline.addLast(new TerminalServerHandler());  //Inbound

                            /** 解析Http请求 */
                            pipeline.addLast(new HttpServerCodec());  //Outbound
                            //主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));//Inbound
                            //主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
                            pipeline.addLast(new ChunkedWriteHandler());//Inbound、Outbound
                            pipeline.addLast(new HttpServerHandler());//Inbound

                            /** 解析WebSocket请求 */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));    //Inbound
                            pipeline.addLast(new WebSocketServerHandler()); //Inbound
                        }
                    });

            ChannelFuture future = server.bind(port).sync();
            logger.info(" 聊天服务器已经启动，端口 ： " + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.startServer();
    }

}
