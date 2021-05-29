package com.lkx.code.suanfa;

import java.util.Arrays;

/**
 * 简单的算法： 选择、冒泡、直接插入
 */
public class SimpleSolution {

    /**
     * 交换数组元素
     * 
     * @param arrays
     * @param a
     * @param b
     */
    private static void swap(int[] arrays, int a, int b, boolean debug) {
        arrays[a] ^= arrays[b];
        arrays[b] ^= arrays[a];
        arrays[a] ^= arrays[b];
        log(debug, "[" + arrays[b] + "] <-> [" + arrays[a] + "] >>> " + Arrays.toString(arrays));
    }

    /**
     * 选择排序
     *
     * 每循环一次，得到一个最小（或者最大）的值,将该值交换放入最前(或者最后),起始(最后)坐标累加1,重新继续.
     * 
     * @param array
     */
    public static void select(int[] array, boolean isDebug) {
        for (int i = 0; i < array.length; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {

                if (array[j] < array[min]) {
                    min = j;
                }

            }
            if (i != min) {
                swap(array, i, min, isDebug);
            }
        }
    }

    /**
     * 冒泡排序
     * 
     * 每次和相邻的两个数进行比较,最小的值进行交换,直到交换到最后面.
     * 
     * @param arr
     */
    public static void gulugulu(int[] arr, boolean isDebug) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag = true;// 设定一个标记，若为true，则表示此次循环没有进行交换，也就是待排序列已经有序，排序已然完成。
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1, isDebug);
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
    }

    public static void insert(int[] arrays, boolean debug) {
        for (int i = 0; i < arrays.length; i++) {
            int j = i;
            while (j > 0 && arrays[j] < arrays[j - 1]) {
                swap(arrays, j, j - 1, debug);
                j--;
            }
        }
    }

    /**
     * 插入排序
     * 
     * 从左往右依次比较,如果遇到右边小于左边的,进行交换之后,然后从右往左依次排序完成之后开始下一轮
     * 
     * @param arrays
     * @param debug
     */
    public static void insert2(int[] arrays, boolean debug) {
        for (int i = 1; i < arrays.length; i++) {
            // 构建一个临时变量,用作从右往左的下标值
            int j = i;
            log(debug, "---------------------------" + i + "----------------------------");
            // 从左往右比较,如果匹配上了，说明右边比左边小
            while (j > 0 && arrays[j] < arrays[j - 1]) {
                // 位置交换
                swap(arrays, j, j - 1, debug);
                // 开始从右边往左边开始排序
                j--;
            }
        }
    }

    public static void test(int[] arrays) {
        int length = arrays.length;
        int[] temp = new int[length];

        for (int i = 0; i < length; i++) {
            int index = arrays[i] % length;
            temp[index] = arrays[i];
        }
        log(true, temp);
    }

    private static void log(boolean debug, String text) {
        if (debug) {
            System.out.println(text);
        }
    }

    private static void log(boolean debug, int[] arrays) {
        if (debug) {
            System.out.println(Arrays.toString(arrays));
        }
    }

    public static void main(String[] args) {
        int[] arrays = {1, 9, 3, 6, 8, 4, 3, 2, 7};
        log(true, arrays);
        // select(arrays, true);
//        gulugulu(arrays, true);
        // test(arrays);
        insert2(arrays, true);
        System.out.println(Arrays.toString(arrays));
    }

}
