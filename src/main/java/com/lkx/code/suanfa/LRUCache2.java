package com.lkx.code.suanfa;

import org.w3c.dom.Node;

import java.util.HashMap;

public class LRUCache2 {
    /**
     * 链表大小
     */
    private int limit;

    public HashMap<String, Node> nodeMap;

    private Node end;
    private Node head;

    public LRUCache2(int limit) {
        this.limit = limit;
        this.nodeMap = new HashMap<>(limit);
    }

    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public String put(String key, String value) {
        Node node = nodeMap.get(key);
        if (node == null) {

            if (nodeMap.size() >= limit) {
                // 删除访问最后的节点
                String odlKey = removeNode(head);
                nodeMap.remove(odlKey);
            }

            node = new Node(key, value);
            addNode(node);
            nodeMap.put(key, node);

        } else {
            node.value = value;
            refreshNode(node);
        }
        System.out.println(node.toString());
        return node.value;
    }

    public String get(String key) {
        Node node = nodeMap.get(key);
        if (node != null) {
            refreshNode(node);
            return node.value;
        }
        return null;
    }

    public void remove(String key) {
        Node node = nodeMap.get(key);
        if (node != null) {
            removeNode(node);
            nodeMap.remove(key);
        }
    }

    /**
     * 刷新当前node的顺序位置
     * 
     * @param node
     */
    private void refreshNode(Node node) {
        // 如果node就是end节点那么无需调整
        if (node == end) {
            return;
        }

        // 先删 在加
        removeNode(node);

        addNode(node);
    }

    /**
     * 调整head和end的顺序
     * 
     * @param node
     */
    private void addNode(Node node) {

        // 调整end的链表顺序，将当前节点放到头部
        if (end != null) {
            end.next = node;
            node.pre = end;
            node.next = null;
        }

        end = node;

        // 调整head的链表顺序
        if (head == null) {
            head = node;
        }
    }

    /**
     * 删除节点，仅仅只是操作节点
     * 
     * @param node
     * @return
     */
    private String removeNode(Node node) {

        if (node == end) {
            // 直接将该节点的上一个节点作为头
            end = end.pre;
        } else if (node == head) {
            // 直接将该节点的下一个节点做为头
            head = head.next;
        } else {
            // 既不是head也不是end节点,那么就将当前node调整
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        return node.key;
    }

    /**
     * 数据节点
     */
    class Node {
        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

        Node pre;
        Node next;
        String key;
        String value;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node node = pre;
            while (node != null) {
                sb.append(String.format("%s:%s ", node.key, node.value));
                node = node.pre;
            }

            return sb.toString();
        }
    }

    public static void main(String[] args) {
        LRUCache2 lruCache = new LRUCache2(3);
        lruCache.put("001", "用户1信息");
        lruCache.put("002", "用户2信息");
        lruCache.put("003", "用户3信息");
        lruCache.put("004", "用户4信息");
        lruCache.put("005", "用户5信息");
        String s = lruCache.get("002");
        lruCache.put("004", "用户2信息更新");
        lruCache.put("006", "用户6信息");
    }
}
