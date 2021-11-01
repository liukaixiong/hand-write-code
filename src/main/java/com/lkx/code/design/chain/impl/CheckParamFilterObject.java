package com.lkx.code.design.chain.impl;

import com.lkx.code.design.chain.AbstractHandler;
import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;

public class CheckParamFilterObject extends AbstractHandler {

    @Override
    public void doFilter(Request request, Response response) {
        System.out.println("非空参数检查");
    }

}