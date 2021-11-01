package com.lkx.code.design.chain.impl;

import com.lkx.code.design.chain.AbstractHandler;
import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;

public class CheckRuleFilterObject extends AbstractHandler {

    @Override
    public void doFilter(Request request, Response response) {
        // check rule
        System.out.println("check rule");
    }
}