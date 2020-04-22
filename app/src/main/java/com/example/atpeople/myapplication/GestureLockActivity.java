package com.example.atpeople.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.customview.GestureLockView;
import com.example.atpeople.myapplication.main.MainActivity;
import com.example.atpeople.myapplication.util.SharedPreferencesUtil;

import butterknife.BindView;

/**
 * Create by peng on 2020/4/22
 */
public class GestureLockActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lock)
    GestureLockView gestureLockView;

    String firtt="";
    @Override
    protected int initLayout() {
        return R.layout.activity_gestrue_view;
    }

    @Override
    protected void initView() {
        String gestureLock_psw = SharedPreferencesUtil.getString(context, "gestureLock_psw", null);
        if(gestureLock_psw==null){
            tv_title.setText("请创建手势密");
        }else {
            tv_title.setText("请绘制正确的手势");
        }
        // 获取本地有没有保留的密码 有的话,则是验证
        gestureLockView.setLockValueCallBack(new GestureLockView.LockValueCallBack() {
            @Override
            public void valueCallBack(String value) {
                Log.d("TAG","value="+value);
                if (value.length()<4){
                    tv_title.setText("请至少连续绘制4个点");
                    return;
                }
                if(gestureLock_psw==null){
                    if(firtt.length()==0){
                        firtt=value;
                    }else {
                        if(!firtt.equals(value)){
                            tv_title.setText("与首次绘制不一样,请重新绘制");
                            return;
                        }
                        // 覆盖掉首次,并保存
                        firtt=value;
                        SharedPreferencesUtil.putString(context, "gestureLock_psw", firtt);
                        Intent intent = new Intent(GestureLockActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    if(gestureLock_psw.equals(value)){
                        Intent intent = new Intent(GestureLockActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        tv_title.setText("手势不正确,请重新绘制");
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

}