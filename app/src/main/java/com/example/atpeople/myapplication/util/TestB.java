package com.example.atpeople.myapplication.util;

/**
 * Create by peng on 2019/12/17
 */
public class TestB {
    /**
     * text : AAA
     * age : 18
     */
    private String text;
    private int age;

    public TestB(String text, int age) {
        this.text = text;
        this.age = age;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
