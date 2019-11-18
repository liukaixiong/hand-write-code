package com.lkx.code.netty.api.buffer;

import java.nio.ByteBuffer;

/**
 * buffer的类型 , 适用场景 : 编解码
 *
 * @author ： liukx
 * @time ： 2019/11/13 - 17:04
 */
public class TypeBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(1);
        byteBuffer.putLong(100000000l);
        byteBuffer.putChar('l');

        byteBuffer.flip();

        System.out.println(" 开始获取 ");

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());


    }
}
