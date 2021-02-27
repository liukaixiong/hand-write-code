//给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复
//的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 示例： 
//
// 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
//
//满足要求的三元组集合为：
//[
//  [-1, 0, 1],
//  [-1, -1, 2]
//]
// 
// Related Topics 数组 双指针 
// 👍 2799 👎 0

package com.lkx.code.leetcode.leetcode.editor.cn;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ThreeSum {
    public static void main(String[] args) {
        Solution solution = new ThreeSum().new Solution();
//        int nums[] = new int[]{-1, 0, 1, 2, -1, -4};
        int nums[] = new int[]{-1,0,1,2,-1,-4};
        List<List<Integer>> lists = solution.threeSum(nums);
        System.out.println(JSON.toJSONString(lists));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
//            List<List<Integer>> resultList = new ArrayList<>();
//
//            // nums = [-1, 0, 1, 2, -1, -4]，
//            for (int i = 0; i < nums.length - 1; i++) {
//                int i1 = nums[i] + nums[i + 1];
//                for (int j = nums.length - 1; j > i + 1; j--) {
//                    if (i1 + nums[j] == 0) {
//                        resultList.add(Arrays.asList(nums[i], nums[i + 1], nums[j]));
//                        break;
//                    }
//                }
//            }


            List<List<Integer>> ans = new ArrayList<>();
            if (nums == null || nums.length <= 2) return ans;

            Arrays.sort(nums); // O(nlogn)

            for (int i = 0; i < nums.length - 2; i++) { // O(n^2)
                if (nums[i] > 0) break; // 第一个数大于 0，后面的数都比它大，肯定不成立了
                if (i > 0 && nums[i] == nums[i - 1]) continue; // 去掉重复情况
                int target = -nums[i];
                int left = i + 1, right = nums.length - 1;
                while (left < right) {
                    if (nums[left] + nums[right] == target) {
                        ans.add(new ArrayList<>(Arrays.asList(nums[i], nums[left], nums[right])));

                        // 现在要增加 left，减小 right，但是不能重复，比如: [-2, -1, -1, -1, 3, 3, 3], i = 0, left = 1, right = 6, [-2, -1, 3] 的答案加入后，需要排除重复的 -1 和 3
                        left++; right--; // 首先无论如何先要进行加减操作
                        while (left < right && nums[left] == nums[left - 1]) left++;
                        while (left < right && nums[right] == nums[right + 1]) right--;
                    } else if (nums[left] + nums[right] < target) {
                        left++;
                    } else {  // nums[left] + nums[right] > target
                        right--;
                    }
                }
            }
            return ans ;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}