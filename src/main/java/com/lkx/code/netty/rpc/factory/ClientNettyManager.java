package com.lkx.code.netty.rpc.factory;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 客户端连接管理器
 *
 * @author ： liukx
 * @time ： 2019/8/29 - 13:29
 */
public class ClientNettyManager {

    private ConcurrentMap<SocketAddress, CopyOnWriteArrayList<ChannelFuture>> connections = Maps.newConcurrentMap();

}
