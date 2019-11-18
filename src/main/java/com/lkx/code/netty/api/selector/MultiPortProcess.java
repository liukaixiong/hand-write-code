package com.lkx.code.netty.api.selector;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 这个案例是说明selector的使用
 * <p>
 * 多个端口监听，事件的触发以及selector的关系
 * <p>
 * <p>
 * 利用selector轮训端口触发的相应的事件
 *
 * @author ： liukx
 * @time ： 2019/11/15 - 10:36
 */
public class MultiPortProcess {
    public static void main(String[] args) throws Exception {
        int[] ports = new int[5];

        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; i++) {
            int port = ports[i];
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 配置是否非阻塞，默认true
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);
            // 将指定的channel注册到selectKey
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口 : " + port);
        }

        while (true) {
            int key = selector.select();
            System.out.println(" key : " + key);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println(" selectedKeys :" + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 为当前管道注册一个可读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println(" 客户端连接事件触发 : " + socketChannel);
                }
                // 判断新进来的事件是否是可读的
                else if (selectionKey.isReadable()) {
                    // 拿到客户端的连接管道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;

                    while (true) {
                        // 构建一个buffer进行读取
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        // 读取客户端传过来的数据
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0) {
                            // 如果读完了则终止
                            break;
                        }
                        // 如果没有读完，则进行翻转。
                        byteBuffer.flip();
                        // 回写到客户端
                        socketChannel.write(byteBuffer);
                        // 记录读取大小
                        byteRead += read;
                    }
                    System.out.println(" 读取 : " + byteRead + " , 来自于 : " + socketChannel);
                }
                // 获取完成之后需要先删除 ..
                iterator.remove();
//                selectionKeys.clear();
//                selectionKeys.clear();
            }
        }

    }
}
