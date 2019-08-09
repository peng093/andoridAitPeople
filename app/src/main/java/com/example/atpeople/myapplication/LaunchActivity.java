package com.example.atpeople.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.atpeople.myapplication.main.MainActivity;
import com.example.atpeople.myapplication.util.particleview.ParticleView;
import com.tbruyelle.rxpermissions2.RxPermissions;



import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by peng on 2019/7/16
 */
public class LaunchActivity extends AppCompatActivity {
    private ParticleView mPv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mPv1 = (ParticleView) findViewById(R.id.pv_1);
        // 前部分主文字
        mPv1.mHostText="心有所想";
        mPv1.mParticleText="目有所见";
        mPv1.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                goToIndex();
            }
        });
        mPv1.startAnim();
        requestReadAndWritePermission();
    }


    private void goToIndex(){
        mPv1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 800);

    }
    private void  isFirst(){
        SharedPreferences jame = getSharedPreferences("jame", 0);
        boolean isFirst = jame.getBoolean("isFirst", true);
        if(isFirst){
            SharedPreferences.Editor edit = jame.edit();
            edit.putBoolean("isFirst", false);
            edit.commit();
        }else {
            goToIndex();
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
