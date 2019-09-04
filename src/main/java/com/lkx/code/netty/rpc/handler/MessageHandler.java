package com.lkx.code.netty.rpc.handler;

import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
import com.lkx.code.netty.rpc.registry.utils.RegistyConstans;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息处理器
 *
 * @author ： liukx
 * @time ： 2019/8/29 - 14:18
 */
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private Map<String, List<RegistryRemoteData>> remoteMap;

    public MessageHandler(Map<String, List<RegistryRemoteData>> remoteMap) {
        this.remoteMap = remoteMap;
    }

    /**
     * 这里处理取消/订阅/推送服务监听
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 处理取消、订阅、推送服务监听收到消息 ： " + msg.toString());
        if (msg instanceof RegistryRemoteData) {
            process((RegistryRemoteData) msg);
        } else if (msg instanceof List) {
            List<RegistryRemoteData> registryRemoteDataList = (List<RegistryRemoteData>) msg;
            for (int i = 0; i < registryRemoteDataList.size(); i++) {
                process(registryRemoteDataList.get(i));
            }
        }
    }

    private void process(RegistryRemoteData msg) {
        RegistryRemoteData registryRemoteData = msg;
        if (registryRemoteData.getType() == RegistyConstans.PULL_PROJECT) {
            onlineProject(registryRemoteData);
        } else if (registryRemoteData.getType() == RegistyConstans.ONLINE_PROJECT) {
            onlineProject(registryRemoteData);
        } else if (RegistyConstans.ONLINE_PROJECT == registryRemoteData.getType()) {
            List<RegistryRemoteData> rmd = this.remoteMap.get(registryRemoteData.getProject());
            if (rmd != null) {
                rmd.remove(registryRemoteData);
            }
        }
    }

    private void onlineProject(RegistryRemoteData registryRemoteData) {
        List<RegistryRemoteData> rmd = this.remoteMap.get(registryRemoteData.getProject());
        if (rmd == null) {
            rmd = new ArrayList<>();
            rmd.add(registryRemoteData);
            this.remoteMap.put(registryRemoteData.getProject(), rmd);
            logger.info(" 注册中心推送一个服务过来注册1 : " + registryRemoteData.toString());
        } else {
            // 这里还需要去重
            if (!rmd.contains(registryRemoteData)) {
                rmd.add(registryRemoteData);
                logger.info(" 注册中心推送一个服务过来注册2 : " + registryRemoteData.toString());
            }
        }
    }

    /**
     * 处理上线关联服务
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(" 处理取消 channelActive ： " + ctx.toString());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
