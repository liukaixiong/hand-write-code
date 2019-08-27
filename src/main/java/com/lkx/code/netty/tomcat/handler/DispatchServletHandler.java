package com.lkx.code.netty.tomcat.handler;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lkx.code.netty.tomcat.definition.ServletDefinition;
import com.lkx.code.netty.tomcat.factory.ServletFactory;
import com.lkx.code.netty.tomcat.http.Request;
import com.lkx.code.netty.tomcat.http.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由转发映射servlet
 *
 * @author ： liukx
 * @time ： 2019/8/26 - 15:09
 */
public class DispatchServletHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(DispatchServletHandler.class);
    private ServletFactory servletFactory;

    public DispatchServletHandler(ServletFactory servletFactory) {
        this.servletFactory = servletFactory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            // 转交给我们自己的request实现
            Request request = new Request(ctx, req);
            // 转交给我们自己的response实现
            Response response = new Response(ctx, req);
            // 实际业务处理
            String url = request.getUrl();
            logger.info("URL : " + url + " 开始与servletMapping进行匹配  ");
            ServletDefinition servletDefinition = servletFactory.getServlet(url);
            String jsonString = getJSONString(req);
            if (servletDefinition != null) {
                logger.info(" 匹配成功！ 执行业务逻辑类:" + servletDefinition.toString());
                Class clazzServlet = servletDefinition.getServlet();
                Method method = servletDefinition.getMethod();
                Class requestClass = servletDefinition.getRequestClass();
                Object requestBody = JSON.parseObject(jsonString, requestClass);
                Object servlet = servletFactory.getObject(clazzServlet);
                Object responseBody = null;
                if (requestClass == null) {
                    responseBody = ReflectUtil.invoke(servlet, method, null);
                } else {
                    responseBody = ReflectUtil.invoke(servlet, method, requestBody);
                }
                logger.info(" 执行完成 .. " + responseBody);
                // 处理完成,将结果返回给客户端
                response.write(responseBody);
            } else {
                logger.info(" URL : " + url + " 匹配失败!");
                response.write("404 - Not Found");
            }
        }
    }

    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (fullHttpRequest.getMethod() == HttpMethod.POST) {
            String strContentType = fullHttpRequest.headers().get("Content-type").trim();
//            if (strContentType.contains("x-www-form-urlencoded")) {
            if (strContentType.contains("form")) {
                params = getFormParams(fullHttpRequest);
            } else if (strContentType.contains("application/json")) {
                try {
                    params = getJSONParams(fullHttpRequest);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            } else {
                return null;
            }
            return params;
        }
        return null;
    }

    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        // HttpPostMultipartRequestDecoder
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(strContent);
        for (String key : jsonObject.keySet()) {
            params.put(key, jsonObject.get(key));
        }
        return params;
    }

    private String getJSONString(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");
        return strContent;
    }
}
