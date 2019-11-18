package com.lkx.code.netty.api.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件操作
 *
 * @author ： liukx
 * @time ： 2019/11/13 - 16:08
 */
public class FileOperation {
    public static void main(String[] args) throws Exception {
        // 读取文件中的内容
        FileInputStream inputStream = new FileInputStream("input.txt");
        // 写入文件的对象
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        // Capacity = 512
        ByteBuffer buffer = ByteBuffer.allocate(512);

        while (true) {
            buffer.clear(); // 如果这行代码注释掉? ?
            /**
             * 假设第一次读的大小是13
             *  这时候read后的坐标 : P = 13 L = 13 C = 512
             *  经过一次flip之后 : P = 0 ; L = 13 ; C =512
             *  然后 write 之后 : P =13 ; L = 13 ; C =512
             * 第二个循环开始 :
             *  没有clear方法? P = 13 ; L =13 ;C = 512 这时候read是写不进去的！所以返回0，因为 P = L；
             *      然后回到上面的flip方法，其实这次没有读到文件的内容，而是残留在第一次读取的文件内容。所以无限循环的写。因为永远read不进去
             *  如果有clear方法 ： P = 0 ; L = C ; C = 512;
             *      这时候read是可以写东西进去的，写满了之后在经过flip方法再往文件里面写下一部分的内容。直到返回-1 文件读取完成了.
             * 第三个循环...
             */
            int read = inputChannel.read(buffer);
            System.out.println(" read : " + read);
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
