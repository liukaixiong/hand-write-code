package com.lkx.code.design.chainPlus.impl.order;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.AbstractHandlerPlus;
import com.lkx.code.design.chainPlus.ChainConstants;

public class CheckBlackFilterObjectPlus extends AbstractHandlerPlus {

    @Override
    public void doFilter(Request request, Response response) {
        // invoke black list check
        System.out.println("校验黑名单");
    }

    @Override
    public String type() {
        return ChainConstants.TYPE_ORDER;
    }
}