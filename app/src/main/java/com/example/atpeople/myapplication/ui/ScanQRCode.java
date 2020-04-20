package com.example.atpeople.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;

/**
 * Create by peng on 2020/4/20
 * 扫描的的大概原理
 * 1.打开相机预览,监听帧率回调,解析每一帧,如果某一帧解析到数据就返回解析数据并停止相机
 * 2.直接通过图片,转bitmap后,解析数据消息
 */
public class ScanQRCode extends BaseActivity {
    @BindView(R.id.bt_scan)
    Button bt_scan;
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    private static final int REQUEST_CODE_SCAN=99;
    @Override
    protected int initLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initView() {
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQRCode.this, CaptureActivity.class);
                /**ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能*/
                ZxingConfig config = new ZxingConfig();
                config.setPlayBeep(false);//是否播放扫描声音 默认为true
                config.setShake(false);//是否震动  默认为true
                config.setDecodeBarCode(true);//是否扫描条形码 默认为true
                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            tv_msg.setText(content);
        }
    }
}
