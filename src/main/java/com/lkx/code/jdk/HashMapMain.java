package com.lkx.code.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashMapMain {

    private static Logger logger = LoggerFactory.getLogger(HashMapMain.class);

    final static int defaultLength = 16;

    /**
     * 摘自HashMap的hash算法
     *
     * @param key
     * @return
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) throws Exception {


        // 当数组长度为16的时候，length-1的后四位全部都是1
//        intToBinary("周杰伦", 32);
//        intToBinary("周杰伦1809", 32);

//        for (int i = 1000; i < 2000; i++) {
//            intToBinary("周杰伦" + i, 32);
//        }

        String s15 = intToBinary(15, 32);
        System.out.println(" 15的二进制 : " + s15);
        String s16 = intToBinary(16, 32);
        System.out.println(" 16的二进制 : " + s16);
        // 如果是扩容的情况
        //周杰伦           0000,0001,0100,1001,1011,0000,0001,1110
        //周杰伦1809       1110,0110,1010,1111,1000,1111,1000,1110
        System.out.println("开始计算HASH的下标:");
        testHashLog("周杰伦");
        testHashLog("周杰伦11");
        testHashLog("周杰伦1809");
//        String content = "1101,0101,1001,1110,1111,0011,0010,0000";
//        String text = startSelectReplace(content, 6);
//        String text1 = startSelectReplace(content, 7);
//        String text2 = startSelectReplace(content, 8);
//        String text3 = startSelectReplace(content, 9);
//        String text4 = startSelectReplace(content, 10);
//        System.out.println(text);


//        System.out.println(("郭德纲".hashCode()) & 16);
//        System.out.println(("周杰伦11".hashCode()) & 16);
//        System.out.println(("周杰伦22".hashCode()) & 16);
//        System.out.println(("周杰伦33".hashCode()) & 16);
//        System.out.println(("周杰伦44".hashCode()) & 16);
//        System.out.println("----------------------");
//        System.out.println(("周杰伦".hashCode()) & 15);
//        System.out.println(("周杰伦11".hashCode()) & 15);
//        System.out.println(("周杰伦22".hashCode()) & 15);
//        System.out.println(("周杰伦33".hashCode()) & 15);
//        System.out.println(("周杰伦44".hashCode()) & 15);
//        System.out.println("----------------------");
//        System.out.println(("周杰伦".hashCode() ^ ("周杰伦".hashCode() >>> 16)) & 15);
//        System.out.println(("周杰伦11".hashCode() ^ ("周杰伦11".hashCode() >>> 16)) & 15);
//        System.out.println(("周杰伦22".hashCode() ^ ("周杰伦22".hashCode() >>> 16)) & 15);
//        System.out.println(("周杰伦33".hashCode() ^ ("周杰伦33".hashCode() >>> 16)) & 15);
//        System.out.println(("周杰伦44".hashCode() ^ ("周杰伦44".hashCode() >>> 16)) & 15);
//        System.out.println("----------------------");
//        System.out.println(hash("周杰伦") & 15);
//        System.out.println(hash("周杰伦11") & 15);
//        System.out.println(hash("周杰伦22") & 15);
//        System.out.println(hash("周杰伦33") & 15);
//        System.out.println(hash("周杰伦44") & 15);
    }

    public static int hashIndex(Object val, int length) {
        return hash(val) & (length - 1);
    }

    /**
     * 传递进来如果是整数,那么就是hashcode值,如果不是则调用hashcode方法
     *
     * @param val
     * @return
     */
    public static int getHashCode(Object val) {
        int hashCode = 0;
        if (val instanceof Number) {
            hashCode = (int) val;
        } else {
            hashCode = val.hashCode();
        }
        return hashCode;
    }

    public static void testHashLog(Object val) {
        // 1. 获取hashcode的二进制值
        int hashCode = getHashCode(val);
        String binaryValue = Integer.toBinaryString(hashCode);
        String hightBinaryValue = intToBinary(val, 32);
        System.out.println("1. 值: " + val + " \t hashCode:" + hashCode + " \t 二进制结果:" + binaryValue + "\t 高位补0结果:" + hightBinaryValue + " \t最后四位:" + hightBinaryValue.substring(35, hightBinaryValue.length()));
        int hash = hash(val);
        String hashBinaryValue = intToBinary(hash, 32);
        System.out.println("2. 将原始hashcode的二进制进行右移>>>16位后的结果 " + hashBinaryValue + " \t最后四位:" + hashBinaryValue.substring(35));
        int hashIndex = hashIndex(val, defaultLength);

        int lastIndex = defaultLength / 16 + 4;
        int splitIndex = lastIndex / 5;
        int currentIndex = lastIndex + splitIndex;
        int start = hashBinaryValue.length() - currentIndex;
        String referenceIndex = hashBinaryValue.substring(start, start + 1);
        String cankao = endSelectReplace(hashBinaryValue, currentIndex);
        System.out.println("3. 将hash&数组长度得到下标结果:" + hashIndex + "\t 参考值:" + referenceIndex + " \t 参考下标值:" + cankao + " 倒数位置:" + lastIndex + " \t 下标结果的二进制:" + intToBinary(hashIndex, 32));


        //System.out.println("值:" + val + " 计算hashcode的二进制结果：" + binaryValue + " 高位补0的结果：" + hightBinaryValue + "\t 最后四位:" + hightBinaryValue.substring(35, hightBinaryValue.length()) + " \t Hash算法之后的二进制 : " + hashBinaryValue + " \t hash 结果:" + hashIndex);
        System.out.println("-----------------------------------------");
    }

    public static int bitToIndex(int bit, int length) {
        return bit & length;
    }

    /**
     * 从后往前查找，找到之后[]出来
     *
     * @param content
     * @param num
     * @return
     */
    public static String endSelectReplace(String content, int num) {
        int newNum = content.length() - num;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            if (i == newNum) {
                sb.append("[" + content.charAt(i) + "]");
            } else {
                sb.append(content.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 二进制转换成对应的hashcode值
     *
     * @param val
     * @return
     */
    public static int bitTOInt(Object val) {
        return Integer.parseInt(val.toString(), 2);
    }

    /**
     * hashcode 转成 bit 二进制
     *
     * @param hashCode
     * @return
     */
    public static String hashCodeToBit(int hashCode) {
        return Integer.toBinaryString(hashCode);
    }

    /**
     * 计算对象的hashcode结果，高位补0
     *
     * @param val    对象
     * @param bitNum 位长度
     * @return
     */
    public static String intToBinary(Object val, int bitNum) {
        //1.补零
        int hashCode = getHashCode(val);
        String binaryValue = Integer.toBinaryString(hashCode);
        String binaryStr = binaryValue;
        if (bitNum < binaryStr.length()) {
            bitNum += bitNum;//不断翻倍8 16 32 64...
        }
        while (binaryStr.length() < bitNum) {
            binaryStr = "0" + binaryStr;
        }
        //2.格式化
        String str = "";
        for (int i = 0; i < binaryStr.length(); ) {
            str += binaryStr.substring(i, i = i + 4) + ",";
        }
        String value = str.substring(0, str.length() - 1);
        return value;
    }

}
