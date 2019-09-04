package com.lkx.code.netty.chat.server.handler;

import com.lkx.code.netty.chat.server.processor.MsgProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("WebSocket process msg ");
        processor.sendMsg(ctx.channel(), msg.text());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();
        String addr = processor.getAddress(client);
        log.info("WebSocket Client:" + addr + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
