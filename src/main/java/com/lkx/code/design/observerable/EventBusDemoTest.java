package com.lkx.code.design.observerable;

public class EventBusDemoTest {

    public static void main(String[] args) {
        EventListener eventListener = new EventListener();
        EventBusCenter.register(eventListener);
        EventBusCenter.post(new NotifyEvent("13542185454", "123@qq.com", "666"));
    }

}
