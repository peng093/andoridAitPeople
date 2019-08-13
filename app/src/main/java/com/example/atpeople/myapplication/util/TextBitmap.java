package com.example.atpeople.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Create by peng on 2019/8/13
 */
public class TextBitmap {
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
}
