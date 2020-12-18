package com.example.atpeople.myapplication.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.atpeople.myapplication.ui.annotation.BaseEvent;
import com.example.atpeople.myapplication.ui.annotation.InjectContentView;
import com.example.atpeople.myapplication.ui.annotation.InjectInvocationHandler;
import com.example.atpeople.myapplication.ui.annotation.InjectViews;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {
    public static void InjectAll(Activity activity) {
        InjectContentView(activity);
        InjectViews(activity);
        OnClick(activity);
    }

    /**
     * setContentView的注解处理
     * @param activity
     */
    public static void InjectContentView(Activity activity) {
        // 获取MainActivity类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取MainActivity类上的注解，传入的是InjectContentView.class，注意返回的是InjectContentView
        InjectContentView contentView = clazz.getAnnotation(InjectContentView.class);
        // 获取注解里的参数，也就是那个int的布局文件（R.layout.XXX）
        int value = contentView.value();
        try {
            // 通过反射获取Activity里的setContentView方法，参数是int型的布局文件id
            Method method = clazz.getMethod("setContentView", int.class);
            // 调用反射获取到的方法，value为上面通过注解获取到的layoutID
            method.invoke(activity, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * findViewById的注解处理
     * @param activity
     */
    public static void InjectViews(Activity activity) {
        // 获取MainActivity类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取全部的类中生命的的成员变量
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 获取对应的注解，传入的是InjectViews.class，注意返回的是InjectViews
            InjectViews annotation = declaredField.getAnnotation(InjectViews.class);
            // 这里要判断下注解是否为null，因为比如声明一个 public int i 时，是没有注解的
            if (annotation != null) {
                // 当注解不为null，获取其参数
                int value = annotation.value();
                try {
                    // 通过反射获取findViewById方法
                    Method method = clazz.getMethod("findViewById", int.class);
                    // 调用方法
                    Object view = method.invoke(activity, value);
                    // 这里要注意，类中的成员变量为private,故必须进行此操作，否则无法给控件赋值（即初始化的捆绑）
                    declaredField.setAccessible(true);
                    // 将初始化后的控件赋值到MainActivity里的对应控件上
                    declaredField.set(activity, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 点击事件的注解处理
     * @param activity
     */
    private static void OnClick(Activity activity) {
        // 获取MainActivity
        Class<? extends Activity> clazz = activity.getClass();
        // 获取MainActivity中所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 获取方法上对应的@IOnclick的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 通过annotationType获取注解@BaseEvent
                Class<? extends Annotation> annotationType = annotation.annotationType();
                // 需要判断是否为null
                if (annotationType != null) {
                    // 获取@IOnclick注解上的BaseEvent注解
                    BaseEvent baseEvent = annotationType.getAnnotation(BaseEvent.class);
                    // 需要判断是否为null，因为有的注解没有@BaseEvent
                    if (baseEvent != null) {
                        // 获取@BaseEvent的三个value
                        String callback = baseEvent.listenerCallback();
                        Class type = baseEvent.listenerType();
                        String setListener = baseEvent.setListener();
                        try {
                            // 通过反射获取方法，@IOnclick里的int[] value()不需要传参，所以参数省略
                            Method declaredMethod = annotationType.getDeclaredMethod("value");
                            // 调用方法，获取到@IOnclick的value，即两个button的id，参数省略
                            int[] valuesIds = (int[]) declaredMethod.invoke(annotation);
                            // 这个类稍后会给出代码，目的是拦截方法
                            InjectInvocationHandler handler = new InjectInvocationHandler(activity);
                            // 添加到拦截列表
                            handler.add(callback, method);
                            // 得到监听的代理对象
                            Proxy proxy = (Proxy) Proxy.newProxyInstance(type.getClassLoader(),
                                    new Class[]{type}, handler);
                            // 遍历所有button的id
                            for (int valuesId : valuesIds) {
                                View view = activity.findViewById(valuesId);
                                // 通过反射获取方法
                                Method listener = view.getClass().getMethod(setListener, type);
                                //  执行方法
                                listener.invoke(view, proxy);
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
