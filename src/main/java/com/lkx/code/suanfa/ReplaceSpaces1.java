package com.lkx.code.suanfa;

/**
 * 题目：请实现一个函数，把字符串中的每个空格替换成"%20"。例如输入“We are happy.”，
 */
public class ReplaceSpaces1 {

    /**
     * 总体实现思路:
     * 1. 先计算出空格出现的次数,根据次数来计算整个字符串的长度.         【 a b c  】  = 5个空格
     * 2. 将字符串的长度设置成计算后的长度                             [5 * 3 + 8 = 21]
     * 3. 这个时候的字符串是左边是原始的字符串，后面的都是补空格的字符串。   [ a b c               ]
     * 4. 这个时候需要从字符串的最右边开始插入字符，同时判断字符从最后往前的字符是否是空格，空格就默认补全%20.直到计算结束。
     * 计算原始数据的下标,从后往前开始判断空格              [ a b c 空格]             -> [ a b c空格空格]             -> [ a b c空格空格]
     * 如果上面判断的是空格则填充%20,否则补充上面的下标数据   [ a b c            %20]  -> [ a b c         %20%20]    -> [ a b c        c%20%20]
     *
     * @param str
     * @return
     */
    public static String replace(StringBuffer str) {


        if (str == null) {
            return null;
        }
        // 得到字符串的长度
        int length = str.length();
        int fixedLength = length - 1;
        // 先计算出现空格的次数，并算出字符串的总长度
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                length += 2;
            }
        }

        str.setLength(length);
        int indexOf = length - 1;
        while (indexOf > fixedLength) {
            char c = str.charAt(fixedLength);
            if (c != ' ') {
                str.setCharAt(indexOf--, c);
            } else {
                str.setCharAt(indexOf--, '0');
                str.setCharAt(indexOf--, '2');
                str.setCharAt(indexOf--, '%');
            }
            fixedLength--;
        }

        return str.toString();
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer(" a b  c  ");
        System.out.println(replace(sb));
        ;
    }


}
