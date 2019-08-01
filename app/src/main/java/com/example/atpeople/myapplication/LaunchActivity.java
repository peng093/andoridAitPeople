package com.example.atpeople.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.example.atpeople.myapplication.main.MainActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by peng on 2019/7/16
 */
public class LaunchActivity extends AhoyOnboarderActivity {
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestReadAndWritePermission();
        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Work Assistant", "Helps you find task targets quickly.", R.drawable.backpack);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Work Report", "Dynamic information editing, and complete information reporting.", R.drawable.chalk);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Share", "Share your work with the team.", R.drawable.chat);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);
        SharedPreferences jame = getSharedPreferences("jame", 0);
        boolean isFirst = jame.getBoolean("isFirst", true);
        if(isFirst){
            setOnboardPages(pages);
            SharedPreferences.Editor edit = jame.edit();
            edit.putBoolean("isFirst", false);
            edit.commit();
        }else {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void requestReadAndWritePermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
    @Override
    public void onFinishButtonPressed() {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
