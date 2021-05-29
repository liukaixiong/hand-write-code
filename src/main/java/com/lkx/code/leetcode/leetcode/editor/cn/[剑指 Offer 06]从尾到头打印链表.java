package com.lkx.code.leetcode.leetcode.editor.cn;
// 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
//
//
//
// 示例 1：
//
// 输入：head = [1,3,2]
// 输出：[2,3,1]
//
//
//
// 限制：
//
// 0 <= 链表长度 <= 10000
// Related Topics 链表
// 👍 135 👎 0

import java.io.Serializable;
import java.util.Stack;

// Java：从尾到头打印链表
class CongWeiDaoTouDaYinLianBiaoLcof {
    public static void main(String[] args) {
        Solution solution = new CongWeiDaoTouDaYinLianBiaoLcof().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode(int x) { val = x; } }
     */
    public class ListNode {
        int val;
        ListNode next;

        public ListNode() {
        }

        ListNode(int x) {
            val = x;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class Solution {

        public int[] reversePrint(ListNode head) {
            // 方法1 是用栈的方式打印
            Stack<ListNode> nodeStack = new Stack<>();

            while (head != null) {
                nodeStack.push(head);
                head = head.next;
            }

            int size = nodeStack.size();
            int[] printSize = new int[size];
            for (int i = 0; i < size; i++) {
                ListNode pop = nodeStack.pop();
                printSize[i] = pop.val;
            }
            return printSize;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
