package com.lkx.code.leetcode.leetcode.editor.cn;
// 给你一个字符串 s，找到 s 中最长的回文子串。
//
//
//
// 示例 1：
//
//
// 输入：s = "babad"
// 输出："bab"
// 解释："aba" 同样是符合题意的答案。
//
//
// 示例 2：
//
//
// 输入：s = "cbbd"
// 输出："bb"
//
//
// 示例 3：
//
//
// 输入：s = "a"
// 输出："a"
//
//
// 示例 4：
//
//
// 输入：s = "ac"
// 输出："a"
//
//
//
//
// 提示：
//
//
// 1 <= s.length <= 1000
// s 仅由数字和英文字母（大写和/或小写）组成
//
// Related Topics 字符串 动态规划
// 👍 3256 👎 0

// Java：最长回文子串
class LongestPalindromicSubstring {
    public static void main(String[] args) {
        Solution solution = new LongestPalindromicSubstring().new Solution();
        String value = solution.longestPalindrome("abcbafds");
        System.out.println(value);
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 暴力破解法 参考值: abcbafds 结果 abcba 从左 ij开始【0，1】【0，2】【0，3】一个个开始比较如果遇到相同的i+1往右走,j-1往左走,继续比较直到比较结束 然后将长度和起始下标保存,为结果做截取
         *
         * @param s
         * @return
         */
        public String longestPalindrome(String s) {
            int len = s.length();
            if (s == null || len < 2) {
                return s;
            }
            // 构建一个最大长度的变量
            int maxLength = 1;
            // 最大长度的起始坐标
            int start = 0;
            char[] chars = s.toCharArray();

            // 构建两个循环，负责从左开始往右比较，遇到匹配的数据值然后进行回环匹配
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    // 得到当前j的下标与i的长度
                    int currentLength = j - i + 1;
                    // 一旦匹配上回文数,则重置变量下标
                    if (currentLength > maxLength && effectiveLoopBack(chars, i, j)) {
                        maxLength = currentLength;
                        start = i;
                    }
                }
            }
            return s.substring(start, start + maxLength);
        }

        /**
         * 是否符合回文规则
         *
         * @param chars
         * @param left
         * @param right
         * @return
         */
        private boolean effectiveLoopBack(char[] chars, int left, int right) {
            while (left < right) {
                // 如果有一位没有匹配上，则直接返回false
                if (chars[left] != chars[right]) {
                    return false;
                }
                // 如果匹配上了，则继续回文匹配
                left++; // i继续往右走
                right--; // j继续往左靠，直到快碰撞的时候while结束
            }
            return true;
        }

        public String longestPalindrome1(String s) {
            int len = s.length();
            if (len < 2) {
                return s;
            }
            int maxLen = 1;
            String res = s.substring(0, 1);
            // 中心位置枚举到 len - 2 即可
            for (int i = 0; i < len - 1; i++) {
                String oddStr = centerSpread(s, i, i);
                String evenStr = centerSpread(s, i, i + 1);
                String maxLenStr = oddStr.length() > evenStr.length() ? oddStr : evenStr;
                if (maxLenStr.length() > maxLen) {
                    maxLen = maxLenStr.length();
                    res = maxLenStr;
                }
            }
            return res;
        }

        private String centerSpread(String s, int left, int right) {
            // left = right 的时候，此时回文中心是一个字符，回文串的长度是奇数
            // right = left + 1 的时候，此时回文中心是一个空隙，回文串的长度是偶数
            int len = s.length();
            int i = left;
            int j = right;
            while (i >= 0 && j < len) {
                if (s.charAt(i) == s.charAt(j)) {
                    i--;
                    j++;
                } else {
                    break;
                }
            }
            // 这里要小心，跳出 while 循环时，恰好满足 s.charAt(i) != s.charAt(j)，因此不能取 i，不能取 j
            return s.substring(i + 1, j);
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)

}
