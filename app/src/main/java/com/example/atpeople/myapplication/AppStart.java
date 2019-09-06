package com.example.atpeople.myapplication;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lib.lock.gesture.utils.ContextUtils;

/**
 * Create by peng on 2019/7/11
 */
public class AppStart extends Application {

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
