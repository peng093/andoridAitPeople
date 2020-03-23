package com.example.atpeople.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.atpeople.myapplication.main.MainActivity;
import com.example.atpeople.myapplication.util.particleview.ParticleView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by peng on 2019/7/16
 * 动态更换启动页，实现的思路如下：
 * 1、本地有个默认图片
 * 2、每次启动的时候联网检查是否有新的图片，如果有就下载到本地
 * 3、启动的时候判断一下，如果有缓存到本地的则显示，否则显示默认的
 */
public class LaunchActivity extends AppCompatActivity {
    @BindView(R.id.shimmer_tv)
    ShimmerTextView shimmer_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initWebView();
        requestReadAndWritePermission();
    }

    private void initWebView() {
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmer_tv);
        shimmer.setRepeatCount(0)
                .setDuration(2000)
                .setStartDelay(100)
                // 从左往右，或者从右往左
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setAnimatorListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
//        Observable.timer(5, TimeUnit.SECONDS)
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }
//    private void goToIndex(){
//        mPv1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 800);
//
//    }
    private void  isFirst(){
        SharedPreferences jame = getSharedPreferences("jame", 0);
        boolean isFirst = jame.getBoolean("isFirst", true);
        if(isFirst){
            SharedPreferences.Editor edit = jame.edit();
            edit.putBoolean("isFirst", false);
            edit.commit();
        }else {
            // goToIndex();
        }
    }
    private void requestReadAndWritePermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(!aBoolean){
                            Toast.makeText(LaunchActivity.this, "请开启权限！", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
