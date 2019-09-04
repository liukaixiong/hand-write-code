package com.lkx.code.netty.rpc.consumer;

import com.lkx.code.netty.rpc.api.IHelloService;
import com.lkx.code.netty.rpc.consumer.proxy.RpcProxy;

public class RpcConsumer {

    public static void main(String[] args) throws InterruptedException {
        RpcProxy rpc = new RpcProxy();
        IHelloService rpcHello = rpc.create(IHelloService.class);
        Thread.sleep(3000);
        System.out.println(rpcHello.sayHello("Tom老师"));

//        IRpcService service = RpcProxy.create(IRpcService.class);
//
//        System.out.println("8 + 2 = " + service.add(8, 2));
//        System.out.println("8 - 2 = " + service.sub(8, 2));
//        System.out.println("8 * 2 = " + service.mult(8, 2));
//        System.out.println("8 / 2 = " + service.div(8, 2));
    }

}
