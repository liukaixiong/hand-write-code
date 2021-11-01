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
public class FlowCheckIpBlackHandler extends AbstractHandlerPlus {

    @Override
    public String type() {
        return ChainConstants.TYPE_FLOW;
    }

    @Override
    public void doFilter(Request filterRequest, Response response) {
        System.out.println("3. 校验请求是否黑名单");
    }
}
