package com.example.atpeople.myapplication.util.pushRevicer;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;

/**
 * Create by peng on 2020/1/17
 */
public class HuaweiPushReceiver extends PushReceiver {
    private static final String TAG = "HuaweiPushReceiver";

    /**
     * 连接上华为服务时回调,可以获取token值
     * @param context
     * @param token
     * @param extras
     * */
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
        //MyApplication.PUST_CLIENTID_HW = token;//保存token，需要上传到应用服务器，以便应用服务器根据token发送消息
        String content = "华为推送get token and belongId successful, token = " + token + ",belongId = " + belongId;
        Log.e(TAG, content);
    }

    /**
     * 透传消息的回调方法
     * @param context
     * @param msg 推送消息内容
     * @param bundle
     * */
    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = new String(msg, "UTF-8");
            boolean bisRuning = false;
            if (content != null) {
//                bisRuning = !MyApplication.isRunInBackground;
//                Log.e(TAG,"收到推送消息 是否后台运行:"+ bisRuning);
//                //处理透传的消息，可以静默也可以通过NotificationManager来展示推送消息
//                NotificactionUtil.jsonPareChnnel(content,bisRuning);//自定义
                Log.e(TAG,"alarmMessage:"+content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 自定义的消息的回调方法
     * @param context
     * @param event
     * @param extras
     * */
    @Override
    public void onEvent(Context context, PushReceiver.Event event, Bundle extras) {
        Log.e(TAG,"event:"+event.toString()+" extras:"+extras);
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            //可以对extras.getString返回的键值对数据进行处理
            String content = "华为推送--------receive extented notification message: " + extras.getString(BOUND_KEY.pushMsgKey);
            Log.e(TAG, content);
        }
        super.onEvent(context, event, extras);
    }

    /**
     * 连接状态的回调方法
     * @param context
     * @param pushState
     * */
    @Override
    public void onPushState(Context context, boolean pushState) {
        try {
            String content = "华为推送---------The current push status： " + (pushState ? "Connected" : "Disconnected");
            Log.e(TAG, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
