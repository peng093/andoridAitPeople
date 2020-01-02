package com.example.atpeople.myapplication.ui.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.util.PictureUtil;
import com.example.atpeople.myapplication.util.TextBitmap;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/7/30
 */
public class CanvasView extends AppCompatActivity {
    @BindView(R.id.lly_lly)
    LinearLayout lly_lly;
    @BindView(R.id.iv_text)
    ImageView iv_text;
    @BindView(R.id.iv_text2)
    ImageView iv_text2;
    @BindView(R.id.iv_text3)
    ImageView iv_text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_view);
        ButterKnife.bind(this);
        Integer[] colors = {Color.BLUE, Color.RED, Color.GRAY};
        Drawable drawable =new BitmapDrawable(getBitmap(Arrays.asList(colors)));
        lly_lly.setBackground(drawable);
        int width=dip2px(50);
        int bgColor=Color.parseColor("#019FE8");
        iv_text.setImageBitmap(TextBitmap.getCircleTextBitmap("A",bgColor,width,width,Color.WHITE));
        iv_text2.setImageBitmap(TextBitmap.getRectTextBitmap("B",bgColor,width,width,Color.WHITE));
        PictureUtil.downPic(iv_text3,this);
    }
    public  int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public Bitmap getBitmap(List<Integer> colors) {
        int w = 320,h = 640;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        // 设置画笔的锯齿效果
        paint.setAntiAlias(true);

        Bitmap whiteBgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(whiteBgBitmap);

        int itemArcWidth = 360 / colors.size();
        int position = 0;
        for (Integer color : colors) {
            paint.setColor(color);
            canvas.drawArc(-400, -400, w+400, h+400,position, itemArcWidth, true, paint);
            position += itemArcWidth;
        }

        return getRoundedCornerBitmap(whiteBgBitmap,20f) ;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
