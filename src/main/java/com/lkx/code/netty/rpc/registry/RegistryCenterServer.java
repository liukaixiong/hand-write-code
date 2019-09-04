package com.lkx.code.netty.rpc.registry;

import com.lkx.code.netty.rpc.registry.handler.RegistryHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 注册中心启动
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 15:31
 */
public class RegistryCenterServer {
    private Logger logger = LoggerFactory.getLogger(RegistryCenterServer.class);

    private int port = 8888;

    private int dataTimeOut = 10;

    private RegistryFactory registryFactory;

    public RegistryCenterServer() {
        this.registryFactory = new RegistryFactory(dataTimeOut);
    }

    public void start() {
        // 构建主子线程
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //对象参数类型编码器
                            pipeline.addLast("encoder", new ObjectEncoder());
                            //对象参数类型解码器
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new RegistryHandler(registryFactory));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind("127.0.0.1",port).sync();
            logger.info(" 注册中心启动完成 ... 端口 : " + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        RegistryCenterServer registryCenterServer = new RegistryCenterServer();
        registryCenterServer.start();
    }

}
