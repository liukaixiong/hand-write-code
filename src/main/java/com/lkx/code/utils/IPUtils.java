package com.lkx.code.utils;

import java.util.List;

import com.google.common.base.Splitter;

public class IPUtils {

    public static void main(String[] args) throws Exception {
        String ip = "192.168.255.11";
        long ipLong = ipToTen(ip);
        System.out.println(ipLong);
        tenToIp(ipLong + "");
        System.out.println(genIpHex(ip));
    }

    private static String getProperty(String name) {
        String value = null;

        value = System.getProperty(name);

        if (value == null) {
            value = System.getenv(name);
        }

        return value;
    }

    static String genIpHex(String ip) throws Exception {

        List<String> items = Splitter.on(".").splitToList(ip);
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte)Integer.parseInt(items.get(i));
        }

        StringBuilder sb = new StringBuilder(bytes.length / 2);

        for (byte b : bytes) {
            sb.append(Integer.toHexString((b >> 4) & 0x0F));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }

    /**
     * int 转换成 Ip
     * @param p
     */
    private static void tenToIp(String p) {
        long temp = Long.parseLong(p);
        String ip = Long.toBinaryString(temp);

        StringBuilder sb = new StringBuilder();
        if (ip.length() < 32) {
            for (int i = 0; i < (32 - ip.length()); i++) {
                sb.append(0);
            }
            sb.append(ip);
        } else if (ip.length() == 32) {
            sb.append(ip);
        }

        for (int i = 0; i < sb.length() - 8; i = i + 8) {
            System.out.print(Integer.parseInt(sb.substring(i, i + 8), 2) + ".");
        }

        System.out.println(Integer.parseInt(sb.substring(sb.length() - 8, sb.length()), 2));
    }

    /**
     * IP转换成十进制
     * 
     * @param ip
     * @return
     */
    private static long ipToTen(String ip) {
        String[] arr = ip.split("\\.");
        long n = Long.parseLong(arr[0]);

        for (int i = 1; i < arr.length; i++) {
            n = n << 8;// 相当于右移8位，同时可以理解为2的8次方
            n = n + Long.parseLong(arr[i]);
        }

        return n;
    }
}
