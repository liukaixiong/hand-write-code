package com.lkx.code.suanfa;

import com.lkx.code.leetcode.ListNode;

public class LinkedSolution {

    /**
     * 将数组转化成链表
     * 
     * @param nums
     * @return
     */
    public static ListNode builderListNode(int... nums) {
        ListNode root = new ListNode(nums[0]);
        ListNode currentNode = root;

        for (int i = 1; i < nums.length; i++) {
            ListNode next = new ListNode(nums[i]);
            currentNode.next = next;
            currentNode = next;
        }
        return root;
    }

    /**
     * 将链表进行翻转
     * 
     * @param listNode
     * @return
     */
    public static ListNode reverseListNode(ListNode listNode) {
        if (listNode == null) {
            return null;
        }
        ListNode head = null;
        ListNode cur = listNode;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = head;
            head = cur;
            cur = next;
        }

        return head;
    }

    public static void main(String[] args) {
        ListNode listNode = builderListNode(1, 2, 3, 4, 5);
        System.out.println(reverseListNode(listNode));
    }

}
