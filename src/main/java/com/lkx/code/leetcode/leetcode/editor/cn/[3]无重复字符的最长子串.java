//给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。 
//
// 
//
// 示例 1: 
//
// 
//输入: s = " bb"
//输出: 3 
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
// 
//
// 示例 2: 
//
// 
//输入: s = "bbbbb"
//输出: 1
//解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
// 
//
// 示例 3: 
//
// 
//输入: s = "pwwkew"
//输出: 3
//解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
// 
//
// 示例 4: 
//
// 
//输入: s = ""
//输出: 0
// 
//
// 
//
// 提示： 
//
// 
// 0 <= s.length <= 5 * 104 
// s 由英文字母、数字、符号和空格组成 
// 
// Related Topics 哈希表 双指针 字符串 Sliding Window 
// 👍 4664 👎 0

package com.lkx.code.leetcode.leetcode.editor.cn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        Solution solution = new LongestSubstringWithoutRepeatingCharacters().new Solution();
        // dvdf
        print(solution, "pwwkew");
        print(solution, "dvdf");
        print(solution, "bbbbb");
    }

    private static void print(Solution solution, String text) {
        int pwwkew = solution.lengthOfLongestSubstring(text);
        System.out.println(pwwkew);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lengthOfLongestSubstring(String s) {

//            if (s == null || s.length() == 0) {
//                return 0;
//            }
//
//            int max = 0;
//
//            Map<Character, Integer> map = new HashMap<>();
//            int start = 0;
//            for (int i = 0; i < s.length(); i++) {
//                Character cur = s.charAt(i);
//                int index = map.getOrDefault(cur, -1);
//                if (start <= index) {
//                    int curMax = i - start;
//                    // 目前字符串长度最大值
//                    max = max > curMax ? max : curMax;
//
//                    // 表示要直接从重复的下一个开始.
//                    start = index + 1;
//                }
//
//                map.put(cur, i);
//            }
//
//            int endMax = s.length() - start;
//            max = max > endMax ? max : endMax;


            // pwwkew
            if (s == null || s.length() == 0) {
                return 0;
            }
            /**
             * 记录重复字符的下标,如果存在多个重复,则只取最后一位的下标
             */
            Map<Character, Integer> indexCache = new HashMap<>();
            // 长度最长的记录
            int max = 0;
            // 起始值,也就是开始的位置,如果遇到重复的则累加1
            int start = 0;

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                Integer cur = indexCache.getOrDefault(c, -1);
                // 如果找到的下标大于起始值
                if (start <= cur) {
                    // 这里就开始计算起始值和当前值的差距了,也就是得到了重复之后的长度了
                    int range = i - start;
                    // 如果当前长度范围大于max的话,则替换,否则继续下一个范围查找
                    max = range > max ? range : max;
                    // 这里很关键,是找到map中之前存在的下标开始计算!!!!
                    start = cur + 1;
                }
                indexCache.put(c, i);
            }
            // 处理循环到最后都没有重复的情况
            int endMax = s.length() - start;

            return max > endMax ? max : endMax;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}