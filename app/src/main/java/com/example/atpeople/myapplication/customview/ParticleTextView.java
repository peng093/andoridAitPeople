package com.example.atpeople.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yasic.library.particletextview.MovingStrategy.MovingStrategy;
import com.yasic.library.particletextview.Object.Particle;
import com.yasic.library.particletextview.Object.ParticleTextViewConfig;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ParticleTextView extends View {
    private Paint textPaint;
    private Bitmap bitmap;
    private int[][] colorArray;
    private Particle[] particles = null;
    private boolean isAnimationStart = false;
    private boolean isAnimationPause = false;

    private int columnStep;
    private int rowStep;
    private double releasing;
    private double miniJudgeDistance;
    private String targetText = null;
    private int textSize;
    private float particleRadius;
    private String[] particleColorArray = null;
    private MovingStrategy movingStrategy = null;
    private long delay = 1000;

    public ParticleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ParticleTextViewConfig tempConfig = new ParticleTextViewConfig.Builder().instance();
        setConfig(tempConfig);
    }

    private Paint initTextPaint() {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#3399ff"));
        textPaint.setAntiAlias(false);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        return textPaint;
    }

    public void setConfig(ParticleTextViewConfig config){
        if (config != null){
            this.columnStep = config.getColumnStep();
            this.rowStep = config.getRowStep();
            this.releasing = config.getReleasing();
            this.miniJudgeDistance = config.getMiniJudgeDistance();
            this.targetText = config.getTargetText();
            this.textSize = config.getTextSize();
            this.particleRadius = config.getParticleRadius();
            this.particleColorArray = config.getParticleColorArray();
            this.movingStrategy = config.getMovingStrategy();
            this.delay = config.getDelay();
        } else {
            Log.e("CONFIGERROR", "ParticleTextView Config is Null");
        }
    }

    public void startAnimation(){
        this.isAnimationStart = true;
    }

    public void stopAnimation() {
        this.isAnimationStart = false;
    }

    public boolean isAnimationPause(){
        return this.isAnimationPause;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        textPaint = initTextPaint();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(bitmap);
        textCanvas.drawText(targetText, centerX, centerY + textHeight / 2, textPaint);
        colorArray = new int[bitmap.getHeight()][bitmap.getWidth()];
        for (int row = 0; row < bitmap.getHeight(); row++) {
            for (int column = 0; column < bitmap.getWidth(); column++) {
                colorArray[row][column] = bitmap.getPixel(column, row);
            }
        }

        int red, green, blue;
        particles = new Particle[(colorArray.length / rowStep) * colorArray[0].length / columnStep];
        int index = 0;
        for (int i = 0; i < colorArray.length; i += rowStep) {
            for (int j = 0; j < colorArray[0].length; j += columnStep) {
                red = Color.red(colorArray[i][j]);
                green = Color.green(colorArray[i][j]);
                blue = Color.blue(colorArray[i][j]);
                if (red == 51 || green == 51 || blue == 51) {
                    particles[index] = new Particle(particleRadius, getRandomColor());
                    movingStrategy.setMovingPath(particles[index], getWidth(), getHeight(), new double[]{j ,i});
                    particles[index].setVx((particles[index].getTargetX() - particles[index].getSourceX()) * releasing);
                    particles[index].setVy((particles[index].getTargetY() - particles[index].getSourceY()) * releasing);
                    index++;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean hasParticleNotFinish = false;
        if (!isAnimationStart){
            return;
        }
        for (Particle item: particles){
            if (item != null){
                if (Math.abs(item.getVx()) < miniJudgeDistance && Math.abs(item.getVy()) < miniJudgeDistance){
                    item.setSourceX(item.getTargetX());
                    item.setSourceY(item.getTargetY());
                } else {
                    hasParticleNotFinish = true;
                }
            }else {
                break;
            }
        }

        if (!hasParticleNotFinish){
            isAnimationPause = true;
            for (Particle item: particles){
                if (item != null) {
                    item.updatePath();
                } else {
                    break;
                }
            }
            if (delay >= 0){
                rx.Observable.timer(delay, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                isAnimationPause = false;
                            }
                        });
            }
        }

        for (int i = 0; i < particles.length; i++) {
            canvas.save();
            if (particles[i] != null){
                canvas.save();
                textPaint.setColor(Color.parseColor(particles[i].getParticleColor()));
                canvas.drawCircle((int)particles[i].getSourceX(), (int)particles[i].getSourceY(), particles[i].getRadius(), textPaint);
                particles[i].setVx((particles[i].getTargetX() - particles[i].getSourceX()) * releasing);
                particles[i].setVy((particles[i].getTargetY() - particles[i].getSourceY()) * releasing);
                if (!isAnimationPause){
                    particles[i].setSourceX(particles[i].getSourceX() + particles[i].getVx());
                    particles[i].setSourceY(particles[i].getSourceY() + particles[i].getVy());
                }
            } else {
                canvas.restore();
                invalidate();
                break;
            }
        }
    }

    private String getRandomColor() {
        if (particleColorArray == null){
            String red, green, blue;
            Random random = new Random();

            red = Integer.toHexString(random.nextInt(256)).toUpperCase();
            green = Integer.toHexString(random.nextInt(256)).toUpperCase();
            blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

            red = red.length() == 1 ? "0" + red : red ;
            green = green.length() == 1 ? "0" + green : green ;
            blue = blue.length() == 1 ? "0" + blue : blue ;
            return "#" + red + green + blue;
        }else {
            return particleColorArray[(int) (Math.random() * particleColorArray.length)];
        }
    }
    public void refresh(){
        if(isAnimationPause()){
            stopAnimation();
        }
        startAnimation();
        invalidate();
    }
}
