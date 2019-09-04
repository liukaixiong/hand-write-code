package com.lkx.code.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 聊天内容的编码
 *
 * @author ： liukx
 * @time ： 2019/9/4 - 14:00
 */
public class ChatDecoder extends ByteToMessageDecoder {

    private Logger log = LoggerFactory.getLogger(ChatDecoder.class);
    /**
     * 正则表达式替换
     * 编码格式 : [命令][时间][发送人名称][系统]-内容
     */
    private static Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            // 获取可读字节数
            final int length = in.readableBytes();
            final byte[] array = new byte[length];
            String content = new String(array, in.readerIndex(), length);

            if (!(null == content || "".equals(content.trim()))) {
                if (!ChatProtocol.isChatP(content)) {
                    log.warn(" 获取到异常的空消息 .. ");
                    ctx.channel().pipeline().remove(this);
                    return;
                }
            }

            in.getBytes(in.readerIndex(), array, 0, length);
            // 直接通过序列化框架进行解码之后，往下一个handler进行传输
            out.add(new MessagePack().read(array, ChatMessage.class));
            // 数据已经解码完毕，直接清空buffer。
            in.clear();
        } catch (MessageTypeException e) {
//            log.error("decode error", e);
            ctx.channel().pipeline().remove(this);
        }
    }

    public static ChatMessage decode(String msg) {
        if (null == msg || "".equals(msg.trim())) {
            return null;
        }
        try {
            String[] message = msg.split("-");
            String[] headers = StringUtils.substringsBetween(message[0], "[", "]");
            String content = "";
            if (message.length >= 2) {
                content = message[1].trim();
            }

            long time = 0;
            try {
                time = Long.parseLong(headers[1]);
            } catch (Exception e) {
            }
            String nickName = headers[2];
            //昵称最多十个字
            nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);
            if (msg.startsWith("[" + ChatProtocol.LOGIN.getName() + "]")) {
                return new ChatMessage(headers[0], headers[3], time, nickName);
            } else if (msg.startsWith("[" + ChatProtocol.CHAT.getName() + "]")) {
                return new ChatMessage(headers[0], time, nickName, content);
            } else if (msg.startsWith("[" + ChatProtocol.FLOWER.getName() + "]")) {
                return new ChatMessage(headers[0], headers[3], time, nickName);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}