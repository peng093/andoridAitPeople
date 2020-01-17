package com.example.atpeople.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.lib.lock.gesture.utils.ContextUtils;

/**
 * Create by peng on 2019/7/11
 */
public class AppStart extends Application {
    private static final String TAG = "AppStart";
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
        ContextUtils.init(this);
    }
    public static Context getContext() {
        return context;
    }
}
