package com.example.atpeople.myapplication.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseFragment;
import com.example.atpeople.myapplication.rxbinding.Login;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;

import butterknife.BindView;

/**
 * Create by peng on 2020/4/2
 */
public class RxJavaFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.list)
    ViewGroup mListView;

    Drawable bg_color;


    @Override
    protected int initLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        int radus= BackgroundColorUtil.dip2px(getActivity().getApplicationContext(),10);
        bg_color= BackgroundColorUtil.getRandomColorDrawable(radus,true,1);
        addDemo("Login", Login.class);
    }

    @Override
    protected void initData() {

    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,BackgroundColorUtil.dip2px(getActivity().getApplicationContext(),10),0,0);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackground(bg_color);
        b.setOnClickListener(this);
        mListView.addView(b);
    }

    @Override
    public void onClick(View view) {
        Class activityClass = (Class) view.getTag();
        startActivity(new Intent(getActivity().getApplicationContext(), activityClass));
    }
}
