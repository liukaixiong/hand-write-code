//package com.lkx.code.netty.factory;
//
//import org.jupiter.transport.JConnection;
//import org.jupiter.transport.JConnector;
//import org.jupiter.transport.UnresolvedAddress;
//import org.jupiter.transport.UnresolvedSocketAddress;
//import org.jupiter.transport.netty.JNettyTcpConnector;
//
///**
// * Netty连接抽象
// *
// * @author ： liukx
// * @time ： 2019/8/28 - 14:10
// */
//public class NettyConnector {
//
//    public static void main(String[] args) {
//        JConnector connectorRegister = new JNettyTcpConnector();
//        UnresolvedAddress address = new UnresolvedSocketAddress("localhost", 20001);
//        JConnection connection = (JConnection) connectorRegister.connect(address, false);
//        connectorRegister.connectionManager().manage(connection);
//
//    }
//
//
//}
