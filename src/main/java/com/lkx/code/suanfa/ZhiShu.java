package com.lkx.code.suanfa;

import java.util.*;

public class ZhiShu {


    public static void putAttr(String key, Object value, List<Map<String, Object>> resultMap) {
        int resultSize = resultMap.size();
        if (value instanceof List) {
            List valueObject = (List) value;
            int valueSize = valueObject.size();
            for (int i = 0; i < resultSize; i++) {
                Map<String, Object> stringObjectMap = resultMap.get(i);
                int index = i % valueSize;

                if (key.equals("D") && i > 0) {
                    int splitSize = resultSize / valueSize;
                    index = i / splitSize % valueSize;
                }
                stringObjectMap.put(key, valueObject.get(index));
            }
        } else {
            for (int i = 0; i < resultSize; i++) {
                Map<String, Object> stringObjectMap = resultMap.get(i);
                stringObjectMap.put(key, value);
            }
        }
    }


    public static void main(String[] args) {

        Map<String, Object> requestRuleMap = new HashMap<>();
//        requestRuleMap.put("A", "1");
//        requestRuleMap.put("B", Arrays.asList("2", "3", "4"));
//        requestRuleMap.put("c", Arrays.asList("5", "6"));
//        requestRuleMap.put("D", Arrays.asList("7","8","9","10"));
        requestRuleMap.put("A", "1");
        requestRuleMap.put("B", Arrays.asList("2","0"));
        requestRuleMap.put("c", "3");
        requestRuleMap.put("D", "4");


        Integer maxListSize = 1;
        for (Object value :
                requestRuleMap.values()) {
            if (value instanceof Collection) {
                int size = ((Collection) value).size();
                maxListSize = maxListSize * size;
            }
        }

        List<Map<String, Object>> resultMap = new ArrayList<>(maxListSize);

        for (int i = 0; i < maxListSize; i++) {
            resultMap.add(new HashMap<>());
        }

        int size = requestRuleMap.size();
        requestRuleMap.forEach((K, V) -> {
            putAttr(K, V, resultMap);
        });

        System.out.println(resultMap);

        checkResult(resultMap);
    }

    /**
     * 检查结果
     *
     * @param resultMap
     */
    private static void checkResult(List<Map<String, Object>> resultMap) {
        Map<String, Map<String, Object>> codeMap = new HashMap<>();
        for (int i = 0; i < resultMap.size(); i++) {
            Map<String, Object> stringObjectMap = resultMap.get(i);
            StringBuilder sb = new StringBuilder();
            for (Object dataValue : stringObjectMap.values()) {
                sb.append(dataValue).append("#");
            }
            System.out.println(sb.toString());
            Map<String, Object> oldValueMap = codeMap.putIfAbsent(sb.toString(), stringObjectMap);
            if (oldValueMap != null) {
                System.out.println("重复: " + oldValueMap + " \t " + stringObjectMap);
            }

        }
    }


}
