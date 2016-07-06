package com.tmall.wireless.flare;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ddf on 16/2/23.
 */
public final class CfgDataParser {
    private final static String KEY_ITEMS = "items";
    private final static String KEY_PAGE_NAME = "page";
    private final static String KEY_VIEW_ID = "name";
    private final static String KEY_VIEW_TRIGGER = "trigger";
    private final static String KEY_REPEAT_COUNT = "repeatCount";
    private final static String KEY_REPEAT_MODE = "repeatMode";
    private final static String KEY_MOTIONS = "motions";
    private final static String KEY_ANIM_ITEM_ID = "name";
    private final static String KEY_ANIM_ITEM_ATTR_NAME = "key";
    private final static String KEY_ANIM_ITEM_BEGINTIME = "beginTime";
    private final static String KEY_ANIM_ITEM_DURATION = "duration";
    private final static String KEY_ANIM_ITEM_FROMVAL = "fromValue";
    private final static String KEY_ANIM_ITEM_TOVAL = "toValue";
    private final static String KEY_ANIM_ITEM_FOLLOW = "follow";
    private final static String KEY_ANIM_ITEM_INTERPOLATOR = "interpolator";
    private final static String KEY_ANIM_ITEM_EVALUATOR = "evaluator";

    private Map<String, Set<ComposedAnim>> mKVPageAnims = new HashMap<String, Set<ComposedAnim>>();

    public boolean parseAnim(JSONObject jsonObject) {
        if (jsonObject == null || !jsonObject.has(KEY_ITEMS)) {
            return false;
        }

        Map<String, Set<ComposedAnim>> newKVPageAnims = new HashMap<String, Set<ComposedAnim>>();
        JSONArray jsonArray = jsonObject.optJSONArray(KEY_ITEMS);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.optJSONObject(i);
            String pageName = item.optString(KEY_PAGE_NAME);
            if (!TextUtils.isEmpty(pageName)) {
                ComposedAnim composedAnim = parseComposedAnim(item);
                if (newKVPageAnims.containsKey(pageName)) {
                    newKVPageAnims.get(pageName).add(composedAnim);
                } else {
                    HashSet<ComposedAnim> composedAnimSet = new HashSet<ComposedAnim>();
                    composedAnimSet.add(composedAnim);
                    newKVPageAnims.put(pageName, composedAnimSet);
                }
            }
        }

        updateData(newKVPageAnims);
        return true;
    }

    public Map<String, Set<ComposedAnim>> getData() {
        return mKVPageAnims;
    }

    private void updateData(Map<String, Set<ComposedAnim>> newKVPageAnims) {
        Set<String> pageNameSet = newKVPageAnims.keySet();
        for (String pageName : pageNameSet) {
            mKVPageAnims.put(pageName, newKVPageAnims.get(pageName));
        }
    }


    private ComposedAnim parseComposedAnim(JSONObject jsonObject) {
        ComposedAnim composedAnim = new ComposedAnim();
        composedAnim.targetViewId = jsonObject.optString(KEY_VIEW_ID);
        composedAnim.targetViewTrigger = jsonObject.optString(KEY_VIEW_TRIGGER);
        composedAnim.targetPageName = jsonObject.optString(KEY_PAGE_NAME);
        composedAnim.repeatCount = jsonObject.optInt(KEY_REPEAT_COUNT);
        composedAnim.repeatMode = jsonObject.optString(KEY_REPEAT_MODE);

        Set<AnimItem> animItemSet = new HashSet<AnimItem>();
        JSONArray motionsArray = jsonObject.optJSONArray(KEY_MOTIONS);
        for (int i = 0; i < motionsArray.length(); i++) {
            animItemSet.add(parseAnimItem(motionsArray.optJSONObject(i)));
        }
        composedAnim.buildAnimTree(animItemSet);
        return composedAnim;
    }

    private AnimItem parseAnimItem(JSONObject jsonObject) {
        AnimItem item = new AnimItem();
        item.animId = jsonObject.optString(KEY_ANIM_ITEM_ID);
        item.key = jsonObject.optString(KEY_ANIM_ITEM_ATTR_NAME);
        item.beginTime = jsonObject.optInt(KEY_ANIM_ITEM_BEGINTIME);
        item.duration = jsonObject.optInt(KEY_ANIM_ITEM_DURATION);
        item.fromValue = jsonObject.optString(KEY_ANIM_ITEM_FROMVAL);
        item.follow = jsonObject.optString(KEY_ANIM_ITEM_FOLLOW);
        item.repeatCount = jsonObject.optInt(KEY_REPEAT_COUNT);
        item.repeatMode = jsonObject.optString(KEY_REPEAT_MODE);
        item.interpolator = jsonObject.optString(KEY_ANIM_ITEM_INTERPOLATOR);
        item.evaluator = jsonObject.optString(KEY_ANIM_ITEM_EVALUATOR);

        String toValue = jsonObject.optString(KEY_ANIM_ITEM_TOVAL);
        if (toValue.contains(",")) {
            String arrVal[] = toValue.split(",");
            item.toValue1 = arrVal[0];
            if (arrVal.length >= 2) {
                item.toValue2 = arrVal[1];
            }
        } else {
            item.toValue1 = toValue;
        }
        return item;
    }

    private CfgDataParser() {
    }

    public final static CfgDataParser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CfgDataParser INSTANCE = new CfgDataParser();
    }
}
