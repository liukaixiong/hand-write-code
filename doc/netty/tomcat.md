# 手写Netty版本的简易tomcat以及实现部分SpringMVC功能

## 实现思路

1. 首先加载配置文件。
2. 将配置文件中的扫描包进行位置扫描
   1. 解析类结构
   2. 根据注解将URL以及实例信息进行关联
3. 构建一个Netty的服务端，负责阻塞接收客户端传递过来的请求。
4. 服务端接收到的请求需要解析成对应的Request。
5. 根据得到解析的Request信息中的URL去匹配第一步加载到的Servlet，反射触发，执行后续的业务逻辑。
6. 处理完业务逻辑之后，将返回结果封装成Response。
7. 返回给客户端。



这里一共有几个关键步骤:

	1. 服务端接收请求		->		NettyTomcatServer
 	2. 将请求解析成Request。->  Request
 	3. 根据Request转发Servlet。-> DispatchServletHandler
 	4. 处理业务之后，将返回值转换成Response。 -> Response
 	5. 返回客户端

## 代码实现

**路径** : com.lkx.code.netty.tomcat

**启动类:** NettyTomcatServer

**模拟路由:** 

```http
GET - http://localhost:8080/get/query
POST - http://localhost:8080/post/query
{
	"name":"lkx"
}
```



### 解析配置文件

**WebConfiguration**

```java
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
```



启动服务监听，来接收客户端请求。

**NettyTomcatServer**

```java
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
```

由于使用了Netty的HttpRequestDecoder进行解码，所以解析Request请求就省略了。

实际上就是读到客户端传递过来的流解析成文本，然后根据scan扫描到的类信息得到入参class，然后通过json转换成入参class，后续反射调用。



然后将请求通过handler进行转发给业务类。

**DispatchServletHandler**

```java
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
            // 处理完成，将结果返回给客户端。
            response.write(responseBody);
        } else {
            logger.info(" URL : " + url + " 匹配失败!");
            response.write("404 - Not Found");
        }
    }
}
```

业务处理完成了之后，将返回值进行包装，返回给客户端

**Response**

```java
public void write(String out) throws Exception {
        try {
            if (out == null || out.length() == 0) {
                return;
            }
            // 设置 http协议及请求头信息
            FullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html;");
            ctx.write(response);
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
```

另外仿照SpringMVC的Controller

**PostServlet**

```java
@Controller
@RequestMapping("/post")
public class PostServlet {


    private Logger logger = LoggerFactory.getLogger(PostServlet.class);

    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String, String> query(JSONObject jsonObject) {
        logger.info(" 获取到的数据 : " + jsonObject.toJSONString());

        Map<String, String> map = new HashMap<>();
        map.put("success", "true");
        map.put("date", DateUtil.formatDateTime(new Date()));
        return map;

    }

}
```

[以上详细代码地址](https://github.com/liukaixiong/hand-write-code)

>  仅供学习练手，如有错误，请指正！共同进步。

**案例参考咕泡学院-tom老师的Netty实战**
[咕泡学院地址](https://www.gupaoedu.com/)

