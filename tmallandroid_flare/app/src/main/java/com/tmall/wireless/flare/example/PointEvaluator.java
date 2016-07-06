package com.tmall.wireless.flare.example;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by ddf on 16/3/16.
 */
public class PointEvaluator implements TypeEvaluator {
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
        float y = startPoint.y + fraction * (endPoint.y - startPoint.y);
        Point point = new Point((int)x, (int)y);
        return point;
    }
}
