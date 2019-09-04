package com.lkx.code.netty.rpc.provider.impl;

import com.lkx.code.netty.rpc.api.IHelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * demo用例实现类
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 19:29
 */
public class HelloServiceImpl implements IHelloService {

    private Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String sayHello(String name) {
        logger.info(" 接收到数据 : " + name);
        return "hello : " + name;
    }
}
