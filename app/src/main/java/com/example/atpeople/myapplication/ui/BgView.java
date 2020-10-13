package com.example.atpeople.myapplication.ui;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.customview.BgColorTextView;

/**
 * Create by peng on 2020/10/13
 */
public class BgView extends BaseActivity {
    private BgColorTextView ctv0;
    private BgColorTextView ctv1;
    private BgColorTextView ctv2;
    @Override
    protected int initLayout() {
        return R.layout.bg_view;
    }

    @Override
    protected void initView() {
        ctv0 = (BgColorTextView) findViewById(R.id.text_0);
        ctv1 = (BgColorTextView) findViewById(R.id.text_1);
        ctv2 = (BgColorTextView) findViewById(R.id.text_2);

        ctv0.setCtvBackgroundColor(getResources().getColor(R.color.colorOrange));
        ctv1.setCtvBackgroundColor(getResources().getColor(R.color.colorRed));
        ctv2.setCtvBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void initData() {

    }
}
