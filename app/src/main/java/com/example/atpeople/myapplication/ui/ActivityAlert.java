package com.example.atpeople.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.atpeople.myapplication.R;

/**
 * Create by peng on 2019/7/25
 */
public class ActivityAlert extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
    }
}
