//给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i,
//ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
//
// 说明：你不能倾斜容器。
//
//
//
// 示例 1：
//
//
//
//
//输入：[1,8,6,2,5,4,8,3,7]
//输出：49
//解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
//
// 示例 2：
//
//
//输入：height = [1,1]
//输出：1
//
//
// 示例 3：
//
//
//输入：height = [4,3,2,1,4]
//输出：16
//
//
// 示例 4：
//
//
//输入：height = [1,2,1]
//输出：2
//
//
//
//
// 提示：
//
//
// n = height.length
// 2 <= n <= 3 * 104
// 0 <= height[i] <= 3 * 104
//
// Related Topics 数组 双指针
// 👍 2024 👎 0

package com.lkx.code.leetcode.leetcode.editor.cn;

class ContainerWithMostWater {
    public static void main(String[] args) {
        Solution solution = new ContainerWithMostWater().new Solution();
//        solution.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7});
        solution.maxArea(new int[]{1, 2, 3, 4, 5, 6, 7});
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxArea(int[] height) {
            int i = 0, j = height.length - 1, res = 0, temp = 0;
//            while (i < j) {
//                res = height[i] < height[j] ?
//                        Math.max(res, temp = (j - i) * height[i++]) :
//                        Math.max(res, temp =(j - i) * height[j--]);
//                System.out.println("i = " + i + ", j=" + j + " \t " + " height[i]=" + height[i] + " , height[j]" + height[j] + " \t "+temp+ " max : " + res);
//            }


            /**
             * 面积计算 = (j - i) * height[Math.min(height[i],height[j])]
             *
             */
            while (i < j) {
                res = height[i] < height[j] ?
                        Math.max(res, temp = (j - i) * height[i++]) :
                        Math.max(res, temp = (j - i) * height[j--]);
//                System.out.println("i = " + i + ", j=" + j + " \t " + " height[i]=" + height[i] + " , height[j]" + height[j] + " \t " + temp + " max : " + res);
                System.out.println("[" + i + "," + j + "] => [" + height[i] + "," + height[j] + "] => " + temp + " max : " + res);
            }
            return res;

        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}