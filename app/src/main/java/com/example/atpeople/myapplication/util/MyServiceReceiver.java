package com.example.atpeople.myapplication.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Create by peng on 2020/3/23
 */
public class MyServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "MyServiceReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //android.content.BroadcastReceiver.getResultCode()方法
//                public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
//                public static final int RESULT_ERROR_NO_SERVICE = 4;
//                public static final int RESULT_ERROR_NULL_PDU = 3;
//                public static final int RESULT_ERROR_RADIO_OFF = 2;
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    Log.e(TAG, "短信发送成功");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // 普通错误
                    Log.e(TAG, "短信发送失败");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.e(TAG, "服务当前不可用");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Log.e(TAG, "没有提供pdu");
                    break;

                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Log.e(TAG, "无线广播被明确地关闭");
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
