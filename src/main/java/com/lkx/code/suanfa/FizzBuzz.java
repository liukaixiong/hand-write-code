package com.lkx.code.suanfa;

import java.io.IOException;
import java.util.function.IntConsumer;

/**
 * 
 * 多线程面试题: 多线程并发执行,遇到能被3整除打印fuzz 、遇到能被5整除打印buzz、遇到能被3和5整除打印fuzzbuzz
 * 
 */
class FizzBuzz {
    public static void main(String[] args) throws IOException {
        int n = 100;
        new Thread(() -> {
            int i = 0;
            while (i < n) {
                if (i % 3 == 0 && i % 5 != 0) {
                    System.out.println(i + " - fuzz");
                }
                i++;
            }
        }).start();
        new Thread(() -> {
            int i = 0;
            while (i < n) {
                if (i % 3 != 0 && i % 5 == 0) {
                    System.out.println(i + " - buzz");
                }
                i++;
            }
        }).start();
        new Thread(() -> {
            int i = 0;
            while (i < n) {
                if (i % 3 == 0 && i % 5 == 0) {
                    System.out.println(i + " - fizzbuzz");
                }
                i++;
            }
        }).start();
        System.in.read();
    }
}