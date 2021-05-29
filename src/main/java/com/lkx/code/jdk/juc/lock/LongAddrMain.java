package com.lkx.code.jdk.juc.lock;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LongAddrMain {

    CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

    public static void main(String[] args) {
        // 并发累加器
        LongAdder addr = new LongAdder();
        addr.add(1);
        addr.add(10);
        addr.add(-1);
        System.out.println(addr.longValue());

        /**
         * 负责计算自定义的的运算，基于比如乘除类型的。
         */
        LongAccumulator longAccumulator = new LongAccumulator((a, b) -> a * b, 0L);
        longAccumulator.accumulate(10);
        longAccumulator.accumulate(9);
        long l = longAccumulator.longValue();
        System.out.println(l);
    }

    public void run(Supplier<Integer> supplier) {

    }

}
