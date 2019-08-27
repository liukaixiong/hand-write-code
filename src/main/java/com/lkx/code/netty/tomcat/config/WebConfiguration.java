package com.lkx.code.netty.tomcat.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * web的配置文件解析
 *
 * @author ： liukx
 * @time ： 2019/8/26 - 15:11
 */
public class WebConfiguration {

    private Properties webXml = new Properties();
    private Map<String, String> configMap = new HashMap<>();

    /**
     * 解析配置文件，并将解析完成的配置文件关系绑定到servletMapping中
     */
    public void init() {
        //加载web.xml文件,同时初始化 ServletMapping对象
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");

            webXml.load(fis);

            for (Object k : webXml.keySet()) {

                configMap.put(k.toString(), webXml.getProperty(k.toString()));
//                String key = k.toString();
//                if (key.endsWith(".url")) {
//                    String servletName = key.replaceAll("\\.url$", "");
//                    String url = webXml.getProperty(key);
//                    String className = webXml.getProperty(servletName + ".className");
//                    HttpServlet obj = (HttpServlet) Class.forName(className).newInstance();
//                    servletMapping.put(url, obj);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }
}
