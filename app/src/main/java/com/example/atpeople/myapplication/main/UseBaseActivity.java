package com.example.atpeople.myapplication.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.callback.BaseCallBack;
import com.example.atpeople.myapplication.callback.PermissionCallback;
import com.example.atpeople.myapplication.ui.table.Table;
import com.orhanobut.logger.Logger;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.bt_edit_alert)
    Button bt_edit_alert;
    @BindView(R.id.bt_custom_alert)
    Button bt_custom_alert;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 以上设置一定要在 super.onCreate(savedInstanceState) 方法之前设置
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_use_base;
    }

    @Override
    protected void initView() {
        setTitle("通用顶部标题");
        setTopLeftButton(0);
        setShowStatusBar(true);
        setStatusBarFontColor(this, Color.WHITE);
        setTopRightButton("我的", new OnClickListener() {
            @Override
            public void onClick() {
                showNormalAlertDialog("提示", "你点击了右上角的图标", new BaseCallBack() {
                    @Override
                    public void success(Object o) {
                        showToast("你点击了确定");
                    }

                    @Override
                    public void failed(Throwable e) {
                        showToast("你点击了取消");
                    }
                });
            }
        });

        // 每秒只响应一次点击事件
        bt_button.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // showToast("每秒只响应一次点击事件");
                Logger.e("每秒只响应一次点击事件");
            }
        });
        // 每秒可以重复响应点击事件
        bt_log.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                Logger.d("每秒可以重复响应点击事件");
            }
        });
        bt_custom_title.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getBaseContext(), Table.class));
            }
        });
        bt_edit_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditAlertDialog("请输入", "默认", new BaseCallBack<String>() {
                    @Override
                    public void success(String string) {
                        Logger.d("输入内容=="+string);
                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
            }
        });
        bt_custom_alert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                View vw= LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_alert_view, null);
                showCustomViewAlertDialog(vw, new BaseCallBack() {
                    @Override
                    public void success(Object o) {

                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
            }
        });

    }

    @OnClick({R.id.bt_request_permission})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.bt_request_permission:
                // 申请权限的时候,一般都是需要用到才申请,所以不建议一次性申请
                String[] pers={Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                requestPerission(pers, new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        showToast("同意获取权限");
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissions) {
                        Logger.e("拒绝的权限=="+ JSON.toJSONString(deniedPermissions));
                    }
                });
                break;
        }
    }

    @Override
    protected void initData() {

    }

}
