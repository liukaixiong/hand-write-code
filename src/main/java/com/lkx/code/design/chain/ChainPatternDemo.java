package com.lkx.code.design.chain;

import java.util.List;

import javax.annotation.PostConstruct;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;

public class ChainPatternDemo {

    // 自动注入各个责任链的对象
    private List<AbstractHandler> abstractHandleList;

    private AbstractHandler abstractHandler;

    // spring注入后自动执行，责任链的对象连接起来
    @PostConstruct
    public void initializeChainFilter() {

        for (int i = 0; i < abstractHandleList.size(); i++) {
            if (i == 0) {
                abstractHandler = abstractHandleList.get(0);
            } else {
                AbstractHandler currentHander = abstractHandleList.get(i - 1);
                AbstractHandler nextHander = abstractHandleList.get(i);
                currentHander.setNextHandler(nextHander);
            }
        }
    }

    // 直接调用这个方法使用
    public Response exec(Request request, Response response) {
        abstractHandler.filter(request, response);
        return response;
    }

    public AbstractHandler getAbstractHandler() {
        return abstractHandler;
    }

    public void setAbstractHandler(AbstractHandler abstractHandler) {
        this.abstractHandler = abstractHandler;
    }
}