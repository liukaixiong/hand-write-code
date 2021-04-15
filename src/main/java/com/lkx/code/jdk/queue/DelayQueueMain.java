package com.lkx.code.jdk.queue;

import com.lkx.code.jdk.queue.delayed.MyDelayedTask;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * 延迟队列运行方法
 * 
 * @author liukx
 */
public class DelayQueueMain {
    public static void main(String[] args) throws Exception {
        DelayQueue delayQueue = new DelayQueue();
        new Thread(new Runnable() {
            @Override
            public void run() {
                delayQueue.offer(new MyDelayedTask("task1", 10000));
                delayQueue.offer(new MyDelayedTask("task2", 3900));
                delayQueue.offer(new MyDelayedTask("task3", 1900));
                delayQueue.offer(new MyDelayedTask("task4", 5900));
                delayQueue.offer(new MyDelayedTask("task5", 6900));
                delayQueue.offer(new MyDelayedTask("task6", 7900));
                delayQueue.offer(new MyDelayedTask("task7", 4900));
            }
        }).start();

        while (true) {
            Delayed take = delayQueue.take();
            System.out.println(take);
        }

    }
}
