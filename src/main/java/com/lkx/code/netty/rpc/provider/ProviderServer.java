package com.lkx.code.netty.rpc.provider;

import com.lkx.code.netty.rpc.factory.ClientNettyFactory;
import com.lkx.code.netty.rpc.provider.handler.ProviderHandler;
import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生产者服务端
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 19:32
 */
public class ProviderServer {

    private Logger logger = LoggerFactory.getLogger(ProviderServer.class);

    private Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private Map<String, List<RegistryRemoteData>> remoteMap = new ConcurrentHashMap<>();

    private List<String> classNames = new ArrayList<String>();
    private ClientNettyFactory clientNettyFactory;
    private int port;

    public ProviderServer(int port) {
        this.port = port;
        this.clientNettyFactory = new ClientNettyFactory(remoteMap);
    }

    public void start() {

        // 扫描包
        scannerClass("com.lkx.code.netty.rpc.provider.impl");
        // 注册bean
        doRegister();
        // 将本地服务注册到注册中心
        registerCenterServer();
        // 启动服务
        startServer();
    }

    /*
     * 递归扫描
     */
    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            //如果是一个文件夹，继续递归
            if (file.isDirectory()) {
                scannerClass(packageName + "." + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    /**
     * 完成注册
     */
    private void doRegister() {
        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                beanMap.put(i.getName(), clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startServer() {


        // 构建主子线程
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap providerServer = new ServerBootstrap();
            providerServer.group(boss, worker)
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
                            pipeline.addLast(new ProviderHandler(beanMap));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = providerServer.bind(port).sync();
            logger.info(" 生产者启动完成 ... 端口 : " + port);

            // 启动完完成之后，将服务注册到注册中心

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCenterServer() {
        RegistryRemoteData registryRemoteData = new RegistryRemoteData();
        registryRemoteData.setIp("127.0.0.1");
        registryRemoteData.setPort(port);
        registryRemoteData.setProject("provider");
        // 向注册中心注册
        Channel channel = clientNettyFactory.connectorRegister("127.0.0.1", 8888);
        try {
            channel.writeAndFlush(registryRemoteData)
                    .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProviderServer providerServer = new ProviderServer(8889);
        providerServer.start();
    }

}
