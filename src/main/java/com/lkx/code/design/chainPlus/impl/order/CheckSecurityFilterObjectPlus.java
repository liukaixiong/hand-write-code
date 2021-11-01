package com.lkx.code.design.chainPlus.impl.order;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.AbstractHandlerPlus;
import com.lkx.code.design.chainPlus.ChainConstants;

public class CheckSecurityFilterObjectPlus extends AbstractHandlerPlus {

    @Override
    public void doFilter(Request request, Response response) {
        // invoke Security check
        System.out.println("安全调用校验");
    }

    @Override
    public String type() {
        return ChainConstants.TYPE_ORDER;
    }
}