package com.lkx.code.netty.tomcat.factory;

import com.lkx.code.netty.tomcat.definition.ServletDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 实例工厂
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 10:13
 */
public class ServletFactory {
    /**
     * 存放所有实例,类似于Spring的IOC
     */
    private Map<Class, Object> clazzMap = new HashMap<>();

    /**
     * 存储所有URL的关系
     */
    private Map<String, ServletDefinition> servletMap = new HashMap<>();

    public void addClass(Class clazz, Object obj) {
        Object oldObj = clazzMap.get(clazz);
        if (oldObj != null) {
            return;
        }

        clazzMap.put(clazz, obj);
    }

    public Object getObject(Class clazz) {
        return clazzMap.get(clazz);
    }

    public void addServlet(String url, ServletDefinition servletDefinition) {
        if (StringUtils.isNotEmpty(url)) {
            ServletDefinition servlet = servletMap.get(url);
            if (servlet != null) {
                return;
            } else {
                servletMap.put(url, servletDefinition);
            }
        }
    }

    public ServletDefinition getServlet(String url) {
        return servletMap.get(url);
    }
}
