package com.lkx.code.design.chainPlus.impl.order;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.AbstractHandlerPlus;
import com.lkx.code.design.chainPlus.ChainConstants;

public class CheckParamFilterObjectPlus extends AbstractHandlerPlus {

    @Override
    public void doFilter(Request request, Response response) {
        System.out.println("非空参数检查");
    }

    @Override
    public String type() {
        return ChainConstants.TYPE_ORDER;
    }
}