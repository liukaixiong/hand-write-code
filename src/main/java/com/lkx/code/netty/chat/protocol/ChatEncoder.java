package com.lkx.code.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 聊天消息编码器
 *
 * @author ： liukx
 * @time ： 2019/9/4 - 14:07
 */
public class ChatEncoder extends MessageToByteEncoder<ChatMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessage msg, ByteBuf out) throws Exception {
        // 直接通过序列化框架进行编码
        out.writeBytes(new MessagePack().write(msg));
    }

    /**
     * 这里需要将对象进行重新编码,如果是直接json传输的话,太大了。
     *
     * @param msg
     * @return
     */
    public static String encode(ChatMessage msg) {
        if (null == msg) {
            return "";
        }
        // 重新将数据进行编码成字符串
        // 内容 : [命令][时间][发送人名称][系统]-内容
        String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        if (ChatProtocol.LOGIN.getName().equals(msg.getCmd()) ||
                ChatProtocol.FLOWER.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");
        } else if (ChatProtocol.CHAT.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getSender() + "]");
        } else if (ChatProtocol.SYSTEM.getName().equals(msg.getCmd())) {
            prex += ("[" + msg.getOnline() + "]");
        }
        if (!(null == msg.getContent() || "".equals(msg.getContent()))) {
            prex += (" - " + msg.getContent());
        }
        return prex;
    }


}
