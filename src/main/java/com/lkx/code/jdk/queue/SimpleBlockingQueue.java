package com.lkx.code.jdk.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 手写阻塞队列
 *
 * @author ： liukx
 * @time ： 2019/8/14 - 17:10
 */
public class SimpleBlockingQueue<T> {
    private Logger logger = LoggerFactory.getLogger(SimpleBlockingQueue.class);
    /**
     * 构建一个存储集合
     */
    public List<T> queue = new LinkedList<>();
    /**
     * 记录存储集合的大小
     */
    public volatile int size;
    /**
     * 记录当前存储的下标
     */
    public volatile int index;

    /**
     * 锁结构
     */
    private Lock lock = new ReentrantLock();
    private Condition isFull = lock.newCondition();
    private Condition isNull = lock.newCondition();

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public void add(T obj) throws Exception {
        lock.lock();
        try {
            // 如果下标超过了指定的大小，说明数据满了。
            if (index >= size) {
                logger.info(" 阻塞队列满啦 ... ");
                // 满了需要阻塞住，让take消费之后让出一个位置来唤醒当前isFull的阻塞。
                // 然后往下走,加入当前线程的数据添加到让出来的那个位置上
                isFull.await();
            }
            queue.add(obj);
            // 下标累加，take会自减
            ++index;
            // 这里和上面出现的情况一样，如果有数据进入，那么take那边如果是因为没有数据而阻塞住，那么这里进来了一个数据，
            // 则要通知take方法可以消费了.
            isNull.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws Exception {
        lock.lock();
        try {
            if (index == 0) {
                logger.info(" 阻塞队列中没有值...");
                isNull.await();
            }
            T t = queue.get(0);
            // 这里会把第一个位置删掉，这里也是为什么要用LinkedList的原因。
            // 把第一个位置删掉之后,第二个位置会自动顶到第一个位置上来。
            queue.remove(0);
            logger.info("take : " + t.toString());
            --index;
            isFull.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleBlockingQueue queue = new SimpleBlockingQueue(10);

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    queue.add(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000);
        new Thread(() -> {
            for (; ; ) {
                try {
                    queue.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        for (int i = 0; i < 100; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                try {
//                    queue.add(finalI);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//        for (int i = 0; i < 100; i++) {
//            new Thread(() -> {
//                try {
//                    Object take = queue.take();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }

    }

}
