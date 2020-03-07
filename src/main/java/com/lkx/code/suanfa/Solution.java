package com.lkx.code.suanfa;

import java.util.Arrays;

/**
 * 找出重复的数据
 */
public class Solution {
    // Parameters:
    //    numbers:     an array of integers
    //    length:      the length of array numbers
    //    duplication: (Output) the duplicated number in the array number,length of duplication array is 1,so using duplication[0] = ? in implementation;
    //                  Here duplication like pointor in C/C++, duplication[0] equal *duplication in C/C++
    //    这里要特别注意~返回任意重复的一个，赋值duplication[0]
    // Return value:       true if the input is valid, and there are some duplications in the array number
    //                     otherwise false
    public static boolean duplicate(int numbers[], int length, int[] duplication) {
        if (numbers == null || length <= 0)
            return false;
        for (int a : numbers) {
            if (a < 0 || a >= length)
                return false;
        }
        System.out.println("打印数组 : " + Arrays.toString(numbers));
        int temp;
        for (int i = 0; i < length; i++) {
            System.out.println("========================");
            while (numbers[i] != i) {
                System.out.println(" numbers[numbers[i]] == numbers[i] = " + numbers[numbers[i]] + "=" + numbers[i] + " 此时的位置 : " + Arrays.toString(numbers));
                if (numbers[numbers[i]] == numbers[i]) {
                    duplication[0] = numbers[i];
                    return true;
                }
                temp = numbers[i];
                numbers[i] = numbers[temp];
                numbers[temp] = temp;
                System.out.println(" 进行交换 : " + numbers[i]);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int arrya[] = {1, 3, 4, 2, 1, 4};
        int chongfu[] = new int[10];
        boolean duplicate = duplicate(arrya, arrya.length, chongfu);
        System.out.println("是否重复 : " + duplicate + " 重复数组为:" + Arrays.toString(chongfu));
    }
}