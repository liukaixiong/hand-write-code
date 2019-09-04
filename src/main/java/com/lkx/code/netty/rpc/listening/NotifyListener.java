package com.lkx.code.netty.rpc.listening;

import com.lkx.code.netty.rpc.protocol.RegisterMeta;

/**
 * 监听回调类
 *
 * @author ： liukx
 * @time ： 2019/8/29 - 14:54
 */
public interface NotifyListener {

    void notify(RegisterMeta registerMeta, NotifyEvent event);
    enum NotifyEvent {
        CHILD_ADDED,
        CHILD_REMOVED
    }
}
