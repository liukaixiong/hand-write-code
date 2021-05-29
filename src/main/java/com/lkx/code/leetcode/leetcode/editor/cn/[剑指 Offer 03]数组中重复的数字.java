package com.lkx.code.leetcode.leetcode.editor.cn;
// 找出数组中重复的数字。
//
//
// 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请
// 找出数组中任意一个重复的数字。
//
// 示例 1：
//
// 输入：
// [2, 3, 1, 0, 2, 5, 3]
// 输出：2 或 3
//
//
//
//
// 限制：
//
// 2 <= n <= 100000
// Related Topics 数组 哈希表
// 👍 384 👎 0

import java.util.Arrays;

// Java：数组中重复的数字
class ShuZuZhongZhongFuDeShuZiLcof {
    public static void main(String[] args) {
        Solution solution = new ShuZuZhongZhongFuDeShuZiLcof().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 两者交换
         *
         * @param nums
         * @param i
         * @param j
         */
        public void restCount(int[] nums, int i, int j) {
            nums[i] = nums[i] ^ nums[j];
            nums[j] = nums[i] ^ nums[j];
            nums[i] = nums[i] ^ nums[j];
            System.out.println(Arrays.toString(nums));
        }

        public int findRepeatNumber(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                // while (nums[i] != nums[nums[i]]) {
                // restCount(nums, i, nums[i]);
                // }
                // if (nums[i] != i && nums[i] == nums[nums[i]]) {
                // return nums[i];
                // }

                /**
                 *
                 * 原地交换的原则 : 因为题目已经给出了0~(n-1)肯定不会超过这个范围 拿原地坐标上的值,与值的坐标比较,不相等则交换; 直到最后，重复的肯定会交换到同一个坐标；
                 */
                // 这里的判断是避免出现比如下标为0,nums[0] = 0的情况，因为下面nums[i] == nums[nums[i]]会得错误的结果
                while (nums[i] != i) {
                    // 这里肯定不会出现下标的问题，相等则结束
                    if (nums[i] == nums[nums[i]]) {
                        return nums[i];
                    }
                    restCount(nums, i, nums[i]);
                }
            }
            return -1;
        }

        /*public int findRepeatNumber(int[] nums) {
        Set<Integer> dataList = new HashSet<>();
        int number = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            boolean result = dataList.add(num);
            if (!result) {
                number = num;
                break;
            }
        }
        return number;
        }*/
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
