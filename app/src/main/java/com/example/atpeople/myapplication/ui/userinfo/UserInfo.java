package com.example.atpeople.myapplication.ui.userinfo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.atpeople.myapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/8/5
 */
public class UserInfo extends AppCompatActivity {
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        ButterKnife.bind(this);
        avatar.setImageURI(Uri.parse("res://com.huipu.myquarter/" + R.drawable.diagonallayout_hughjackman));
    }
}
