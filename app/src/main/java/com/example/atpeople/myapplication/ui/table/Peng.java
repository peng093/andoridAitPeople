package com.example.atpeople.myapplication.ui.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by peng on 2020/1/2
 * @author wyp
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Peng {
    int max() default 0;
    int min() default 0;
    String description() default "";
}
