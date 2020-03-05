package com.example.atpeople.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;

import com.example.atpeople.myapplication.AppStart;
import com.example.atpeople.myapplication.R;

/**
 * Create by peng on 2019/8/13
 */
public class TextBitmap {
    public static int dip2px(float dipValue) {
        final float scale = AppStart.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * @Author Peng
     * @Date 2019/8/13 16:11
     * @Describe 画文字，圆形背景色
     */
    public static Bitmap getCircleTextBitmap(String content, int color, int w, int h, int textColor) {
        Bitmap whiteBgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(whiteBgBitmap);
        // 圆形背景
        Paint bgRect=new Paint();
        bgRect.setStyle(Paint.Style.FILL);
        bgRect.setColor(color);
        canvas.drawCircle(w/2,h/2,w/2,bgRect);
        Paint textPaint=new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(8);
        textPaint.setTextSize(50);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //计算baseline
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=h/2+distance;
        canvas.drawText(content, canvas.getClipBounds().centerX(), baseline, textPaint);
        return whiteBgBitmap;
    }

    /**
     * @Author Peng
     * @Date 2019/8/13 16:11
     * @Describe 画文字，矩形背景色
     */
    public static Bitmap getRectTextBitmap(String content,int color,int w,int h,int textColor) {
        Bitmap whiteBgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(whiteBgBitmap);
        //矩形背景
        Paint bgRect=new Paint();
        bgRect.setStyle(Paint.Style.FILL);
        bgRect.setColor(color);
        RectF rectF=new RectF(0, 0, w, h);
        canvas.drawRect(rectF, bgRect);
        Paint textPaint=new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(8);
        textPaint.setTextSize(50);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //计算baseline
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=rectF.centerY()+distance;
        canvas.drawText(content, rectF.centerX(), baseline, textPaint);
        return whiteBgBitmap;
    }

    /**
     * @Author Peng
     * @Date 2019/8/13 16:11
     * @Describe 画文字，矩形背景色
     */
    public static Bitmap getBgTextBitmap(String content,int resouedId,int w,int h,int textColor) {
        Bitmap whiteBgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(whiteBgBitmap);
        //矩形背景
        Paint bgRect=new Paint();
        bgRect.setStyle(Paint.Style.FILL);
        RectF rectF=new RectF(0, 0, w, h);
        canvas.drawRect(rectF, bgRect);
        Paint textPaint=new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(8);
        textPaint.setTextSize(50);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //计算baseline
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=rectF.centerY()+distance;
        canvas.drawText(content, rectF.centerX(), baseline, textPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(AppStart.getContext().getResources(), R.mipmap.ic_today, null);
        canvas.drawBitmap(bitmap,null,rectF,bgRect);
        return whiteBgBitmap;
    }

    /**
     * @Author Peng
     * @Date 2019/6/5 13:35
     * @Describe 动态的给控件加圆形或圆角的背景色,不需要创建xml文件
     * @param ~int 圆角,建议是控件高度的一一半
     * @param ~int 背景色
     * @param ~boolean 背景色透明，只显示边框？
     * @param ~int 边框
     * 使用方式 view.setBackgroundDrawable(getRoundRectDrawable(40, Color.parseColor("#5bc0de"), true, 10));
     */
    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth){
        //左上、右上、右下、左下的圆角半径
        float[] newRadius = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(newRadius);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
