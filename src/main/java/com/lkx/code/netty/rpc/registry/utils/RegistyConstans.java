package com.lkx.code.netty.rpc.registry.utils;

/**
 * 常量类定义
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 16:28
 */
public class RegistyConstans {
    /**
     * 心跳请求
     */
    public final static Integer HEART_REQUEST = 0;

    /**
     * 心跳注册请求
     */
    public final static Integer HEART_REGISTER_REQUEST = 1;
    /**
     * 获取数据列表请求
     */
    public final static Integer HEART_LIST_REQUEST = 2;

    /**
     * 推送类型
     */
    public final static Integer PUSH_PROJECT = 01;

    /**
     * 订阅类型
     */
    public final static Integer PULL_PROJECT = 01;
    /**
     * 下线类型
     */
    public final static Integer OFFLINE_PROJECT = 02;
    /**
     * 上线类型
     */
    public final static Integer ONLINE_PROJECT = 03;


}
