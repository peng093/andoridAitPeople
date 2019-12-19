package com.example.atpeople.myapplication.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create by peng on 2019/12/17
 */
public class RemoveDuplicateUtil {
    // 使用 List<User> name = RemoveDuplicateUtil.replese("age", users);
    /**
     * @Author Peng
     * @Date 2019/12/17 9:27
     * @Describe 万能去重集合List<Object>
     * @param ~键名 指定键名去重
     * @param ~List<T>
     */
    public static <T> List<T> replese(String tag, List<T> sourUsers) {
        List<T> users = new ArrayList<>();
        Set<Object> tags = new HashSet<>();
        for (T sourUser : sourUsers) {
            // 拿到对象中对应键的值,并保存到集合
            Object valueByName =  getValueByKey(sourUser, tag);
            if (!tags.contains(valueByName)){
                tags.add(valueByName);
                users.add(sourUser);
            }
        }
        return users;
    }
    public static Object getValueByKey(Object obj, String key) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        // 得到类中的所有属性集合
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            // 设置些属性是可以访问的
            f.setAccessible(true);
            try {
                // 判断键是否相等,如果等就返回该键的值
                // System.out.println("单个对象的某个键的值==反射==" + f.get(obj));
                if (f.getName().endsWith(key)) {
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }
}
