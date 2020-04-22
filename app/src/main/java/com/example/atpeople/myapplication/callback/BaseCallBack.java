package com.example.atpeople.myapplication.callback;

/**
 * Create by peng on 2020/4/1
 */
public interface BaseCallBack<T>{
    void success(T t);
    void failed(Throwable e);
}
