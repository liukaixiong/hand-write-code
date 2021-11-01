package com.lkx.code.design.observerable;

import java.util.ArrayList;
import java.util.List;

public class Observerable {

    private List<Observer> observers = new ArrayList<Observer>();

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        notifyAllObservers(state);
    }

    // 添加观察者
    public void addServer(Observer observer) {
        observers.add(observer);
    }

    // 移除观察者
    public void removeServer(Observer observer) {
        observers.remove(observer);
    }

    // 通知
    public void notifyAllObservers(int state) {

        if (state != 1) {
            System.out.println("不是通知的状态");
            return;
        }

        for (Observer observer : observers) {
            observer.doEvent();
        }
    }
}