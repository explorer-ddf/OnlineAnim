package com.tmall.wireless.flare;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by ddf on 16/2/25.
 */
public class ObjectAnimatorFactory {

    public static ObjectAnimator getObjectAnimator(Object target, AnimItem item) {
        ObjectAnimator objectAnimator = null;
        if (item.key.equals(IFlareConstant.ATTR_ALPHA) ||
                item.key.equals(IFlareConstant.ATTR_ROTATION) ||
                item.key.equals(IFlareConstant.ATTR_ROTATION_X) ||
                item.key.equals(IFlareConstant.ATTR_ROTATION_Y) ||
                item.key.equals(IFlareConstant.ATTR_TRANSLATION_X) ||
                item.key.equals(IFlareConstant.ATTR_TRANSLATION_Y) ||
                item.key.equals(IFlareConstant.ATTR_TRANSLATION_Z) ||
                item.key.equals(IFlareConstant.ATTR_SCALE_X) ||
                item.key.equals(IFlareConstant.ATTR_SCALE_Y)) {

            float fromVal = Float.valueOf(item.fromValue).floatValue();
            float toVal1 = Float.valueOf(item.toValue1).floatValue();
            if (TextUtils.isEmpty(item.toValue2)) {
                objectAnimator = ObjectAnimator.ofFloat(target, item.key,
                        fromVal, toVal1);
            } else {
                float toVal2 = Float.valueOf(item.toValue2).floatValue();
                objectAnimator = ObjectAnimator.ofFloat(target, item.key,
                        fromVal, toVal1, toVal2);
            }
        } else if (item.key.equals(IFlareConstant.ATTR_COLOR)) {

            int fromVal = Color.parseColor(item.fromValue);
            int toVal1 = Color.parseColor(item.toValue1);
            if (TextUtils.isEmpty(item.toValue2)) {
                objectAnimator = ObjectAnimator.ofInt(target, IFlareConstant.ATTR_COLOR,
                        fromVal, toVal1);
            } else {
                int toVal2 = Color.parseColor(item.toValue2);
                objectAnimator = ObjectAnimator.ofInt(target, IFlareConstant.ATTR_COLOR,
                        fromVal, toVal1, toVal2);
            }

            objectAnimator.setEvaluator(new ArgbEvaluator());
        }

        if (objectAnimator != null) {
            if (item.beginTime > 0) {
                objectAnimator.setStartDelay(item.beginTime);
            }
            if (item.duration > 0) {
                objectAnimator.setDuration(item.duration);
            }

            if (item.repeatCount != 0) {
                objectAnimator.setRepeatCount(item.repeatCount);
            }

            if (!TextUtils.isEmpty(item.repeatMode)) {
                if (item.repeatMode.equals(IFlareConstant.MODE_RESTART)) {
                    objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
                } else if (item.repeatMode.equals(IFlareConstant.MODE_REVERSE)) {
                    objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                }
            }

            if (!TextUtils.isEmpty(item.interpolator)) {
                if (item.interpolator.equals(IFlareConstant.INTERPOLATOR_LINEAR)) {
                    objectAnimator.setInterpolator(new LinearInterpolator());
                } else if (item.interpolator.equals(IFlareConstant.INTERPOLATOR_ACCELERATE)) {
                    objectAnimator.setInterpolator(new AccelerateInterpolator());
                } else if (item.interpolator.equals(IFlareConstant.INTERPOLATOR_DECELERATE)) {
                    objectAnimator.setInterpolator(new DecelerateInterpolator());
                } else if (item.interpolator.equals(IFlareConstant.INTERPOLATOR_ACCELERATE_DECELERATE)) {
                    objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                }
            }
            if (!TextUtils.isEmpty(item.evaluator)) {
                // TODO: 16/2/26
            }
        }

        return objectAnimator;
    }
}
