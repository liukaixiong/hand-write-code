package com.lkx.code.netty.tomcat.servlet;

import com.lkx.code.netty.tomcat.anno.Controller;
import com.lkx.code.netty.tomcat.anno.RequestMapping;
import com.lkx.code.netty.tomcat.anno.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/get")
public class GetServlet {

    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Map<String, String> get() {
        Map<String, String> data = new HashMap<>();
        data.put("success", "true");
        data.put("message", "get is success!");
        return data;
    }

}
