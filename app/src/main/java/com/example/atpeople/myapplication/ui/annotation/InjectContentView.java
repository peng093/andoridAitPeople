package com.example.atpeople.myapplication.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)// 因为要在类上使用
@Retention(RetentionPolicy.RUNTIME)// 之前已经说过，要编译到.class文件里
public @interface InjectContentView {
    int value();  // 声明一个int参数
    //int value() default 0; // 也可以设一个默认值

    // InjectContentView被@interface修饰后，被当成注解，可以接InjectContentView（int value）
    // 传入的value会保存在当前InjectContentView中
}
