package com.example.atpeople.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.atpeople.myapplication.AppStart;

/**
 * SharedPreferences:共享偏好，用来做数据存储，通过xml，存放标记性数据和设置信息
 */
public class SPUtil {
    /** 私有变量,只能通过当前类的公共方法访问*/
    private SharedPreferences sp;
    private static class SingletonHolder {
        // 声明单例的变量
        private static final SPUtil INSTANCE=new SPUtil();
    }
    private SPUtil() {
        sp= AppStart.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
    }
    /** 获取当前类的单一实例对象*/
    public static final SPUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }



    /**
     * 写入Boolean变量至sharedPreferences中
     *
     * @param context 上下文环境
     * @param key     存储节点名称
     * @param value   存储节点的值
     */
    public void putBoolean(String key, Boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean标识从sharedPreferences中
     *
     * @param context 上下文环境
     * @param key     存储节点名称
     * @param value   没有此节点的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public boolean getBoolean(String key, Boolean value) {
        return sp.getBoolean(key, value);
    }

    /**
     * 写入String变量至sharedPreferences中
     *
     * @param context 上下文环境
     * @param key     存储节点名称
     * @param value   存储节点的值String
     */
    public void putString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }


    /**
     * 读取String标识从sharedPreferences中
     *
     * @param context  上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点的默认值
     * @return 返回默认值或者此节点读取到的结果
     */
    public String getString( String key, String defValue) {
        return  sp.getString(key, defValue);
    }

    /**
     * 写入int变量至sharedPreferences中
     *
     * @param context 上下文环境
     * @param key     存储节点名称
     * @param value   存储节点的值String
     */
    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }


    /**
     * 读取int标识从sharedPreferences中
     *
     * @param context  上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点的默认值
     * @return 返回默认值或者此节点读取到的结果
     */
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    /**
     * 从sharedPreferences中移除指定节点
     *
     * @param context 上下文环境
     * @param key     需要移除节点的名称
     */
    public void remove(String key) {
        sp.edit().remove(key).commit();
    }

}
