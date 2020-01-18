package com.example.atpeople.myapplication.ui.textAnimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.customview.FlashTextView;

import butterknife.BindView;

/**
 * Create by peng on 2020/1/16
 */
public class CombinationAnimation extends BaseActivity {
    @BindView(R.id.tv_text)
    TextView tv_text;
    @BindView(R.id.tv_text2)
    FlashTextView tv_text2;
    @BindView(R.id.tv_text3)
    FlashTextView tv_text3;

    @Override
    protected int initLayout() {
        return R.layout.activity_combination;
    }

    @Override
    protected void initView() {
        setTitle("自定义组合动画");
        setTopLeftButton(0, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(tv_text, "translationX", -500f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tv_text, "scaleY", 1f, 3f, 1f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(tv_text, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(tv_text, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleY).with(fadeInOut).after(moveIn);
        animSet.setDuration(2000);
        animSet.start();

        // 监听动画变化过程
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                Log.d("TAG", "cuurent value is " + currentValue);
            }
        });
        anim.start();

        // 右侧逐个出现
        tv_text2.setText("Hello world", AnimationUtils.loadAnimation(this, R.anim.right_to_fade), 300);

        // 从透明到不透明
        tv_text3.setText("FMS V3.5.35", AnimationUtils.loadAnimation(this, R.anim.to_alpha), 300);
    }
}
