package com.lkx.code.netty.api.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * nio的服务端
 * 测试方式 :
 * windows : 客户端开启多个CMD , telnet 8899 输入字符 , 查看控制台
 *
 * @author ： liukx
 * @time ： 2019/11/15 - 16:38
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Map<String, SocketChannel> socketChannelMap = new HashMap<>();
        // 相应的事件处理
        while (true) {
            int selectKey = selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach((selectionKey) -> {
                SocketChannel clientChannel = null;
                try {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        clientChannel = server.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        String channelKey = UUID.randomUUID().toString();
                        // 客户端注册完毕
                        socketChannelMap.put(channelKey, clientChannel);
                        System.out.println(" [注册消息] : " + channelKey + " - " + clientChannel);
                    } else if (selectionKey.isReadable()) { // 判断是否有新到来的数据
                        clientChannel = (SocketChannel) selectionKey.channel();
                        // 构建一个buffer进行读取
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                            byteBuffer.clear();
                        // 将客户端数据读取到buffer中
                        int count = clientChannel.read(byteBuffer);
                        // 表示读到了数据
                        if (count > 0) {
                            // 如果没有读完，则进行翻转。
                            byteBuffer.flip();
                            Charset defaultCharset = Charset.forName("UTF-8");
                            String receivedMessage = String.valueOf(defaultCharset.decode(byteBuffer).array());
                            System.out.println(clientChannel + " : " + receivedMessage);

                            // 定位到自己的UUID , 通过对应的管道
                            String currentUUID = "";
                            for (String key : socketChannelMap.keySet()) {
                                SocketChannel socketChannel = socketChannelMap.get(key);
                                if (socketChannel == clientChannel) {
                                    currentUUID = key;
                                }
                            }

                            // 给其他管道发送消息
                            for (String key : socketChannelMap.keySet()) {
                                SocketChannel socketChannel = socketChannelMap.get(key);
                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put((currentUUID + " : " + receivedMessage + "\r\n").getBytes());
                                // 每次读完了之后，记得翻转，重置坐标，然后才能写
                                writeBuffer.flip();
                                socketChannel.write(writeBuffer);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            // !!! 每次处理完成之后，记得清除本次的操作.
            selectionKeys.clear();


        }

    }


}
