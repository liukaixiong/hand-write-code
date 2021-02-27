package com.lkx.code.jdk.juc.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    private Logger logger = LoggerFactory.getLogger(ReadWriteLockTest.class);
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

	/**
	 *	这里仅仅是想知道锁重入的情况，是不是这个时候加入的锁会到等待队列里面排队。 
	 */
    public void queryData() {
        try {
            Thread.sleep(500);
            readLock.lock();
            logger.info("查询数据完成.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }

    }

    public void test3() throws Exception {
        // 开始锁降级
        writeLock.lock();
        logger.info("主线程抢到写锁...");
       	// 这里的休眠是为了让下面线程能在预想的情况下加入等待队列.
        Thread.sleep(500);
		// 这里就是假设等待队列里面排在前面的是读锁线程
        processReadLock(1); // 这里可以和下面processWriteLock对调
		
        processWriteLock(2);

        Thread.sleep(500);
		// 开始锁降级
        readLock.lock(); // A 降级开始
        // 锁降级完成
        writeLock.unlock();
        logger.info("主线程释放写锁");
        queryData();
        readLock.unlock(); // A 降级结束
        logger.info("主线程读锁释放");
        logger.info("过程结束..");
    }

    private void processWriteLock(int threadIndex) {
        new Thread(() -> {
            logger.info("线程" + threadIndex + " 写锁开始竞争,阻塞中.");
            writeLock.lock();
            logger.info("线程" + threadIndex + " 写锁执行中..");
            writeLock.unlock();
            logger.info("线程" + threadIndex + " 写锁释放..");
        }).start();
    }

    private void processReadLock(int threadIndex) {
        new Thread(() -> {
            logger.info("线程" + threadIndex + " 读锁开始竞争,阻塞中.");
            readLock.lock();
            logger.info("线程" + threadIndex + " 读锁执行中..");
            readLock.unlock();
            logger.info("线程" + threadIndex + " 读锁释放..");
        }).start();
    }


    public static void main(String[] args) throws Exception {
        ReadWriteLockTest readWriteLockTest = new ReadWriteLockTest(); 
        readWriteLockTest.test3();
    }
}