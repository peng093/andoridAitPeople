package com.example.atpeople.myapplication.ui.carview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.atpeople.myapplication.R;

import butterknife.ButterKnife;

/**
 * Create by peng on 2019/7/24
 */
public class Carview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_view);
        ButterKnife.bind(this);

    }
}
