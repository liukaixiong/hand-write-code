package com.lkx.code.netty.tomcat.servlet;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.lkx.code.netty.tomcat.anno.Controller;
import com.lkx.code.netty.tomcat.anno.RequestMapping;
import com.lkx.code.netty.tomcat.anno.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
