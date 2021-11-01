package com.lkx.code.design.observerable;

import com.google.common.eventbus.EventBus;

public class EventBusCenter {

    private static EventBus eventBus = new EventBus();

    private EventBusCenter() {}

    public static EventBus getInstance() {
        return eventBus;
    }

    // 添加观察者
    public static void register(Object obj) {
        eventBus.register(obj);
    }

    // 移除观察者
    public static void unregister(Object obj) {
        eventBus.unregister(obj);
    }

    // 把消息推给观察者
    public static void post(Object obj) {
        eventBus.post(obj);
    }
}