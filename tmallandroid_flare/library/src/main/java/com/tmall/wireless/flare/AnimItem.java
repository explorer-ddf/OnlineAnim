package com.tmall.wireless.flare;

import android.text.TextUtils;

/**
 * Created by ddf on 16/2/23.
 */
public class AnimItem {
    public final static String INVALID_ANIM_ID = "NULL";
    public String animId;
    public String follow;
    public String key; //view属性
    public String repeatMode;
    public String interpolator;
    public String evaluator;
    public String fromValue;
    public String toValue1;
    public String toValue2;
    public int beginTime;
    public int duration;
    public int repeatCount;

    public AnimItem() {
    }

    public AnimItem(String animId) {
        this.animId = animId;
    }

    public boolean isInvalid() {
        return TextUtils.isEmpty(animId) || TextUtils.isEmpty(key);
    }

    public boolean isFirstShow() {
        return TextUtils.isEmpty(follow);
    }
}
