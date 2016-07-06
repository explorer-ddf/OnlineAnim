package com.tmall.wireless.flare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.tmall.wireless.internal.TreeBuilder;
import com.tmall.wireless.internal.TreeNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ddf on 16/2/23.
 */
public class FlareUtil {

    public static boolean init(String config) {
        try {
            return CfgDataParser.getInstance().parseAnim(new JSONObject(config));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param containerName 能够唯一包含@param container的模块容器名称.
     * @param container     该模块的view.从此view里面查找需要动画的view.
     */
    public static List<AnimatorSet> bind(String containerName, View container) {
        Map<String, ComposedAnim> mapTargetViewId2Anim = getPageTargetViewMap(containerName);
        if (mapTargetViewId2Anim == null) {
            return null;
        }

        List<AnimatorSet> ret = new ArrayList<AnimatorSet>();
        Context context = container.getContext();
        String pkgName = context.getPackageName();
        for (Map.Entry<String, ComposedAnim> entry : mapTargetViewId2Anim.entrySet()) {
            String viewId = entry.getKey();
            int resId = context.getResources().getIdentifier(viewId, "id", pkgName);
            if (resId != 0) {
                final ComposedAnim composedAnim = entry.getValue();
                View targetView = container.findViewById(resId);
                if (targetView != null) {
                    final AnimatorSet animatorSet = bindInternal(targetView, composedAnim);
                    ret.add(animatorSet);

                    bindAnimTrigger(targetView, animatorSet, composedAnim.targetViewTrigger);
                }
            }
        }
        return ret;
    }

    /**
     * @param containerName 能够唯一包含@param container的模块容器名称
     * @param targetView    需要绑定动画的目标view
     */
    public static AnimatorSet bindStatic(String containerName, View targetView) {
        Map<String, ComposedAnim> mapTargetViewId2Anim = getPageTargetViewMap(containerName);
        if (mapTargetViewId2Anim != null && mapTargetViewId2Anim.size() == 0) {
            return null;
        }

        for (Map.Entry<String, ComposedAnim> entry : mapTargetViewId2Anim.entrySet()) {
            final ComposedAnim composedAnim = entry.getValue();
            final AnimatorSet animatorSet = bindInternal(targetView, composedAnim);

            bindAnimTrigger(targetView, animatorSet, composedAnim.targetViewTrigger);
            return animatorSet;
        }
        return null;
    }

    private static AnimatorSet bindInternal(View targetView, ComposedAnim composedAnim) {
        TreeBuilder<AnimItem> animTree = composedAnim.getAnimTree();
        TreeNode<AnimItem> rootNode = animTree.getRoot();
        List<TreeNode<AnimItem>> childNodeList = rootNode.getChildNodeList();
        if (childNodeList != null && childNodeList.size() > 0) {
            AnimatorSet animationSet = new AnimatorSet();
            TreeNode<AnimItem> animNode = childNodeList.get(0);
            ObjectAnimator objectAnimator = ObjectAnimatorFactory.getObjectAnimator(targetView, animNode.getNodeData());
            AnimatorSet.Builder builder = animationSet.play(objectAnimator);
            buildSequentialAnim(targetView, animationSet, objectAnimator, animNode.getChildNodeList());

            for (int i = 1; i < childNodeList.size(); i++) {
                TreeNode<AnimItem> siblingNode = childNodeList.get(i);
                ObjectAnimator siblingObjectAnimator = ObjectAnimatorFactory.getObjectAnimator(targetView, siblingNode.getNodeData());
                builder.with(siblingObjectAnimator);
                buildSequentialAnim(targetView, animationSet, siblingObjectAnimator, siblingNode.getChildNodeList());
            }
            return animationSet;
        }
        return null;
    }

    // 绑定动画触发时机.,如果没有配置则立即触发.
    private static void bindAnimTrigger(View targetView, final AnimatorSet animatorSet, final String targetViewTrigger) {
        if (TextUtils.isEmpty(targetViewTrigger)) {
            animatorSet.start();
        } else {
            targetView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void sendAccessibilityEvent(View host, int eventType) {
                    super.sendAccessibilityEvent(host, eventType);

                    if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && targetViewTrigger.equals(IFlareConstant.TRIGGER_ONCLICK)
                            || eventType == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED && targetViewTrigger.equals(IFlareConstant.TRIGGER_ONLONGCLICK)) {
                        animatorSet.start();
                    }
                }
            });
        }
    }
    /**
     * 构建动画树
     */
    private static void buildSequentialAnim(View targetView, AnimatorSet animationSet, ObjectAnimator objectAnimator, List<TreeNode<AnimItem>> childNodeList) {
        if (childNodeList == null || childNodeList.size() == 0) {
            return;
        }
        for (TreeNode<AnimItem> node : childNodeList) {
            ObjectAnimator nextObjectAnimator = ObjectAnimatorFactory.getObjectAnimator(targetView, node.getNodeData());
            animationSet.play(objectAnimator).before(nextObjectAnimator);
            buildSequentialAnim(targetView, animationSet, nextObjectAnimator, node.getChildNodeList());
        }
    }


    /**
     * 获取页面内需要应用动画的目标view及其对应的动画map
     */
    private static Map<String, ComposedAnim> getPageTargetViewMap(String pageName) {
        Map<String, Set<ComposedAnim>> data = CfgDataParser.getInstance().getData();
        if (data.containsKey(pageName)) {
            Map<String, ComposedAnim> retMap = new HashMap<String, ComposedAnim>();
            Set<ComposedAnim> composedAnimSet = data.get(pageName);
            for (ComposedAnim composedAnim : composedAnimSet) {
                if (composedAnim.isValid()) {
                    retMap.put(composedAnim.targetViewId, composedAnim);
                }
            }
            return retMap;
        }
        return null;
    }
}
