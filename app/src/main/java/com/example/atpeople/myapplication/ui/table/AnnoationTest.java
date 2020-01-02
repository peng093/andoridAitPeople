package com.example.atpeople.myapplication.ui.table;

/**
 * Create by peng on 2020/1/2
 * 使用注解附加到变量上
 *
 * 1.什么是注解
 *  注解可以理解成一个标签，是给类、方法、变量、属性等加标签；
 * 2.注解有什么用
 * 1、提供信息给编译器： 编译器可以利用注解来探测错误和警告信息 比如 @Override提示子类要复写父类中被 修饰的方法
 * 2、编译阶段时的处理： 软件工具可以用来利用注解信息来生成代码、Html文档或者做其它相应处理。
 * 3、运行时的处理： 某些注解可以在程序运行的时候接受代码的提取
 */
public class AnnoationTest {
    @Peng(min=3,max = 6,description = "用户名长度在3-6个字符之间")
    private String name;
    @Peng(min=6,max = 10,description = "密码长度在6-10个字符之间")
    private String pasdword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasdword() {
        return pasdword;
    }

    public void setPasdword(String pasdword) {
        this.pasdword = pasdword;
    }
}
