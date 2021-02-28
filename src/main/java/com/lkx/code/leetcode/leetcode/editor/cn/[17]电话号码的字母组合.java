package com.lkx.code.leetcode.leetcode.editor.cn;
//给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。 
//
// 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。 
//
// 
//
// 
//
// 示例 1： 
//
// 
//输入：digits = "23"
//输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
// 
//
// 示例 2： 
//
// 
//输入：digits = ""
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：digits = "2"
//输出：["a","b","c"]
// 
//
// 
//
// 提示： 
//
// 
// 0 <= digits.length <= 4 
// digits[i] 是范围 ['2', '9'] 的一个数字。 
// 
// Related Topics 深度优先搜索 递归 字符串 回溯算法 
// 👍 1151 👎 0

import java.util.ArrayList;
import java.util.List;

//Java：电话号码的字母组合
class LetterCombinationsOfAPhoneNumber {
    public static void main(String[] args) {
        Solution solution = new LetterCombinationsOfAPhoneNumber().new Solution();
        List<String> strings = solution.letterCombinations("256");
        System.out.println(strings);
        // TO TEST
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        // 数字到号码的映射
        private String[] map = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        private List<String> results = new ArrayList<>();

        private StringBuilder sb = new StringBuilder();

        /**
         * 代码思路:
         * 1. 利用递归的方式,先从map数组中获取对应的下标单词
         * 2. 将下标单词进行遍历
         * 3. 将遍历的每个字母都存储到StringBuilder中
         * 4. 如果StringBuilder的长度等于入参字符串的长度说明数据已经生成,一组单词结束
         * 5. 同时删除length-1位置的字母,并且进行下一次递归.
         * @param digits
         * @return
         */
        public List<String> letterCombinations(String digits) {
            if (digits == null || digits.length() == 0) {
                return results;
            }
            backtrack(digits, 0);
            return results;
        }

        // 回溯函数
        private void backtrack(String digits, int index) {

            if (sb.length() == digits.length()) {
                results.add(sb.toString());
                return;
            }
            // 获取入参中的数字所对应的词组
            String numberString = map[digits.charAt(index) - '2'];

            // 遍历该词组
            for (int i = 0; i < numberString.length(); i++) {
                char number = numberString.charAt(i);
                sb.append(number);
                backtrack(digits, index + 1);
                // 这里主要是为了获取到值之后,删除方便下一个继续
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
