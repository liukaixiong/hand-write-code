package com.lkx.code.netty.api.channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端编写
 *
 * @author ： liukx
 * @time ： 2019/11/18 - 14:19
 */
public class NioClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                // 是否建立连接
                if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((LocalDateTime.now() + " 连接成功\r\n").getBytes());
                        writeBuffer.flip();
                        channel.write(writeBuffer);

                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                        executorService.submit(() -> {
                            while (true) {
                                writeBuffer.clear();
                                InputStreamReader input = new InputStreamReader(System.in);
                                BufferedReader br = new BufferedReader(input);
                                String sendMessage = br.readLine();
                                writeBuffer.put(sendMessage.getBytes());
                                writeBuffer.flip();
                                channel.write(writeBuffer);
                            }
                        });
                    }
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel server = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    int count = server.read(readBuffer);
                    if (count > 0) {
                        // 读取到服务端发送过来的内容
                        String msg = new String(readBuffer.array(), 0, count);
                        System.out.println("[服务端] : " + msg);
                    }
                }
            }
            // 处理完成之后 , 记得关闭
            selectionKeys.clear();
        }

    }
}
