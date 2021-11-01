package com.lkx.code.suanfa;

import java.util.HashMap;

/**
 * 双向链表中：head指向最久为访问的元素 end指向最近未访问的元素
 */
public class LRUCache {
    private Node head;
    private Node end;
    // 缓存存储上限
    private int limit;

    private HashMap<String, Node> hashMap;

    public LRUCache(int limit) {
        this.limit = limit;
        hashMap = new HashMap<String, Node>();
    }

    /**
     * 删除节点
     * 
     * @param node
     *            要删除的节点
     */
    private String removeNode(Node node) {
        if (node == end) {
            // 移除尾节点
            end = end.pre;
        } else if (node == head) {
            // 移除头结点
            head = head.next;
        } else {
            // 移除中间节点
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        return node.key;
    }

    /**
     * 尾部插入节点
     * 
     * @param node
     *            要插入的节点
     */
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

    /*
    * 刷新被访问的节点位置
    * @param node 被访问的节点
    * */
    private void refreshNode(Node node) {
        // 如果访问的是尾节点,即最近未被访问的节点
        if (node == end) {
            return;
        }
        // 移除节点
        removeNode(node);
        // 重新插入节点
        addNode((node));
    }

    public void remove(String key) {
        Node node = hashMap.get(key);
        removeNode(node);
        hashMap.remove(key);
    }

    public void put(String key, String value) {
        Node node = hashMap.get(key);
        if (node == null) {
            // 如果key不存在，插入key-value
            if (hashMap.size() >= limit) {
                String oldKey = removeNode(head);
                hashMap.remove(oldKey);
            }
            node = new Node(key, value);
            addNode(node);
            hashMap.put(key, node);
        } else {
            // 如果key存在，刷新key-value
            node.value = value;
            refreshNode(node);
        }
        System.out.println(node.toString());
    }

    public String get(String key) {
        Node node = hashMap.get(key);
        if (node == null) {
            return null;
        }
        refreshNode(node);
        return node.value;
    }

    class Node {
        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public Node pre;
        public Node next;
        public String key;
        public String value;
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

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(3);
        lruCache.put("001", "用户1信息");
        lruCache.put("002", "用户2信息");
        lruCache.put("003", "用户3信息");
        lruCache.put("004", "用户4信息");
        lruCache.put("005", "用户5信息");
        String s = lruCache.get("002");
        lruCache.put("004", "用户2信息更新");
        lruCache.put("006", "用户6信息");
//        System.out.println(lruCache.get("001"));
//        System.out.println(lruCache.get("006"));
    }
}
