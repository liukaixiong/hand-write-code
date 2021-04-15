package com.lkx.code.jdk.juc.lock;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LongAddrMainTest {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
    LongAdder adder = new LongAdder();

    /**
     * 未初始化的情况下
     * 
     * @throws Exception
     */
    @Test
    public void testAddNotInit() throws Exception {
        run(10, () -> {
            adder.add(1);
        });
        System.in.read();
    }

    /**
     * 添加初始化的情况下
     * 
     * @throws Exception
     */
    @Test
    public void testAddInit() throws Exception {
        // 预先初始化内容
        adder.add(1);
        run(10, () -> {
            adder.add(1);
        });
        adder.intValue();
        System.in.read();
    }

    public void run(int currentNumber, InvokeCode invokeCode) {
        Runnable target;
        for (int i = 0; i < currentNumber; i++) {
            new Thread(() -> {
                await();
                invokeCode.invoke();
            }).start();
        }
    }

    private void await() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    interface InvokeCode {
        void invoke();
    }

}