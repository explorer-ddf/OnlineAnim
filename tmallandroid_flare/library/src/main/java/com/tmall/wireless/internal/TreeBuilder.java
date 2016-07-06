package com.tmall.wireless.internal;

/**
 * Created by ddf on 16/2/25.
 */
public class TreeBuilder<T> {
    private final int DEFAULT_TREE_SIZE = 10;
    private int treeSize = 0;
    private TreeNode<T>[] nodes; //数组记录该树的所有节点

    // 记录节点数
    private int nodeNums;

    public TreeBuilder(T data) {
        treeSize = DEFAULT_TREE_SIZE;
        nodes = new TreeNode[treeSize];
        nodes[0] = new TreeNode<T>(0, data);
        nodeNums++;
    }

    public int addNode(T data, TreeNode<T> parentNode) {
        for (int i = 0; i < treeSize; i++) {
            if (nodes[i] == null) {
                nodes[i] = new TreeNode<T>(i, data);
                parentNode.addChildNode(nodes[i]);
                nodeNums++;
                return i;
            }
        }
        throw new RuntimeException("该树已满，无法添加新节点");
    }

    public TreeNode<T> getRoot() {
        return nodes[0];
    }

    public TreeNode<T> getNodeByIndex(int index) {
        if (index < nodeNums) {
            return nodes[index];
        }
        return null;
    }
}
