package com.lkx.code.suanfa;

/**
 * 启动两个线程： B线程等待A线程执行完毕之后，才开始执行。
 * 
 */
public class ThreadWait {

    public static void main(String[] args) throws Exception {

        Thread threadA = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Thread-A");

        Thread threadB = new Thread(() -> {
            try {
                threadA.join();
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Thread-B");

        threadB.start();
        threadA.start();
        threadA.join();
        threadB.join();
    }
}
