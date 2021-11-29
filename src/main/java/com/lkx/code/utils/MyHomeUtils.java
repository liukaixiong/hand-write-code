package com.lkx.code.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class MyHomeUtils {
    private static String myHomeDir = "D:\\github\\MyHome";
    private static Set<String> fileList = new HashSet<>();

    public static void main(String[] args) {
        String filePath = myHomeDir + "\\文章";
        File[] files = new File(filePath).listFiles();
        deepFile(files, 0);
    }

    private static void deepFile(File[] files, int index) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                if (fileList.add(file.getName()) && !isEndWith(file.getName(), "assets")) {
                    System.out.println("##" + appendIndex("#", index) + " " + file.getName());
                }
                deepFile(file.listFiles(), index + 1);
            } else {
                if (!isEndWith(file.getName(), "png", "gif", "jpg")) {
                    System.out.println(appendIndex("-", index) + "\t" + "[" + file.getName() + "]("
                        + mdPath(file.getPath(), myHomeDir, ".") + ")");
                }
            }
        }
    }

    public static String mdPath(String path, String replaceStr, String replace) {
        return path.replace(replaceStr, replace);
    }

    public static String appendIndex(String str, int index) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < index; i++) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    private static boolean isEndWith(String fileName, String... fileEndsWith) {
        for (int i = 0; i < fileEndsWith.length; i++) {
            String s = fileEndsWith[i];
            if (fileName.endsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
