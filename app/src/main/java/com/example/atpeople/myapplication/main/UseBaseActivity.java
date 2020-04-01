package com.example.atpeople.myapplication.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.callback.AlertCallBack;
import com.example.atpeople.myapplication.ui.table.Table;

import butterknife.BindView;

/**
 * Create by peng on 2019/10/12
 */
public class UseBaseActivity extends BaseActivity {
    @BindView(R.id.bt_button)
    Button bt_button;
    @BindView(R.id.bt_log)
    Button bt_log;
    @BindView(R.id.bt_custom_title)
    Button bt_custom_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //以上设置一定要在 super.onCreate(savedInstanceState) 方法之前设置
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_use_base;
    }

    @Override
    protected void initView() {
        setTitle("通用顶部标题");
        setTopLeftButton(0, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setShowStatusBar(true);
        setStatusBarFontColor(this,Color.WHITE);
        setTopRightButton("我的",R.mipmap.titlebar_search,new OnClickListener() {
            @Override
            public void onClick() {
            // showToast("你点击了右上角的图标");
                showNormalAlertDialog("提示", "你点击了右上角的图标", new AlertCallBack() {
                    @Override
                    public void sure() {
                        showToast("你点击了确定");
                    }

                    @Override
                    public void cancle() {
                        showToast("你点击了取消");
                    }
                });
            }
        });

        //每秒只响应一次点击事件
        bt_button.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showToast("每秒只响应一次点击事件");
            }
        });
        //每秒可以重复响应点击事件
        bt_log.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                consoleLog("每秒可以重复响应点击事件");
            }
        });
        bt_custom_title.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getBaseContext(), Table.class));
            }
        });
    }

    @Override
    protected void initData() {

    }

}
