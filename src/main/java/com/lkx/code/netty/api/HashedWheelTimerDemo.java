package com.lkx.code.netty.api;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * 时间论算法API使用
 *
 * @author ： liukx
 * @time ： 2019/9/6 - 15:31
 */
public class HashedWheelTimerDemo {
    private static Logger logger = LoggerFactory.getLogger(HashedWheelTimerDemo.class);

    public static void main(String[] args) throws Exception {
        /**
         * tickDuration : 时间刻度
         * TimeUnit : 时间单位
         * ticksPerWheel : 时间轮的大小
         * 这里表示 时间轮有60格，每1秒执行一格。
         */
        HashedWheelTimer timer = new HashedWheelTimer(1000, TimeUnit.MILLISECONDS, 60);

        System.out.println(LocalTime.now());

        /**
         * 延迟5秒开始执行时间轮
         */
        TimerTask timerTask = (timeout) -> {
            logger.info(" 1 - " + String.valueOf(LocalTime.now()));
            logger.info(" 2 - " + timeout);
        };
        timer.newTimeout(timerTask, 600, TimeUnit.SECONDS);
        System.out.println("---------------------------");

        timer.newTimeout((timeout) -> {
            logger.info(" 1 - " + String.valueOf(LocalTime.now()));
            logger.info(" 2 - " + timeout);
        }, 3, TimeUnit.SECONDS);

        // 返回全部未执行的任务列表
        // Set<Timeout> stop = timer.stop();

        //阻塞main线程
        System.in.read();
    }

}
