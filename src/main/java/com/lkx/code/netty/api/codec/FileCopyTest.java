package com.lkx.code.netty.api.codec;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 文件复制
 *
 * @author ： liukx
 * @time ： 2019/11/18 - 14:47
 */
public class FileCopyTest {

    public static void main(String[] args) throws Exception {
        String inputFile = "file/NIOTEST13_IN.txt";
        String ouputFile = "file/NIOTEST13_Out.txt";
        // 传统 - FileInputStream FileOutputStream
        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(ouputFile, "rw");
        long inputLength = new File(inputFile).length();
        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();
        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);

        // 打印当前系统的所有字符集
//        Charset.availableCharsets().forEach((k, v) -> {
//            System.out.println(k + " - " + v);
//        });

        // 定义编解码 // 为什么定义的是iso-8859-1 还能输出中文不乱码
        Charset charset = Charset.forName("iso-8859-1");
//        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();

        // 开始编解码 编码 = 将对象解析成字节的过程 , 解码 - 将字节转换成对象
        CharBuffer charBuffer = decoder.decode(inputData);

//        ByteBuffer outputData = Charset.forName("UTF-8").encode(charBuffer);
        ByteBuffer outputData = encoder.encode(charBuffer);
        // 文件中的中文输出
        System.out.println(charBuffer.get(7));
        // 写入文件
        outputFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();

//        String reason = "你";
//        try {
//            if (StringUtils.isNotBlank(reason) && reason.equals(new String(reason.getBytes("ISO8859-1"), "ISO8859-1"))) {
//                reason = new String(reason.getBytes("ISO8859-1"), "UTF-8");
//            }
//        } catch (UnsupportedEncodingException e) {
//            throw e;
//        }
//        System.out.println(reason + "\t" + reason.getBytes("ISO8859-1"));


        /**
         * 8bit = 1byte
         *
         * ASCII : 美国信息交换标准代码 = 7 bit 来表示一个字符，共计可以表示 128种字符
         *
         * ISO-8859-1 : 8 bit 表示一个字符,共计256，可以胜任法文，但不兼容中文
         *
         * gb2312 ： 兼容中文但是对生僻字不兼容
         *
         * gbk ： 相当于gb2312的交集
         *
         * gb18030 ： 最完整的汉字展现形式。
         *
         * big5 ： 台湾
         *
         * unicode ： 所有字符代表。采用两个字节表示一个字符。
         *
         * 问题是存储容量出现问题,例如美国只需要ASCII,1个字节就OK了，但是用了unicode就必须得两个字节。空间造成浪费
         *
         * UTF-8 = unicode Translation format ： unicode转换格式
         * unicode是一种编码方式,而UTF则是一种存储方式；UTF-8是unicode的实现方式之一。
         *
         * 一般来说:UTF-8会用三个字节表示一个中文.
         */


    }

}
