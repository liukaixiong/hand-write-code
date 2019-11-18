package com.lkx.code.netty.api.buffer;

import java.nio.IntBuffer;

/**
 * buffer测试
 *
 * @author ： liukx
 * @time ： 2019/11/13 - 15:12
 */
public class BufferTest {

    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(10);

        System.out.println(intBuffer.capacity());
        // 写入
        for (int i = 0; i < 5; i++) {
            // position 发生偏移 + 1
            intBuffer.put(i);
        }
        // 这时候 limit = capacity
        System.out.println(" before flip limit : " + intBuffer.limit());

        // 这时候触发翻转 触发读取操作
        intBuffer.flip();

        // position = 0 因为要从0开始读
        // limit = 5 ， 因为之前已经写到 5 了。
        System.out.println(" after flip limit : " + intBuffer.limit());

        System.out.println("===========while============");
        // 当position = limit 的时候，表示已经读到之前写的最后索引了，结束。
        while (intBuffer.hasRemaining()) {
            System.out.print(" position : " + intBuffer.position());
            System.out.print(" limit : " + intBuffer.limit());
            System.out.print(" capacity : " + intBuffer.capacity());
            System.out.println(" 值 : " + intBuffer.get());
        }


    }


}
