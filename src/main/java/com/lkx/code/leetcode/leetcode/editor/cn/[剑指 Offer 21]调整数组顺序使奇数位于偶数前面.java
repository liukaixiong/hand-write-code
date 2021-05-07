package com.lkx.code.leetcode.leetcode.editor.cn;
// 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。
//
//
//
// 示例：
//
//
// 输入：nums = [1,2,3,4]
// 输出：[1,3,2,4]
// 注：[3,1,2,4] 也是正确的答案之一。
//
//
//
// 提示：
//
//
// 0 <= nums.length <= 50000
// 1 <= nums[i] <= 10000
//
// 👍 122 👎 0

// Java：调整数组顺序使奇数位于偶数前面
class DiaoZhengShuZuShunXuShiQiShuWeiYuOuShuQianMianLcof {
    public static void main(String[] args) {
        Solution solution = new DiaoZhengShuZuShunXuShiQiShuWeiYuOuShuQianMianLcof().new Solution();
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] exchange(int[] nums) {
            int length = nums.length;
            int index = 0;
            for (int i = 0; i < length; i++) {
                if ((nums[i] & 1) != 0) {
                    if (index != i) {
                        swap(nums, i, index);
                    }
                    index++;
                }
            }
            return nums;
        }

        private void swap(int[] array, int i, int j) {
            array[i] ^= array[j];
            array[j] ^= array[i];
            array[i] ^= array[j];
        }
    }

    // leetcode submit region end(Prohibit modification and deletion)

}
