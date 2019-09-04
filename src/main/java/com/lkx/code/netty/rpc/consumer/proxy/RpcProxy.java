package com.lkx.code.netty.rpc.consumer.proxy;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.RandomUtil;
import com.lkx.code.netty.rpc.anno.RpcService;
import com.lkx.code.netty.rpc.factory.ClientNettyFactory;
import com.lkx.code.netty.rpc.handler.ResultChannelHandler;
import com.lkx.code.netty.rpc.protocol.InvokerProtocol;
import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
import com.lkx.code.netty.rpc.registry.utils.RegistyConstans;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcProxy {

    /**
     * 项目和远程地址绑定
     */
    private Map<String, List<RegistryRemoteData>> remoteMap = new ConcurrentHashMap<>();

    /**
     * 类与项目绑定
     */
    private Map<String, String> projectMap = new ConcurrentHashMap<>();

    protected static ClientNettyFactory clientNettyFactory;

    public RpcProxy() {
        clientNettyFactory = new ClientNettyFactory(remoteMap);
    }

    public <T> T create(Class<?> clazz) {
        RpcService clazzAnno = AnnotationUtil.getAnnotation(clazz, RpcService.class);
        if (clazzAnno == null) {
            return null;
        }
        String group = clazzAnno.group();
        projectMap.put(clazz.getName(), group);
        //clazz传进来本身就是interface
        MethodProxy proxy = new MethodProxy(clazz);
        Class<?>[] interfaces = clazz.isInterface() ?
                new Class[]{clazz} :
                clazz.getInterfaces();
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, proxy);


        // 向远程去获取地址
        RegistryRemoteData remoteData = new RegistryRemoteData();
        remoteData.setType(RegistyConstans.HEART_LIST_REQUEST);
        remoteData.setProject(group);
        // 首先得从注册中心拿到可用的服务资源列表
        Channel connector = clientNettyFactory.connectorRegister("127.0.0.1", 8888);
        if (connector.isWritable()) {
            try {
                connector.writeAndFlush(remoteData).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(" 不可以写了..");
        }
        return result;
    }

    public Map<String, List<RegistryRemoteData>> getRemoteMap() {
        return remoteMap;
    }

    public Map<String, String> getProjectMap() {
        return projectMap;
    }


    private class MethodProxy implements InvocationHandler {
        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果传进来是一个已实现的具体类（本次演示略过此逻辑)
            if (Object.class.equals(method.getDeclaringClass())) {
                try {
                    return method.invoke(this, args);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                //如果传进来的是一个接口（核心)
            } else {
                return rpcInvoke(proxy, method, args);
            }
            return null;
        }


        /**
         * 实现接口的核心方法
         *
         * @param method
         * @param args
         * @return
         */
        public Object rpcInvoke(Object proxy, Method method, Object[] args) {
            String className = clazz.getName();
            String methodName = method.getName();
            String project = projectMap.get(className);

            List<RegistryRemoteData> rmd = remoteMap.get(project);
            RegistryRemoteData registryRemoteData = null;
            if (rmd != null) {
                if (rmd.size() == 1) {
                    registryRemoteData = rmd.get(0);
                } else {
                    int selectIndex = RandomUtil.randomInt(0, rmd.size() - 1);
                    registryRemoteData = rmd.get(selectIndex);
                }
            } else {
                System.out.println(" 远程上面没有服务 ... " + project);
                return null;
            }
            ResultChannelHandler resultChannelHandler = new ResultChannelHandler();
            InvokerProtocol invokerProtocol = new InvokerProtocol();
            invokerProtocol.setClassName(className);
            invokerProtocol.setMethodName(methodName);
            invokerProtocol.setParames(method.getParameterTypes());
            invokerProtocol.setValues(args);
            String ip = registryRemoteData.getIp();
            int port = registryRemoteData.getPort();
            Channel connector = clientNettyFactory.connectorClient(ip, port, resultChannelHandler);
            try {
                connector.writeAndFlush(invokerProtocol).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 然后再进行协议封装
            return resultChannelHandler.getObject();
        }

        ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    future.channel().pipeline().fireExceptionCaught(future.cause());
                }
            }
        };
    }
}



