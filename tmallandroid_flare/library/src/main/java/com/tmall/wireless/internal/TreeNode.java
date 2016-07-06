package com.tmall.wireless.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddf on 16/2/25.
 */
public class TreeNode<T> {
    private int mIndex;  //记录树节点在数组中索引的位置
    T mNodeData;
    public List<TreeNode<T>> mChildNodeList;

    public TreeNode(T nodeData) {
        mIndex = -1;
        mNodeData = nodeData;
    }

    public TreeNode(int index, T nodeData) {
        mIndex = index;
        mNodeData = nodeData;
    }

    public boolean isLeaf() {
        return mChildNodeList == null || mChildNodeList.size() == 0;
    }

    public T getNodeData() {
        return mNodeData;
    }

    public List<TreeNode<T>> getChildNodeList() {
        return mChildNodeList;
    }

    public void addChildNode(TreeNode<T> childNode) {
        if (mChildNodeList == null) {
            mChildNodeList = new ArrayList<TreeNode<T>>();
        }
        mChildNodeList.add(childNode);
    }
}
