package com.lkx.code.netty.api.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件对象
 * 直接操作内存中的对象,即可改变到文件中。
 *
 * @author ： liukx
 * @time ： 2019/11/14 - 14:53
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("file/nioTest.txt", "rw");
        // 文件管道对象
        FileChannel fileChannel = randomAccessFile.getChannel();
        // 第一个参数表示映射模型 : 读还是写?
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        // 操作文件
        mappedByteBuffer.put(0, (byte) 'g');
        mappedByteBuffer.put(3, (byte) 'u');
        // 上面直接操作文件，没有定义任何的输入输出流就可以直接操作文件
        // 相当于将文件和内存进行映射,直接修改内存数据即可映射到文件中。
        System.out.println(" 注意要从文件管理器中找到这个文件打开才能看到效果，直接在编辑器中可能看不到。");

        randomAccessFile.close();
    }


}
