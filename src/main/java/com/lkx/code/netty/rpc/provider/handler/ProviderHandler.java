package com.lkx.code.netty.rpc.provider.handler;

import cn.hutool.core.util.ReflectUtil;
import com.lkx.code.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * 生产者的handler处理器
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 19:36
 */
@ChannelHandler.Sharable
public class ProviderHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> beanMap;

    public ProviderHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InvokerProtocol) {
            InvokerProtocol invokeProtocol = (InvokerProtocol) msg;
            String className = invokeProtocol.getClassName();
            String methodName = invokeProtocol.getMethodName();
            Object[] values = invokeProtocol.getValues();
            Object bean = beanMap.get(className);
            Object result = ReflectUtil.invoke(bean, methodName, values);
            ctx.writeAndFlush(result);
            ctx.close();
        } else {
            System.out.println(" 有问题 ... ");
        }

    }
}
