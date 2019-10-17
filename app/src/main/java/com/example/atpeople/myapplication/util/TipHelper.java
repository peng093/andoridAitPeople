package com.example.atpeople.myapplication.util;

import android.app.Activity;
import android.app.Service;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Create by peng on 2019/9/20
 */
public class TipHelper {
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    /**
     * @Author Peng
     * @Date 2019/9/20 11:39
     * @Describe 1.pattern这个数组中的元素，第0个表示等待时长，第1个表示震动时长，第2个等待时长，第3个震动时长
     */
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        VibrationEffect vibrationEffect= VibrationEffect.createOneShot(300,255);
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(vibrationEffect);
    }

}
