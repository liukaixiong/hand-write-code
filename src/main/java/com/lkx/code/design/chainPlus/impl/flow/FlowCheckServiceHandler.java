package com.lkx.code.design.chainPlus.impl.flow;

import com.lkx.code.design.chain.model.Request;
import com.lkx.code.design.chain.model.Response;
import com.lkx.code.design.chainPlus.AbstractHandlerPlus;
import com.lkx.code.design.chainPlus.ChainConstants;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/1 - 14:08
 */
public class FlowCheckServiceHandler extends AbstractHandlerPlus {

    @Override
    public String type() {
        return ChainConstants.TYPE_FLOW;
    }

    @Override
    public void doFilter(Request filterRequest, Response response) {
        System.out.println("2. 校验服务是否存在");
    }
}
