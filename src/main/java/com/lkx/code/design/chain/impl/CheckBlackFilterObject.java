package com.lkx.code.design.chain.impl;

import com.lkx.code.design.chain.AbstractHandler;
import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;

public class CheckBlackFilterObject extends AbstractHandler {

    @Override
    public void doFilter(Request request, Response response) {
        // invoke black list check
        System.out.println("校验黑名单");
    }

}