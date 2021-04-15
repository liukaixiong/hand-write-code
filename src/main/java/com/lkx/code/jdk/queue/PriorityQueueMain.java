package com.lkx.code.jdk.queue;

import java.util.PriorityQueue;

public class PriorityQueueMain {
    public static void main(String[] args) {

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(100);
        priorityQueue.add(50);
        priorityQueue.add(150);
        priorityQueue.add(11);
        priorityQueue.add(55);
        priorityQueue.add(5);
        /**
         * 添加完毕之前: 11->50->150->100
         *
         * 消费完第一个:
         *
         */

        while (true) {
            Integer poll = priorityQueue.poll();
            if (poll != null) {
                System.out.println(poll);
            } else {
                break;
            }
        }
    }
}
