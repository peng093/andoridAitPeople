package com.example.atpeople.myapplication.main;

import android.util.Log;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.util.ToastUtil;

/**
 * Create by peng on 2020/1/17
 */
public class NotifyDatailActivity extends BaseActivity {
    @Override
    protected int initLayout() {
        return R.layout.activity_use_base;
    }

    @Override
    protected void initView() {
        setTitle("通知详情");
        setTopLeftButton(0, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setShowStatusBar(true);
    }

    @Override
    protected void initData() {
        // 获取通知栏传入的参数
        String action = getIntent().getData().getQueryParameter("action");
//        String title = getIntent().getData().getQueryParameter("title");
//        String content = getIntent().getData().getQueryParameter("content");
        Log.e(TAG, "action=="+action);
        //Log.e(TAG, "title=="+title+",content=="+content);
        //ToastUtil.makeText(this,"title=="+title+",content=="+content);
    }
}
