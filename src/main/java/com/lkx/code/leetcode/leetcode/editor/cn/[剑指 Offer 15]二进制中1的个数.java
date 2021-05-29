package com.lkx.code.leetcode.leetcode.editor.cn;
// 请实现一个函数，输入一个整数（以二进制串形式），输出该数二进制表示中 1 的个数。例如，把 9 表示成二进制是 1001，有 2 位是 1。因此，如果输入
// 9，则该函数输出 2。
//
//
//
// 示例 1：
//
//
// 输入：00000000000000000000000000001011
// 输出：3
// 解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。
//
//
// 示例 2：
//
//
// 输入：00000000000000000000000010000000
// 输出：1
// 解释：输入的二进制串 00000000000000000000000010000000 中，共有一位为 '1'。
//
//
// 示例 3：
//
//
// 输入：11111111111111111111111111111101
// 输出：31
// 解释：输入的二进制串 11111111111111111111111111111101 中，共有 31 位为 '1'。
//
//
//
// 提示：
//
//
// 输入必须是长度为 32 的 二进制串 。
//
//
//
//
// 注意：本题与主站 191 题相同：https://leetcode-cn.com/problems/number-of-1-bits/
// Related Topics 位运算
// 👍 103 👎 0

// Java：二进制中1的个数
class ErJinZhiZhong1deGeShuLcof {
    public static void main(String[] args) {
        Solution solution = new ErJinZhiZhong1deGeShuLcof().new Solution();
        int i = solution.hammingWeight(1111);
        System.out.println("result : "+i);
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    public class Solution {
        // you need to treat n as an unsigned value
        public int hammingWeight(int n) {
            // * 值 : 00000000000000000000000000001011
            // n & 1: 00000000000000000000000000001011 & 1
            // 0000000000000000000000000000101 1
            // 0000000000000000000000000000000 1
            // 0000000000000000000000000000000 1

            /**
             * 每次都和1做与运算，判断二进制的最后一位是否为1，为1则res累加，非1也不会动
             * 然后累加完了，从右往左移动一位，就相当于32位变成31位，一直循环直到最后一位；
             */
            int res = 0;
            /*while (n != 0) {
                res += n & 1;
                n = n >>> 1;
                System.out.println(n);
            }*/

            /**
             * n-1的思路就是: 每次与n的小于n-1去与运算,相当于直接过滤掉为0的计算
             * ----------------------- 1111
             * a = 10001010111
             * b = 10001010110
             * c = 10001010110
             * ------------------------ 1110
             * a = 10001010110
             * b = 10001010101
             * c = 10001010100
             * ------------------------ 1108
             * a = 10001010100
             * b = 10001010011
             * c = 10001010000
             * ----------------------- 1104
             * 其实你会发现，n的1位会越来越少，每次运算都会少一次；
             * 循环的意义就是你这个值里面有多少个1就会循环多少次，
             */
            while (n != 0) {
                res++;
                //System.out.println("a = "+Integer.toBinaryString(n));
                //System.out.println("b = "+Integer.toBinaryString(n-1));
                n = n & (n - 1);
                //System.out.println("c = "+Integer.toBinaryString(n));

                //System.out.println("-------"+n);
            }

            // Integer.bitCount(n);
            // Integer.toBinaryString(n).replaceAll("0","").length();
            return res;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
