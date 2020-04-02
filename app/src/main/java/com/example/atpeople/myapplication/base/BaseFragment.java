package com.example.atpeople.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Create by peng on 2020/4/2
 */
public abstract class BaseFragment extends Fragment {
    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(initLayout(), container, false);
        ButterKnife.bind(this, mContentView);
        initView();
        initData();
        return mContentView;
    }
    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();
    public View getContentView() {
        return mContentView;
    }
    @Override
    public Activity getContext(){
        return getActivity();
    }
}
