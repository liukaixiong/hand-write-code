package com.lkx.code.netty.rpc.api;

import com.lkx.code.netty.rpc.anno.RpcService;

@RpcService(group = "provider")
public interface IHelloService {

    public String sayHello(String name);

}
