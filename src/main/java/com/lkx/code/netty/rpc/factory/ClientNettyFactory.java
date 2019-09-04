package com.lkx.code.netty.rpc.factory;

import com.google.common.collect.Maps;
import com.lkx.code.netty.rpc.consumer.proxy.RpcProxy;
import com.lkx.code.netty.rpc.handler.MessageHandler;
import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;

/**
 * 客户端连接Netty
 *
 * @author ： liukx
 * @time ： 2019/8/29 - 13:28
 */
public class ClientNettyFactory {
    private int nWorkers = 100;
    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private final ConcurrentMap<SocketAddress, ChannelFuture> connectors = Maps.newConcurrentMap();

    private MessageHandler messageHandler;

    private RpcProxy proxy;
    private Map<String, List<RegistryRemoteData>> remoteMap;

    public ClientNettyFactory(Map<String, List<RegistryRemoteData>> remoteMap) {
        messageHandler = new MessageHandler(remoteMap);
        init();
    }

    public void init() {
//        SocketAddress socketAddress = InetSocketAddress.createUnresolved(host, port);
        ThreadFactory workerfactory = new DefaultThreadFactory("connectorRegister", Thread.MAX_PRIORITY);
        worker = new NioEventLoopGroup(nWorkers, workerfactory);
        ((NioEventLoopGroup) worker).setIoRatio(100);
        bootstrap = new Bootstrap();
        bootstrap.group(worker).channelFactory(SocketChannelProvider.JAVA_NIO_CONNECTOR);
//        bootstrap
//                .option(ChannelOption.SO_REUSEADDR, true)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.ALLOW_HALF_CLOSURE, false)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
    }

    public Channel connectorRegister(String host, int port) {
        Channel channel = connectorClient(host, port, messageHandler);
        return channel;
    }

    public Channel connectorClient(String host, int port, ChannelHandler channelHandler) {
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast(new LengthFieldPrepender(4));
                //对象参数类型编码器
                pipeline.addLast("encoder", new ObjectEncoder());
                //对象参数类型解码器
                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                pipeline.addLast("handler", channelHandler);
            }
        });
        Channel channel = getChannel(host, port);
        return channel;
    }

    private Channel getChannel(String host, int port) {
        Channel channel;
        SocketAddress socketAddress = InetSocketAddress.createUnresolved(host, port);
        ChannelFuture channelFuture = connectors.get(socketAddress);
        if (channelFuture != null) {
            channel = channelFuture.channel();
        } else {
            channelFuture = bootstrap.connect(host, port);
            try {
                channelFuture.sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectors.put(socketAddress, channelFuture);
            channel = channelFuture.channel();
        }
        return channel;
    }

    public static void main(String[] args) throws InterruptedException {
//        ClientNettyFactory c = new ClientNettyFactory();
//        c.connectorRegister("127.0.0.1", 8888);

    }


}
