package com.example.atpeople.myapplication.ui.table;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by peng on 2019/9/18
 */
public class Table extends AppCompatActivity {
    private static final String TAG = "Table";
    @BindView(R.id.security)
    TextView security;
    @BindView(R.id.helpFeedback)
    TextView helpFeedback;
    @BindView(R.id.novice)
    TextView novice;
    @BindView(R.id.exitLogin)
    TextView exitLogin;
    @BindView(R.id.tv_test)
    TextView tv_test;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_table);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.security,R.id.helpFeedback,R.id.novice,R.id.exitLogin,R.id.tv_test})
    public void onViewClicked(View view) {
        Log.e(TAG, "onViewClicked: "+55555 );
        switch (view.getId()){
            case R.id.security:
                break;
            case R.id.helpFeedback:
                break;
            case R.id.novice:
                break;
            case R.id.exitLogin:
                break;
            case R.id.tv_test:
                break;

        }
    }
}
