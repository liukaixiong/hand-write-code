package com.lkx.code.design.chainPlus;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.impl.flow.FlowCheckIpBlackHandler;
import com.lkx.code.design.chainPlus.impl.flow.FlowCheckServiceHandler;
import com.lkx.code.design.chainPlus.impl.flow.FlowCheckTokenHandler;
import com.lkx.code.design.chainPlus.impl.flow.FlowPathHandler;
import com.lkx.code.design.chainPlus.impl.order.CheckBlackFilterObjectPlus;
import com.lkx.code.design.chainPlus.impl.order.CheckParamFilterObjectPlus;
import com.lkx.code.design.chainPlus.impl.order.CheckRuleFilterObjectPlus;
import com.lkx.code.design.chainPlus.impl.order.CheckSecurityFilterObjectPlus;

public class ChainPatternDemoPlus {

    // 自动注入各个责任链的对象
    private List<AbstractHandlerPlus> abstractHandleList;

    private Map<String, AbstractHandlerPlus> abstractHandlerMap;

    // spring注入后自动执行，责任链的对象连接起来
    @PostConstruct
    public void initializeChainFilter() {
        abstractHandlerMap = new HashMap<>();
        Map<String, List<AbstractHandlerPlus>> handlerMap = abstractHandleList.stream()
            .sorted(Comparator.comparing(Ordered::getOrder)).collect(Collectors.groupingBy(AbstractHandlerPlus::type));

        handlerMap.forEach((type, abstractHandleList) -> {
            AbstractHandlerPlus abstractHandler = null;
            for (int i = 0; i < abstractHandleList.size(); i++) {
                if (i == 0) {
                    abstractHandler = abstractHandleList.get(0);
                } else {
                    AbstractHandlerPlus currentHandler = abstractHandleList.get(i - 1);
                    AbstractHandlerPlus nextHandler = abstractHandleList.get(i);
                    currentHandler.setNextHandler(nextHandler);
                }
            }
            abstractHandlerMap.put(type, abstractHandler);
        });

    }

    // 直接调用这个方法使用
    public Response exec(String type, Request request, Response response) {
        AbstractHandlerPlus abstractHandlerPlus = abstractHandlerMap.get(type);
        abstractHandlerPlus.filter(request, response);
        return response;
    }

    public void setAbstractHandleList(List<AbstractHandlerPlus> abstractHandleList) {
        this.abstractHandleList = abstractHandleList;
    }

    public static void main(String[] args) {
        ChainPatternDemoPlus chainPatternDemoPlus = new ChainPatternDemoPlus();
        List<AbstractHandlerPlus> abstractHandlerPluses = new ArrayList<>();
        abstractHandlerPluses.add(new CheckBlackFilterObjectPlus());
        abstractHandlerPluses.add(new CheckParamFilterObjectPlus());
        abstractHandlerPluses.add(new CheckRuleFilterObjectPlus());
        abstractHandlerPluses.add(new CheckSecurityFilterObjectPlus());
        abstractHandlerPluses.add(new FlowPathHandler());
        abstractHandlerPluses.add(new FlowCheckServiceHandler());
        abstractHandlerPluses.add(new FlowCheckIpBlackHandler());
        abstractHandlerPluses.add(new FlowCheckTokenHandler());

        chainPatternDemoPlus.setAbstractHandleList(abstractHandlerPluses);

        // 初始化数据结构
        chainPatternDemoPlus.initializeChainFilter();

        System.out.println(" 订单 ------>>>>>>");

        // 执行订单责任链
        chainPatternDemoPlus.exec(ChainConstants.TYPE_ORDER, new Request(), new Response());

        System.out.println(" 流量 ------>>>>>>");

        // 执行流量责任链
        chainPatternDemoPlus.exec(ChainConstants.TYPE_FLOW, new Request(), new Response());

    }
}