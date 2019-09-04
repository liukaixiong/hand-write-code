package com.lkx.code.netty.rpc.registry.handler;

import com.lkx.code.netty.rpc.registry.RegistryFactory;
import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
import com.lkx.code.netty.rpc.registry.utils.RegistyConstans;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.internal.ConcurrentSet;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 注册handler处理器
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 16:11
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(RegistryHandler.class);
    private RegistryFactory registryFactory;
    private static final AttributeKey<ConcurrentSet<RegistryRemoteData>> S_PUBLISH_KEY =
            AttributeKey.valueOf("server.published");
    private final ConcurrentMap<InetSocketAddress, Channel> nonServerChannels = PlatformDependent.newConcurrentHashMap();

    public RegistryHandler(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("触发是否存活: " + ctx.name());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RegistryRemoteData registryData = (RegistryRemoteData) msg;
        if (registryData != null) {
            String ip = registryData.getIp();
            int port = registryData.getPort();
            String project = registryData.getProject();
            ctx.attr(AttributeKey.valueOf(project));
            Integer type = registryData.getType();
            if (RegistyConstans.HEART_REQUEST == type) {
                registryFactory.add(project, ip, port);
            } else if (RegistyConstans.HEART_LIST_REQUEST == type) {
                List<RegistryRemoteData> list = registryFactory.getList(project, RegistyConstans.ONLINE_PROJECT);
                ctx.channel().writeAndFlush(list).sync();
            } else {
                logger.warn(" 无效的请求类型 : " + type);
            }

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive  触发 ... ");
        Channel ch = ctx.channel();
        ConcurrentSet<RegistryRemoteData> registryRemoteData = ch.attr(S_PUBLISH_KEY).get();


        super.channelInactive(ctx);
    }

}
