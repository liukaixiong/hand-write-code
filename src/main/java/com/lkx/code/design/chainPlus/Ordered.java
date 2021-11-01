package com.lkx.code.design.chainPlus;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/1 - 13:53
 */
public interface Ordered {

    int LOW = Integer.MIN_VALUE;

    int HIGHT = Integer.MAX_VALUE;

    int getOrder();

}
