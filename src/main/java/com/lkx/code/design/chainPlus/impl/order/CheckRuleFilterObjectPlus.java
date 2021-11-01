package com.lkx.code.design.chainPlus.impl.order;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.AbstractHandlerPlus;
import com.lkx.code.design.chainPlus.ChainConstants;

public class CheckRuleFilterObjectPlus extends AbstractHandlerPlus {

    @Override
    public void doFilter(Request request, Response response) {
        // check rule
        System.out.println("check rule");
    }

    @Override
    public String type() {
        return ChainConstants.TYPE_ORDER;
    }
}