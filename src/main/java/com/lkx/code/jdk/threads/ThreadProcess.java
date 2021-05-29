package com.lkx.code.jdk.threads;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

/**
 * @Module TODO
 * @Description TODO
 * @Author liukaixiong
 * @Date 2020/12/11 14:56
 */
public class ThreadProcess {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        threadPoolExecutor.execute(()->{
            System.out.println("");
        });
    }

}
