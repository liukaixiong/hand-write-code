package com.lkx.code.design.chain;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.Ordered;

public abstract class AbstractHandler implements Ordered {

    // 责任链中的下一个对象
    private AbstractHandler nextHandler;

    @Override
    public int getOrder() {
        return LOW;
    }

    /**
     * 责任链的下一个对象
     */
    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 具体参数拦截逻辑,给子类去实现
     */
    public void filter(Request request, Response response) {
        doFilter(request, response);
        if (getNextHandler() != null) {
            getNextHandler().filter(request, response);
        }
    }

    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    protected abstract void doFilter(Request filterRequest, Response response);

}