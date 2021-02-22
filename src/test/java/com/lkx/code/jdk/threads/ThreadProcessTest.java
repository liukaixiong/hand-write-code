package com.lkx.code.jdk.threads;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Module TODO
 * @Description TODO
 * @Author liukaixiong
 * @Date 2020/12/11 14:56
 */
public class ThreadProcessTest {


    @Test
    public void test1() {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<CompletableFuture<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return finalI;
            }, executorService);
            result.add(future);
        }
        // 等待所有结果都获取完毕
        long start = System.currentTimeMillis();
        result.forEach(res -> {
            try {
                res.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println((System.currentTimeMillis() - start));
    }


}