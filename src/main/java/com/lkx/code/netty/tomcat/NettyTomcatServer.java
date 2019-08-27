package com.lkx.code.netty.tomcat;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.lkx.code.netty.tomcat.anno.Controller;
import com.lkx.code.netty.tomcat.anno.RequestMapping;
import com.lkx.code.netty.tomcat.anno.RequestMethod;
import com.lkx.code.netty.tomcat.config.WebConfiguration;
import com.lkx.code.netty.tomcat.definition.ServletDefinition;
import com.lkx.code.netty.tomcat.factory.ServletFactory;
import com.lkx.code.netty.tomcat.handler.DispatchServletHandler;
import com.lkx.code.netty.tomcat.utils.DefinitionUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Netty版本的简易版tomcat.
 *
 * @author ： liukx
 * @time ： 2019/8/26 - 14:57
 */
public class NettyTomcatServer {

    private Logger logger = LoggerFactory.getLogger(NettyTomcatServer.class);

    private int port = 8080;
    /**
     * 配置解析对象
     */
    private WebConfiguration webConfiguration;

    private ServletFactory servletFactory;

    public NettyTomcatServer() {
        this.servletFactory = new ServletFactory();
        webConfiguration = new WebConfiguration();
    }

    /**
     * 开启Netty的服务端，负责接收请求数据
     */
    public void start() throws IOException {

        // 1. 解析配置
        webConfiguration.init();
        // 2. 根据配置扫描servlet,并注册
        scan();
        // 3. 启动服务
        startServer();
    }

    private void startServer() {
        // 1. 开启Boss主线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 2. 开启worker工作线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 定义一个服务端启动类
            ServerBootstrap server = new ServerBootstrap();
            // 分配主子线程
            server.group(bossGroup, workerGroup)
                    // 指定管道类
                    .channel(NioServerSocketChannel.class)
                    // 构建处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            logger.info(" 解析 Request 对象");
                            client.pipeline().addLast(new HttpRequestDecoder());
                            client.pipeline().addLast("http-aggregator",
                                    new HttpObjectAggregator(65536));
                            // 目的是将多个消息转换为单一的request或者response对象
                            logger.info(" 解析 Response 对象");
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // 最终将会被这个handler执行到
                            logger.info(" 执行DispatchServletHandler进行路由转发 ... ");
                            client.pipeline().addLast(new DispatchServletHandler(servletFactory));
                        }
                    });
            ChannelFuture channelFuture = server.bind(port).sync();
            logger.info(" tomcat 已经启动了 , 端口 : " + port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 扫描指定注解，并且将对应的servlet进行注册
     *
     * @throws IOException
     */
    private void scan() throws IOException {
        // 扫描需要配置的类
        Map<String, String> configMap = webConfiguration.getConfigMap();
        String scan = configMap.get("netty.server.servlet.scan");
        logger.info(" 开始扫描servlet包路径:" + scan);
        List<Class> classList = DefinitionUtils.scan(scan);
        for (int i = 0; i < classList.size(); i++) {
            Class clazz = classList.get(i);
            Controller controllerAnno = AnnotationUtil.getAnnotation(clazz, Controller.class);
            if (controllerAnno != null) {
                StringBuffer sb = new StringBuffer();
                RequestMapping requestMappingAnnotation = AnnotationUtil.getAnnotation(clazz, RequestMapping.class);
                if (requestMappingAnnotation != null && requestMappingAnnotation.value() != null) {
                    sb.append(requestMappingAnnotation.value()[0]);
                }
                Method[] methodList = ClassUtil.getDeclaredMethods(clazz);
                for (int j = 0; j < methodList.length; j++) {
                    Method methods = methodList[j];
                    RequestMapping methodRequestMappingAnno = AnnotationUtil.getAnnotation(methods,
                            RequestMapping.class);
                    if (methodRequestMappingAnno != null && methodRequestMappingAnno.value() != null) {
                        String[] value = methodRequestMappingAnno.value();
                        sb.append(value[0]);
                        RequestMethod[] requestMethod = methodRequestMappingAnno.method();
                        Class<?> returnType = methods.getReturnType();
                        Class<?> parameterType = null;
                        if (methods.getParameterTypes().length > 0) {
                            // 这里默认只取第一个对象
                            parameterType = methods.getParameterTypes()[0];
                        }
                        String url = sb.toString().replaceAll("//", "/");
                        Object obj = ReflectUtil.newInstance(clazz);
                        ServletDefinition servletDefinition = new ServletDefinition(parameterType, returnType, url,
                                clazz,
                                requestMethod[0], methods);
                        servletFactory.addClass(clazz, obj);
                        servletFactory.addServlet(url, servletDefinition);
                        logger.info(" 注册路由 : " + url + " 处理servlet : " + clazz.toString());
                    }
                }
            }

        }

    }

    public static void main(String[] args) throws IOException {
        NettyTomcatServer nettyTomcatServer = new NettyTomcatServer();
        nettyTomcatServer.start();
    }

}
