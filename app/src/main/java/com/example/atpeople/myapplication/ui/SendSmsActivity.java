package com.example.atpeople.myapplication.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.broadcast.SmsBroadcast;
import com.example.atpeople.myapplication.callback.BaseConfigCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;

import static com.example.atpeople.myapplication.constant.Constant.BACK_SMS_ACTION;
import static com.example.atpeople.myapplication.constant.Constant.READ_SMS;
import static com.example.atpeople.myapplication.constant.Constant.SEND_SMS;
import static com.example.atpeople.myapplication.constant.Constant.SEND_SMS_ACTION;

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
    @BindView(R.id.bt_read)
    Button bt_read;

    private SmsBroadcast mSmsBroadcastReceiver;

    @Override
    protected int initLayout() {
        return R.layout.activity_rtsp;
    }

    @Override
    protected void initView() {
        mSmsBroadcastReceiver=new SmsBroadcast(this, new BaseConfigCallBack() {
            @Override
            public void sendSuccess(String data) {
                Log.e(TAG, "短信发送成功==axonId: "+data);
            }

            @Override
            public void receiveSuccess(String data) {
                Log.e(TAG, "对方接收到短信==axonId: "+data);
            }
        });
    }

    @Override
    protected void initData() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(SendSmsActivity.this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SendSmsActivity.this, new String[]{Manifest.permission.READ_SMS}, READ_SMS);
                        return;
                    } else {
                        readSMS();
                    }
                }
            }
        });
    }

    private void readSMS() {
        Uri SMS_INBOX = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type","read"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("ooc", "************cur == null");
            return;
        }
        List<Map> list=new ArrayList<>();
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            String date = cur.getString(cur.getColumnIndex("date"));// 日期
            String type = cur.getString(cur.getColumnIndex("type"));// 1接收的 2.自己发送的
            String read = cur.getString(cur.getColumnIndex("read"));// 0未读 1已读
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", type);
            map.put("num", number);
            map.put("date", date);
            map.put("msg", body);
            map.put("read", read);
            list.add(map);
        }
        Log.e(TAG, "readSMS: "+list);
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
            case READ_SMS:
                readSMS();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void sendSMSS() {
        // 会发送广播SEND_SMS_ACTION要跟注册时候，匹配
        Intent ss=new Intent(SEND_SMS_ACTION);
        ss.putExtra("axonId","6666");
        PendingIntent send= PendingIntent.getBroadcast(SendSmsActivity.this, 0,ss, 0);
        // 如果对方接受到短信 会发送广播
        Intent rr=new Intent(BACK_SMS_ACTION);
        rr.putExtra("axonId","牛逼111");
        PendingIntent reviced= PendingIntent.getBroadcast(SendSmsActivity.this, 0,rr, 0);

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
        unregisterReceiver(mSmsBroadcastReceiver);
    }

}
