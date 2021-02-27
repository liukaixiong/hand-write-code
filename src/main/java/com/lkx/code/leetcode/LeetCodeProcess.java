package com.lkx.code.leetcode;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Module 算法
 * @Description 算法解析
 * @Author liukaixiong
 * @Date 2020/12/4 13:19
 */
public class LeetCodeProcess {

    /**
     * 两数之和
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            int cha = target - nums[i];
            // 获取当前target-数组下标位置的数,得到结果,然后看是否存在hash中,之前如果过遍历给过则直接拿到值
            if (hashtable.containsKey(cha)) {
                return new int[]{hashtable.get(cha), i};
            }
            hashtable.put(nums[i], i);
        }
        return new int[0];
    }


    public static void printLog(Supplier supplier) {
        Object o = supplier.get();
        System.out.println(JSON.toJSONString(o));
    }

    public static void main(String[] args) {
        printLog(() -> {
            return twoSum(new int[]{
                    1, 5, 3, 2
            }, 8);
        });

    }

}
