package com.lkx.code.suanfa;

import com.lkx.code.jdk.HashMapMain;

import java.util.HashMap;
import java.util.Map;

public class LRUCache3 {
    private int limit;

    private Map<String, Node> nodeMap = new HashMap<>();

    /**
     * 标准链表
     */
    private Node head;

    /**
     * 尾部链表 从后往前延伸
     */
    private Node end;

    class Node {

        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;
        Node next;
        Node pre;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s:%s ", this.key, this.value));
            Node node = pre;
            while (node != null) {
                sb.append(String.format("%s:%s ", node.key, node.value));
                node = node.pre;
            }

            return sb.toString();
        }
    }

    public LRUCache3(int limit) {
        this.limit = limit;
    }

    public void put(String key, String value) {
        Node node = nodeMap.get(key);

        if (node == null) {

            if (nodeMap.size() >= limit) {
                 removeNode(head.key);
                // 同时也要删除缓存中的值
                nodeMap.remove(head.key);
            }

            // 创建新的节点
            node = new Node(key, value);
            addNode(node);
            nodeMap.put(key, node);

        } else {

            node.value = value;

            // 刷新节点的位置
            refreshNode(node);
        }

        System.out.println(end.toString());

    }

    private void refreshNode(Node node) {
        if (node == end) {
            return;
        }

        removeNode(node.key);

        addNode(node);
    }

    private void addNode(Node node) {

        if (end != null) {
            end.next = node;
            node.pre = end;
            node.next = null;
        }

        end = node;

        if (head == null) {
            head = node;
        }

    }

    private void removeAllNode(Node node, int type, String key) {
        if (node == null) {
            return;
        }
        if (node.key.equals(key)) {
            if (node.pre != null) {
                node.pre.next = node.pre;
                node.next=null;
            }

            if (node.next != null) {
                Node next = node.next;
                next.pre = null;
                node.next.pre = next;
            }
        } else {
            if (type == 1) {
                removeAllNode(node.pre, type, key);
            } else if (type == 0) {
                removeAllNode(node.next, type, key);
            } else {
                System.out.println("weizhi");
            }
        }
    }

    /**
     * 删除节点
     * 
     * @param key
     * @return
     */
    private void removeNode(String key) {
        // if (end == node) {
        // end = end.pre;
        // } else if (head == node) {
        // head = head.next;
        // } else {
        // // 既不是头也不是尾，而是中间的值
        // node.next.pre = node.pre;
        // node.pre.next = node.next;
        // }
        removeAllNode(head, 0, key);
        removeAllNode(end, 1, key);
    }

    private String get(String key) {
        Node node = nodeMap.get(key);
        if (node != null) {
            refreshNode(node);
            return node.value;
        }
        return null;
    }

    public static void main(String[] args) {
        LRUCache3 lruCache = new LRUCache3(3);
        lruCache.put("001", "用户1信息");
        lruCache.put("002", "用户2信息");
        lruCache.put("003", "用户3信息");
        lruCache.put("004", "用户4信息");
        lruCache.put("005", "用户5信息");
        String s = lruCache.get("002");
        lruCache.put("004", "用户2信息更新");
        // for (int i = 10; i < 100; i++) {
        // lruCache.put("00" + i, "用户" + i + "信息更新");
        // }
    }

}
