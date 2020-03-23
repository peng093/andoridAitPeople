package com.example.atpeople.myapplication.ui;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.util.MyServiceReceiver;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by peng on 2020/3/19
 */
public class RtspActivity extends BaseActivity {
    //    private VideoView videoView;
    @BindView(R.id.et_phone)
    EditText edContent;
    @BindView(R.id.tv_content)
    EditText edPhone;
    @BindView(R.id.bt_send)
    Button bt_send;

    private static final int SEND_SMS = 100;

    // 自定义ACTION常数 作为广播的IntentFilter识别常数
    private String SMS_SEND_ACTION = "SMS_SEND_ACTION";
    private String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

    @Override
    protected int initLayout() {
        return R.layout.activity_rtsp;
    }

    @Override
    protected void initView() {
//        videoView=findViewById(R.id.view);
//        MediaController mediaController = new MediaController(this);
//        videoView.setVideoURI(Uri.parse("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"));
//        videoView.setMediaController(mediaController);
//        videoView.start();
    }

    @Override
    protected void initData() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
                return;
            } else {
                sendSMSS();
                //已有权限
            }
        } else {
            //API 版本在23以下
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSS();
                } else {
                    // Permission Denied
                    Toast.makeText(RtspActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void sendSMSS() {
        String content = edContent.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,"adfada",content,null,null);
            // Toast.makeText(RtspActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RtspActivity.this, "手机号或内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    MyServiceReceiver mReceiver01;
    @Override
    protected void onResume() {
        super.onResume();
        //自定义IntentFilter为SMS_SEND_ACTION Receiver
        IntentFilter intentFilter;
        intentFilter = new IntentFilter(SMS_SEND_ACTION);
        mReceiver01= new MyServiceReceiver();
        registerReceiver(mReceiver01,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册自定义 Receiver
        unregisterReceiver(mReceiver01);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
