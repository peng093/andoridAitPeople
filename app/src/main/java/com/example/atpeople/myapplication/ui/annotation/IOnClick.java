package com.example.atpeople.myapplication.ui.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)                         // 注解应用于其他注解上
@Retention(RetentionPolicy.RUNTIME)
@BaseEvent(setListener = "setOnClickListener",      // setOnClickListener为View.setOnClickListener
        listenerType = View.OnClickListener.class,  // 监听的类型为点击事件
        listenerCallback = "onClick")               // 这个onClick回调，即为setOnClickListener后回调的onClick
public @interface IOnClick {
    int[] value();// 因为一个方法可能与多个控件绑定
}
