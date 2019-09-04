package com.lkx.code.netty.chat.protocol;

/**
 * 自定义协议类型
 *
 * @author ： liukx
 * @time ： 2019/9/4 - 14:59
 */
public enum ChatProtocol {
    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),
    /**
     * 登录
     */
    LOGIN("LOGIN"),
    /**
     * 登出
     */
    LOGOUT("LOGOUT"),
    /**
     * 聊天
     */
    CHAT("CHAT"),
    /**
     * 送花
     */
    FLOWER("FLOWER");

    private String name;

    public static boolean isChatP(String content) {
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
    }

    ChatProtocol(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
