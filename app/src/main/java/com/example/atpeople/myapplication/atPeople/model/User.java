package com.example.atpeople.myapplication.atPeople.model;

import android.graphics.Color;

import com.example.atpeople.myapplication.atPeople.interFace.InsertData;

import java.io.Serializable;

/**
 * Create by peng on 2020/11/13
 */
public class User implements Serializable, InsertData {
    private int userId;
    private String userName;
    private String userSex;

    public User(int userId, String userName, String userSex) {
        this.userId = userId;
        this.userName = userName;
        this.userSex = userSex;
    }

    @Override
    public String showText() {
        return "@"+userName ;
    }

    @Override
    public String uploadFormatText() {
        final String USER_FORMART = "{[%s, %s]}";
        return String.format(USER_FORMART,"@"+userName,userId);
    }

    @Override
    public int color() {
        return Color.MAGENTA;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }


}
