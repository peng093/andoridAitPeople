package com.example.atpeople.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Create by peng on 2019/7/30
 */
public class ArrowIcon extends View {
    public ArrowIcon(Context context) {

        super(context);

    }

    public ArrowIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 这是绘图方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Integer[] colors = {Color.BLUE, Color.RED, Color.GRAY};
        divide(canvas, Arrays.asList(colors));
    }

    /**
     * @Author Peng
     * @Date 2019/7/30 10:52
     * @Describe 根据颜色等分来扇形
     */
    private void divide(Canvas canvas, List<Integer> colors) {
        float x = getMeasuredWidth();
        float y = getMeasuredHeight();
        canvas.drawCircle(x/2,y/2,x/2,getPaint(Color.GREEN,dip2px(getContext(),4), true));
        canvas.drawLine(x/2,-y/2,x/2,y/2,getPaint(Color.BLACK,dip2px(getContext(),4), true));
    }


    public Paint getPaint(int color, int strokeWidth, boolean isFill) {
        Paint paint = new Paint();
        paint.setStyle(isFill ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setColor(color);
        // 透明度
        // paint.setAlpha(200);
        paint.setStrokeWidth(strokeWidth);
        return paint;
    }

    /**
     * @Author Peng
     * @Date 2019/7/30 11:07
     * @Describe 画直线
     */

    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        // 设置背景颜色
        canvas.drawColor(Color.WHITE);
        // 设置线宽
        paint.setStrokeWidth((float) 2.0);
        // 绘制直线
        canvas.drawLine(0, 0, 50, 50, paint);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
