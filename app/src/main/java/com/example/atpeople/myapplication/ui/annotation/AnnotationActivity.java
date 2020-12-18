package com.example.atpeople.myapplication.ui.annotation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.util.InjectUtils;


@InjectContentView(R.layout.annotation)
public class AnnotationActivity extends AppCompatActivity {

    @InjectViews(R.id.tv_main)
    private TextView tv_main;

    @InjectViews(R.id.btn_login)
    private Button btn_login;

    @InjectViews(R.id.btn_logoff)
    private Button btn_logoff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这行代码什么意思呢？用过xUtils的人都知道，也需要有这么一行代码
        // 自定义注解实现的逻辑都在InjectAll实现了
        InjectUtils.InjectAll(this);

        tv_main.setText("自定义注解绑定view666");
        btn_login.setText("登录再登录");
        btn_logoff.setText("注销在注销");
    }

    @IOnClick({R.id.btn_login, R.id.btn_logoff})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_logoff:
                Toast.makeText(this, "注销", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
