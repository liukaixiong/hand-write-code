package com.lkx.code.leetcode.leetcode.editor.cn;
// 输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
//
// 例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。
//
//
//
// 示例：
//
//
// 给定一个链表: 1->2->3->4->5, 和 k = 2.
//
// 返回链表 4->5.
// Related Topics 链表 双指针
// 👍 187 👎 0

import java.util.ArrayList;
import java.util.List;

// Java：链表中倒数第k个节点
class LianBiaoZhongDaoShuDiKgeJieDianLcof {
    public static void main(String[] args) {
        Solution solution = new LianBiaoZhongDaoShuDiKgeJieDianLcof().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    /**
     * Definition for singly-linked list. public class ListNode { int val; ListNode next; ListNode(int x) { val = x; } }
     */

    class Solution {
        public ListNode getKthFromEnd(ListNode head, int k) {
            ListNode lastNode = head, headNode = head;

            for (int i = 0; i < k; i++) {
                // 先遍历到K的位置，比如10个数K=5 则先定位到0-5的位置。
                lastNode = lastNode.next;
            }

            while(lastNode != null){
                // 从5的位置继续遍历，循环的终止符
                lastNode = lastNode.next;
                // 这里则继续从0开始,这时候如果终止了,那么范围也就出来了.
                headNode = headNode.next;
            }

            return headNode;

        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
