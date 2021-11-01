package com.lkx.code.design.chain.impl;

import com.lkx.code.design.chain.AbstractHandler;
import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;

public class CheckSecurityFilterObject extends AbstractHandler {

    @Override
    public void doFilter(Request request, Response response) {
        // invoke Security check
        System.out.println("安全调用校验");
    }
}