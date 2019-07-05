package com.example.atpeople.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SeconActivity extends AppCompatActivity {
    TextView tv_name;
    int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secon_view);
        tv_name=findViewById(R.id.tv_name);
        id = getIntent().getIntExtra("id", 0);
        tv_name.setText("id:"+id);
    }
}
