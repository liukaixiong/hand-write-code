package com.lkx.code.netty.api.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接缓冲区案例
 *
 * @author ： liukx
 * @time ： 2019/11/14 - 10:39
 */
public class DirectBuffer {
    public static void main(String[] args) throws Exception {
        // 读取文件中的内容
        FileInputStream inputStream = new FileInputStream("input.txt");
        // 写入文件的对象
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        // 修改创建Buffer的方法
        //
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        // 返回一个byteBuffer，将一个byte数组封装成buffer
        // ByteBuffer buffer = ByteBuffer.wrap(byte);

        while (true) {
            // 清空buffer下标
            buffer.clear();
            int read = inputChannel.read(buffer);
            System.out.println(" read  : " + read);
            if (-1 == read) {
                break;
            }
            buffer.flip();
            outputChannel.write(buffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
