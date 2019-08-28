package com.example.atpeople.myapplication.util;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Create by peng on 2019/4/24
 */
public class GsonUtil {
    private static Gson mGson = new Gson();
    /**
    * 将字符串转json实体对象
    * @param string
    * @param Class<T> 指定的实体类
    * @return <T>
    * */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return mGson.fromJson(jsonString, clazz);
    }
    /**
    * 将实体对象转化字符串
    * @param Object
    * @return string
    * */
    public static String toJson(Object obj) {
        return mGson.toJson(obj);
    }

    /**
     * 获取去最原始的数据信息
     * @return json data
     */
    public static String getOriginalFundData(Context context,String fileName) {
        InputStream input = null;
        try {
            //taipingyang.json文件名称
            input = context.getAssets().open(fileName);
            String json = convertStreamToString(input);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            //格式转换
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
