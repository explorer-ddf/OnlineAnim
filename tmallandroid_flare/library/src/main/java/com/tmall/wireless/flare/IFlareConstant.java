package com.tmall.wireless.flare;

/**
 * Created by ddf on 16/2/23.
 */
public interface IFlareConstant {
    //动画属性名
    String ATTR_ALPHA = "alpha";
    String ATTR_ROTATION = "rotation";
    String ATTR_ROTATION_X = "rotationX";
    String ATTR_ROTATION_Y = "rotationY";
    String ATTR_TRANSLATION_X = "translationX";
    String ATTR_TRANSLATION_Y = "translationY";
    String ATTR_TRANSLATION_Z = "translationZ";
    String ATTR_SCALE_X = "scaleX";
    String ATTR_SCALE_Y = "scaleY";
    String ATTR_COLOR = "backgroundColor";

    //页面名
    String PAGE_COUDAN = "supermarket_coudan";

    String MODE_RESTART = "0";
    String MODE_REVERSE = "1";

    int INFINITE = -1;

    //内置时间插值器
    String INTERPOLATOR_LINEAR = "Linear";
    String INTERPOLATOR_ACCELERATE = "Accelerate";
    String INTERPOLATOR_DECELERATE= "Decelerate";
    String INTERPOLATOR_ACCELERATE_DECELERATE= "AccelerateDecelerate";

    String TRIGGER_ONCLICK = "onClick";
    String TRIGGER_ONLONGCLICK = "onLongClick";
}
