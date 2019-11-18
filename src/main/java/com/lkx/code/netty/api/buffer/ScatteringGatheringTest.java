package com.lkx.code.netty.api.buffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 关于Buffer的数据分层聚合
 *
 * @author ： liukx
 * @time ： 2019/11/14 - 15:10
 */
public class ScatteringGatheringTest {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();
        // 一直循环
        while (true) {

            int byteRead = 0;
            while (byteRead < messageLength) {
                // 一直读取客户端输入，直到达到messageLength长度结束
                long r = socketChannel.read(buffers);
                byteRead += r;
                System.out.println("byteRead : " + byteRead);
                Arrays.asList(buffers).stream().map(buffer -> "position:" + buffer.position() + ", limit : " + buffer.limit()).forEach(System.out::println);
            }
            // 翻转三个buffer
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.flip();
            });

            long byteWritten = 0;
            // 回写到客户端管道中
            while (byteWritten < messageLength) {
                long r = socketChannel.write(buffers);
                byteWritten += r;
            }

            // 将原有的buffer进行重置
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println(" byteRead : " + byteRead + " , bytesWritten : " + byteWritten + ", messageLength : " + messageLength);
        }

    }


}
