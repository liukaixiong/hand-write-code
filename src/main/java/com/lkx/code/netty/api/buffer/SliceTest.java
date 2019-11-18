package com.lkx.code.netty.api.buffer;

import java.nio.ByteBuffer;

/**
 * 分片
 *
 * @author ： liukx
 * @time ： 2019/11/13 - 17:16
 */
public class SliceTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }

        // 先指定范围
        byteBuffer.position(2);
        byteBuffer.limit(6);
        // 将byteBuff中的一部分范围值，作为分片取出
        ByteBuffer slice = byteBuffer.slice();
        // 篡改这部分数据的值
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = byteBuffer.get();
            b *= 10;
            slice.put(i, b);
        }

        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());

        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.out.println(" -----------" + b);
        }

    }


}
