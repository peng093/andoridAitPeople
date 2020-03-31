package com.example.atpeople.myapplication.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.atpeople.myapplication.callback.BaseConfigCallBack;
import com.example.atpeople.myapplication.constant.Constant;
import com.example.atpeople.myapplication.ui.SendSmsActivity;
import com.example.atpeople.myapplication.util.ToastUtil;

/**
 * Create by peng on 2020/3/31
 * 一个页面只需要一个广播即可
 */
public class SmsBroadcast extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcast";
    private BaseConfigCallBack mCallBack;
    Context mContext;
    public SmsBroadcast(Context context, BaseConfigCallBack callBack) {
        mCallBack=callBack;
        mContext=context;
        IntentFilter filter = new IntentFilter();
        // 是否发送成功
        filter.addAction(Constant.SEND_SMS_ACTION);
        // 对方是否接受成功
        filter.addAction(Constant.BACK_SMS_ACTION);
        // this 只带SmsBroadcast被实例化
        context.registerReceiver(this,filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action==null){return;}
        switch (action){
            case Constant.SEND_SMS_ACTION:
                sendBack(getResultCode(),intent);
                break;
            case Constant.BACK_SMS_ACTION:
                acceptingState(getResultCode(),intent);
                break;
            default:
                break;
        }
    }


    private void sendBack(int code,Intent intent){
        switch (code){
            case Activity.RESULT_OK:
                String message = intent.getStringExtra("axonId");
                mCallBack.sendSuccess(message);
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                // 普通错误
                ToastUtil.makeText(mContext, "短信发送失败");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                ToastUtil.makeText(mContext, "服务当前不可用");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                ToastUtil.makeText(mContext, "没有提供pdu");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                ToastUtil.makeText(mContext, "无线广播被明确地关闭");
                break;
        }
    }

    private void acceptingState(int code,Intent intent){
        switch (code) {
            case Activity.RESULT_OK:
                String message = intent.getStringExtra("axonId");
                mCallBack.receiveSuccess(message);
                break;
            default:
                Toast.makeText(mContext, "对方接收短信失败!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
