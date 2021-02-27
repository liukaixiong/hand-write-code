//给你一个字符串 s，找到 s 中最长的回文子串。 
//
// 
//
// 示例 1： 
//
// 
//输入：s = "babad"
//输出："bab"
//解释："aba" 同样是符合题意的答案。
// 
//
// 示例 2： 
//
// 
//输入：s = "cbbd"
//输出："bb"
// 
//
// 示例 3： 
//
// 
//输入：s = "a"
//输出："a"
// 
//
// 示例 4： 
//
// 
//输入：s = "ac"
//输出："a"
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
// 👍 3253 👎 0

package com.lkx.code.leetcode.leetcode.editor.cn;

import java.util.HashMap;
import java.util.Map;

class LongestPalindromicSubstring {
    public static void main(String[] args) {
        // aacabdkacaa
        Solution solution = new LongestPalindromicSubstring().new Solution();
        solution.longestPalindrome("aacabdkacaa");
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 暴力破解法
         *
         * @param s
         * @return
         */
        public String longestPalindrome(String s) {
            int len = s.length();

            if (len <= 1) {
                return s;
            }

            char[] chars = s.toCharArray();

            // 定义一个当前最大的回文长度变量
            int maxLength = 1;
            int start = 0;


            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    int currentLength = j - i + 1;
                    if (currentLength > maxLength && validPalindromic(chars, i, j)) {
                        // 如果匹配上的话,则重置变量
                        maxLength = currentLength;
                        start = i;
                    }
                }
            }

            return s.substring(start, start + maxLength);

        }

        /**
         * 验证子串 s[left..right] 是否为回文串
         */
        private boolean validPalindromic(char[] charArray, int left, int right) {
            while (right > left) {

                if (charArray[left] != charArray[right]) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}