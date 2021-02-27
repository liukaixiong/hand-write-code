package com.lkx.code.jdk.juc.list;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Module 并发List测试
 * @Description 写入时复制
 * @Author liukaixiong
 * @Date 2020/11/26 11:26
 */
public class CopyOrWriteTest {

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<String>();
        list.add("asdaf");
        list.get(0);
    }


}
