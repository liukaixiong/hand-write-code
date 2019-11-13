package com.lkx.code.netty.chat.server.processor;

import com.alibaba.fastjson.JSONObject;
import com.lkx.code.netty.chat.protocol.ChatDecoder;
import com.lkx.code.netty.chat.protocol.ChatEncoder;
import com.lkx.code.netty.chat.protocol.ChatMessage;
import com.lkx.code.netty.chat.protocol.ChatProtocol;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理器
 *
 * @author ： liukx
 * @time ： 2019/9/4 - 15:11
 */
public class MsgProcessor {

    private Logger log = LoggerFactory.getLogger(MsgProcessor.class);
    /**
     * 记录在线的用户
     */
    private static ChannelGroup onlineUserGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 定义一些和在线用户绑定的标识信息
     */
    public static final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    public static final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    public static final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");
    public static final AttributeKey<String> FROM = AttributeKey.valueOf("from");

    public void sendMsg(Channel client, String text) {
        sendMsg(client, ChatDecoder.decode(text));
    }

    public void sendMsg(Channel client, ChatMessage request) {

        if (request == null) {
            log.warn(" 异常数据传输 ...");
            return;
        }

        String address = getAddress(client);
        String cmd = request.getCmd();

        // 如果是聊天
        if (ChatProtocol.CHAT.getName().equals(cmd)) {

            for (Channel channel : onlineUserGroup) {
                boolean isSelf = (channel == client);
                if (isSelf) {
                    request.setSender("you");
                } else {
                    request.setSender(getNickName(client));
                }
                request.setTime(sysTime());
                sendOnlineMsg(channel, request);
            }
        } else if (ChatProtocol.FLOWER.getName().equals(cmd)) {
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            // 送花时间校验
            if (null != attrs) {
                // 得到上一次送花的时间
                long lastTime = attrs.getLongValue("lastFlowerTime");
                //60秒之内不允许重复刷鲜花
                int seconds = 10;
                long sub = currTime - lastTime;
                if (sub < 1000 * seconds) {
                    request.setSender("you");
                    request.setCmd(ChatProtocol.SYSTEM.getName());
                    request.setContent("您送鲜花太频繁," + (seconds - Math.round(sub / 1000)) + "秒后再试");
                    String content = ChatEncoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }
            //正常送花
            for (Channel channel : onlineUserGroup) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("你给大家送了一波鲜花雨");
                    setAttrs(client, "lastFlowerTime", currTime);
                } else {
                    request.setSender(getNickName(client));
                    request.setContent(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(sysTime());

                String content = ChatEncoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        } else if (ChatProtocol.LOGIN.getName().equals(cmd)) {
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(address);
            client.attr(FROM).getAndSet(request.getTerminal());
            onlineUserGroup.add(client);
            // 将上线消息发送给其他在线的人
            for (Channel channel : onlineUserGroup) {
                boolean isSelf = (channel == client);
                if (!isSelf) {
                    request = new ChatMessage(ChatProtocol.SYSTEM.getName(), sysTime(), onlineUserGroup.size(),
                            getNickName(client) + "加入");
                } else {
                    request = new ChatMessage(ChatProtocol.SYSTEM.getName(), sysTime(), onlineUserGroup.size(), "已与服务器建立连接！");
                }
                sendOnlineMsg(channel, request);
            }
        }


    }

    /**
     * 发送上线消息
     *
     * @param channel
     * @param request
     */
    private void sendOnlineMsg(Channel channel, ChatMessage request) {
        if ("Console".equals(channel.attr(FROM).get())) {
            channel.writeAndFlush(request);
            return;
        }
        String content = ChatEncoder.encode(request);
        channel.writeAndFlush(new TextWebSocketFrame(content));
    }

    /**
     * 获取用户远程IP地址
     *
     * @param client
     * @return
     */
    public String getAddress(Channel client) {
        return client.remoteAddress().toString().replaceFirst("/", "");
    }

    /**
     * 获取用户昵称
     *
     * @param client
     * @return
     */
    public String getNickName(Channel client) {
        return client.attr(NICK_NAME).get();
    }

    public JSONObject getAttrs(Channel client) {
        try {
            return client.attr(ATTRS).get();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    private Long sysTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取扩展属性
     *
     * @param client
     * @return
     */
    private void setAttrs(Channel client, String key, Object value) {
        try {
            JSONObject json = client.attr(ATTRS).get();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }
    }

}
