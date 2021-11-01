package com.lkx.code.design.observerable.impl;

import com.lkx.code.design.observerable.Observer;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/1 - 15:17
 */
public class IMMessageObserver implements Observer {

    @Override
    public void doEvent() {
        System.out.println("发送IM消息");
    }
}
