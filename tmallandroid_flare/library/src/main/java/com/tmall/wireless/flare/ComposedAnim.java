package com.tmall.wireless.flare;

import android.text.TextUtils;

import com.tmall.wireless.internal.TreeBuilder;
import com.tmall.wireless.internal.TreeNode;

import java.util.Set;

/**
 * Created by ddf on 16/2/23.
 */
public class ComposedAnim {
    public String targetViewId;
    public String targetPageName;
    public String targetViewTrigger;
    public int repeatCount;
    public String repeatMode;
    public TreeBuilder<AnimItem> animTree;

    public ComposedAnim() {
        animTree = initAnimTreeWithRootNode();
    }

    //构建复合动画时序:包括并行和串行
    public void buildAnimTree(Set<AnimItem> animItemSet) {
        for (AnimItem item : animItemSet) {
            if (item.isFirstShow()) {
                int pos = animTree.addNode(item, animTree.getRoot());
                buildNodeTree(animTree.getNodeByIndex(pos), animItemSet);
            }
        }
    }

    private void buildNodeTree(TreeNode<AnimItem> parentNode, Set<AnimItem> animItemSet) {
        String prevAnimId = parentNode.getNodeData().animId;
        for (AnimItem item : animItemSet) {
            if (item.follow.equals(prevAnimId)) {
                int pos = animTree.addNode(item, parentNode);
                buildNodeTree(animTree.getNodeByIndex(pos), animItemSet);
            }
        }
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(targetViewId);
    }

    public TreeBuilder<AnimItem> getAnimTree() {
        return animTree;
    }

    private TreeBuilder<AnimItem> initAnimTreeWithRootNode() {
        AnimItem rootData = new AnimItem(AnimItem.INVALID_ANIM_ID);
        return new TreeBuilder<AnimItem>(rootData);
    }
}
