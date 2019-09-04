package com.lkx.code.netty.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回结果封装
 *
 * @author ： liukx
 * @time ： 2019/8/30 - 15:50
 */
public class ResultChannelHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ResultChannelHandler.class);
    private Object object;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("返回结果集 : " + msg);
        this.object = msg;
    }

    public Object getObject() {
        return object;
    }
}
