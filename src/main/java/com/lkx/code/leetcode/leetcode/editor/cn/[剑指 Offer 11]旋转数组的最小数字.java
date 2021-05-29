package com.lkx.code.leetcode.leetcode.editor.cn;
// 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。例如，数组 [3,4,5,1,2
// ] 为 [1,2,3,4,5] 的一个旋转，该数组的最小值为1。
//
// 示例 1：
//
// 输入：[3,4,5,1,2]
// 输出：1
//
//
// 示例 2：
//
// 输入：[2,2,2,0,1]
// 输出：0
//
//
// 注意：本题与主站 154 题相同：https://leetcode-cn.com/problems/find-minimum-in-rotated-sor
// ted-array-ii/
// Related Topics 二分查找
// 👍 297 👎 0

import cn.hutool.core.math.MathUtil;

import java.util.Arrays;

// Java：旋转数组的最小数字
class XuanZhuanShuZuDeZuiXiaoShuZiLcof {
    public static void main(String[] args) {
        Solution solution = new XuanZhuanShuZuDeZuiXiaoShuZiLcof().new Solution();
        solution.minArray(new int[] {3, 1});
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int minArray(int[] numbers) {
            if (numbers == null || numbers.length == 0) {
                return -1;
            }

            int min = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                min = Math.min(min, numbers[i]);
            }
            return min;
        }
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
