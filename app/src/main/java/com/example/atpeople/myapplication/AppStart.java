package com.example.atpeople.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.kongzue.baseokhttp.util.BaseOkHttp;
import com.kongzue.baseokhttp.util.Parameter;
import com.lib.lock.gesture.utils.ContextUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


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
        // 手势解锁
        ContextUtils.init(this);
        // 全局日志打印
        Logger.addLogAdapter(new AndroidLogAdapter());
        // 全局配置网络请求基本参数
        BaseOkHttp.DEBUGMODE = true;
        // BaseOkHttp.serviceUrl = "https://www.apiopen.top";
        BaseOkHttp.serviceUrl = "https://test3.hydrophis.cn:5632//v1/api";
        BaseOkHttp.overallHeader = new Parameter()
                .add("Charset", "UTF-8")
                .add("Content-Type", "application/json")
                .add("Accept-Encoding", "gzip,deflate")
                // token放在请求头中
                .add("authorization","b9b7ca83c4bf652484715e61ac38bdff")
        ;
    }
    public static Context getContext() {
        return context;
    }
}
