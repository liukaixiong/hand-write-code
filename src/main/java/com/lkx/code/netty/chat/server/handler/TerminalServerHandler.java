package com.lkx.code.netty.chat.server.handler;

import com.lkx.code.netty.chat.protocol.ChatMessage;
import com.lkx.code.netty.chat.server.processor.MsgProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过命令行来聊天的处理器
 *
 * @author : liukx
 * @date : 2019/9/4 - 15:10
 */
public class TerminalServerHandler extends SimpleChannelInboundHandler<ChatMessage> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        log.info("Termina process msg ");
        processor.sendMsg(ctx.channel(), msg);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("Socket Client: 与客户端断开连接:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

}
