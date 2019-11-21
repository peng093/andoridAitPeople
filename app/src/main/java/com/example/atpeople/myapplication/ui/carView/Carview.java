package com.example.atpeople.myapplication.ui.carView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/7/24
 */
public class Carview extends AppCompatActivity {
    @BindView(R.id.nsl_view)
    NestedScrollView nsl_view;
    @BindView(R.id.title_top)
    LinearLayout title_top;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.title_content)
    TextView title_content;

    View decorView;
    // 滑动距离
    int mDistance = 0;
    // 滑动的最大距离
    int maxDistance = 320;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_view);
        ButterKnife.bind(this);
        initScrollViewChangeTitle();
    }
    /**
     * @Author Peng
     * @Date 2019/7/24 18:02
     * @Describe
     */
    private void initScrollViewChangeTitle() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            decorView = getWindow().getDecorView();
            // 状态栏白色字体
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // 滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nsl_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mDistance += (scrollY - oldScrollY);
                    float alpha = 0.0f;
                    if (mDistance <= 0) {
                        title_top.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                        // 这里可以直接设置imageview的颜色,也可以直接替换图片
                        // iv_back.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        iv_back.setImageResource(R.mipmap.back);
                        title_content.setAlpha(0);
                        if (Build.VERSION.SDK_INT >= 21) {
                            // 状态栏白色字体
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        }
                    } else if (mDistance > 0 && mDistance <= maxDistance) {
                        alpha = mDistance * 1f / maxDistance;
                        //百分比
                        double dd = (alpha * 255);
                        title_top.setBackgroundColor(Color.argb((int) dd, 255, 255, 255));
                        iv_back.setImageResource(R.mipmap.back_black);
                        // 这里可以直接设置imageview的颜色,也可以直接替换图片
                        //iv_back.setImageTintList(ColorStateList.valueOf(Color.argb((int) dd, 0, 0, 0)));
                        title_content.setAlpha(alpha);
                        if (Build.VERSION.SDK_INT >= 21) {
                            // 状态栏字体灰色
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                    } else {
                        title_content.setAlpha(1);
                        title_top.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    }
                }
            });

        }
    }
}
