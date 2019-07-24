package com.example.atpeople.myapplication.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

/**
 * Create by peng on 2019/7/24
 */
public class BackgroundColorUtil {
    /**
     * dp转px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @Author Peng
     * @Date 2019/7/24 11:20
     * @Describe 动态生成随机的背景色
     */
    public static GradientDrawable getRandomColorDrawable(int radius, boolean isFill, int strokeWidth){
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        float[] newRadius = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(newRadius);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
