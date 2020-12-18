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

        tv_main.setText("绑定view的注解逻辑: 获取当前类的所有成员变量，然后过滤得到InjectView注解的变量，" +
                "拿到变量就能拿到他的注解及被注入的值，通过反射拿到当前类的findViewById方法，" +
                "调用这个方法并传入注入的值 返回一个Object对象，再把这个对象赋值给变量就完成绑定。\n 设置注解的好处在于，给你设置标记(注解)，那么获取你就会很容易，能统一实现相同的逻辑");
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
