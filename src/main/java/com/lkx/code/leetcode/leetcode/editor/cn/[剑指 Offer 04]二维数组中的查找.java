package com.lkx.code.leetcode.leetcode.editor.cn;
// 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个
// 整数，判断数组中是否含有该整数。
//
//
//
// 示例:
//
// 现有矩阵 matrix 如下：
//
//
// [
// [1, 4, 7, 11, 15],
// [2, 5, 8, 12, 19],
// [3, 6, 9, 16, 22],
// [10, 13, 14, 17, 24],
// [18, 21, 23, 26, 30]
// ]
//
//
// 给定 target = 5，返回 true。
//
// 给定 target = 20，返回 false。
//
//
//
// 限制：
//
// 0 <= n <= 1000
//
// 0 <= m <= 1000
//
//
//
// 注意：本题与主站 240 题相同：https://leetcode-cn.com/problems/search-a-2d-matrix-ii/
// Related Topics 数组 双指针
// 👍 306 👎 0

import java.util.HashSet;
import java.util.Set;

// Java：二维数组中的查找
class ErWeiShuZuZhongDeChaZhaoLcof {
    public static void main(String[] args) {
        Solution solution = new ErWeiShuZuZhongDeChaZhaoLcof().new Solution();
        int[][] matrix = new int[][] {{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22}, {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}};

        boolean numberIn2DArray = solution.findNumberIn2DArray(matrix, 5);
        System.out.println(numberIn2DArray);
        // TO TEST
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 
         * {1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 8, 10, 16, 22}, {10, 13, 14, 17, 24}, {18, 21, 23, 26, 30}}
         * 
         * @param matrix
         * @param target
         * @return
         */
        public boolean findNumberIn2DArray(int[][] matrix, int target) {
            // {
            // {1, 4, 7, 11, 15},
            // {2, 5, 8, 12, 19},
            // {3, 8, 10, 16, 22},
            // {10, 13, 14, 17, 24},
            // {18, 21, 23, 26, 30}
            // }

            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return false;
            }
            // 数组长度的限制，溢出
            int jSize = matrix[0].length;
            int i = matrix.length - 1;
            int j = 0;
            // 要从二位数组的最后往前面找,因为是顺序的.如果数组第一个值大于target说明后面的肯定比target大;
            while (i >= 0 && j < jSize) {
                if (matrix[i][j] == target) {
                    return true;
                } else if (matrix[i][j] > target) {
                    i--;
                } else {
                    j++;
                }
            }

            return false;
        }

        /*public boolean findNumberIn2DArray(int[][] matrix, int target) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return false;
            }
        
            int jSize = matrix[0].length;
        
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < jSize; j++) {
                    // 由于数组中的值是有序的,所以一旦大于target说明后面肯定没有值了
                    if (matrix[i][j] > target) {
                        break;
                    }
                    if (matrix[i][j] == target) {
                        return true;
                    }
                }
            }
            return false;
        }*/
    }
    // leetcode submit region end(Prohibit modification and deletion)

}
