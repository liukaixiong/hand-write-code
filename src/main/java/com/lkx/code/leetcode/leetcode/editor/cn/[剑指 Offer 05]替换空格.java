package com.lkx.code.leetcode.leetcode.editor.cn;
// 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
//
//
//
// 示例 1：
//
// 输入：s = "We are happy."
// 输出："We%20are%20happy."
//
//
//
// 限制：
//
// 0 <= s 的长度 <= 10000
// 👍 98 👎 0

// Java：替换空格
class TiHuanKongGeLcof {
    public static void main(String[] args) {
        Solution solution = new TiHuanKongGeLcof().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String replaceSpace(String s) {
            // 第一种方案
//             return s.replaceAll(" ","%20");

            // 第二种方案，通过遍历的方式
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    stringBuilder.append("%20");
                } else {
                    stringBuilder.append(s.charAt(i));
                }
            }
            return stringBuilder.toString();
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
