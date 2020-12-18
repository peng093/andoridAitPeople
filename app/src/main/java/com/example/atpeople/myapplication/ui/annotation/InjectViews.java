package com.example.atpeople.myapplication.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  //需要的对应不再是类，而是成员变量
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectViews {
    int value() default 0;
}
