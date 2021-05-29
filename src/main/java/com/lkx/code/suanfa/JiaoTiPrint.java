package com.lkx.code.suanfa;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替打印奇偶数
 * 
 */
public class JiaoTiPrint {

    public static void main(String[] args) throws Exception {
        ReentrantLock lock = new ReentrantLock();

        Condition jiCondition = lock.newCondition();

        Condition ouCondition = lock.newCondition();

        AtomicInteger count = new AtomicInteger();

        Thread jiNumThread = new Thread(() -> {
            while (count.get() <= 100) {
                lock.lock();
                try {
                    int i = count.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " -> " + i);
                    ouCondition.signalAll();

                    if (i >= 99) {
                        break;
                    }

                    jiCondition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "jiNumThread");

        Thread ouNumThread = new Thread(() -> {
            while (count.get() <= 100) {
                lock.lock();
                try {
                    int i = count.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " -> " + i);
                    jiCondition.signal();
                    if (i >= 99) {
                        break;
                    }
                    ouCondition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "ouNumThread");

        //设置优先级，让奇数线程先运行。
        jiNumThread.setPriority(10);

        jiNumThread.start();
        ouNumThread.start();
        jiNumThread.join();
        ouNumThread.join();
        System.out.println("执行结束");
    }

}
