package com.example.atpeople.myapplication.ui.gesturepsw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.util.ToastUtil;
import com.lib.lock.gesture.activities.GestureSettingsActivity;
import com.lib.lock.gesture.activities.GestureVerifyActivity;
import com.lib.lock.gesture.content.SPManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/9/6
 */
public class GesturePswActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btn_settings_gesture_password)
    Button mBtnPswSettings;
    @BindView(R.id.btn_verify_gesture_password)
    Button mBtnVerifyPsw;
    @BindView(R.id.btn_remove)
    Button btn_remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_view);
        ButterKnife.bind(this);

        mBtnPswSettings.setOnClickListener(this);
        mBtnVerifyPsw.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_settings_gesture_password:

                if (hasPsw()) {
                    GestureVerifyActivity.launch(this,true);
                } else {
                    GestureSettingsActivity.launch(this);
                }

                break;

            case  R.id.btn_verify_gesture_password:
                GestureVerifyActivity.launch(this,false);
                break;
            case R.id.btn_remove:
                SPManager.getInstance().removePatternPSW();
                ToastUtil.makeText(this, "手势已删除");
                if (!hasPsw()) {
                    mBtnVerifyPsw.setVisibility(View.GONE);
                    btn_remove.setVisibility(View.GONE);
                }
        }
    }

    private boolean hasPsw() {
        return !TextUtils.isEmpty(SPManager.getInstance().getPatternPSW());
    }

    /**其他界面返回的时候，都会调用onResume()*/
    @Override
    protected void onResume() {
        super.onResume();
        if (hasPsw()) {
            mBtnVerifyPsw.setVisibility(View.VISIBLE);
            btn_remove.setVisibility(View.VISIBLE);
        }
    }
}
