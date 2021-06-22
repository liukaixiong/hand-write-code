package com.lkx.code.leetcode;

import com.alibaba.fastjson.JSON;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        String s = JSON.toJSONString(this);
        System.out.println(s);
        return super.toString();
    }
}