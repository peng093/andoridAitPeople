package com.example.atpeople.myapplication.ui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import com.example.atpeople.myapplication.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by peng on 2020/3/19
 */
public class SendSmsActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText edPhone;
    @BindView(R.id.tv_content)
    EditText edContent;
    @BindView(R.id.bt_send)
    Button bt_send;

    private static final int SEND_SMS = 100;

    String SEND_SMS_ACTION = "3";
    String BACK_SMS_ACTION = "4";

    BroadcastReceiver liste_send_status= new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            //根据结果码判断是否发送成功
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    ToastUtil.makeText(SendSmsActivity.this, "短信发送成功");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // 普通错误
                    ToastUtil.makeText(SendSmsActivity.this, "短信发送失败");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    ToastUtil.makeText(SendSmsActivity.this, "服务当前不可用");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    ToastUtil.makeText(SendSmsActivity.this, "没有提供pdu");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    ToastUtil.makeText(SendSmsActivity.this, "无线广播被明确地关闭");
                    break;

            }
        }
    };
    BroadcastReceiver liste_back_status=new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "对方接收到短信!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getBaseContext(), "对方接收短信失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_rtsp;
    }

    @Override
    protected void initView() {
        // 注册自己发送短信的广播：
        registerReceiver(liste_send_status, new IntentFilter(SEND_SMS_ACTION));
        // 注册对方接受到短信的广播：
        registerReceiver(liste_back_status, new IntentFilter(BACK_SMS_ACTION));
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
                    Toast.makeText(SendSmsActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void sendSMSS() {
        // 会发送广播，
        PendingIntent send= PendingIntent.getBroadcast(SendSmsActivity.this, 0, new Intent(SEND_SMS_ACTION), 0);
        // 如果对方接受到短信 会发送广播
        PendingIntent reviced= PendingIntent.getBroadcast(SendSmsActivity.this, 0, new Intent(BACK_SMS_ACTION), 0);

        String content = edContent.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
            SmsManager smsManager = SmsManager.getDefault();
            // 将短信内容分块，发送一条短信最多能够发送70个中文字符，超过这个值系统会将短信内容分为多块进行发送。
            ArrayList<String> list = smsManager.divideMessage(content);
            // 分条进行发送。
            for (int i = 0; i < list.size(); i++) {
                String co=list.get(i);
                smsManager.sendTextMessage(phone, "aaa", co, send, reviced);
            }
        } else {
            Toast.makeText(SendSmsActivity.this, "手机号或内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册自定义 Receiver
        unregisterReceiver(liste_send_status);
        unregisterReceiver(liste_back_status);
    }

}
