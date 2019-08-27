package com.lkx.code.netty.tomcat.definition;

import com.lkx.code.netty.tomcat.anno.RequestMethod;

import java.lang.reflect.Method;

/**
 * server定义
 *
 * @author ： liukx
 * @time ： 2019/8/26 - 17:43
 */
public class ServletDefinition {

    private Class requestClass;
    private Class responseClass;
    private String url;
    private Class servlet;
    private Method method;
    private RequestMethod requestMethod;

    public ServletDefinition() {
    }

    public ServletDefinition(Class requestClass, Class responseClass, String url, Class servlet,
                             RequestMethod requestMethod, Method method) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.url = url;
        this.servlet = servlet;
        this.requestMethod = requestMethod;
        this.method = method;
    }

    public Class getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class requestClass) {
        this.requestClass = requestClass;
    }

    public void setServlet(Class servlet) {
        this.servlet = servlet;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Class getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class responseClass) {
        this.responseClass = responseClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class getServlet() {
        return servlet;
    }
}
