package com.example.atpeople.myapplication.ui.table;

/**
 * Create by peng on 2020/1/2
 */
public class NoBug {
    @JianCha
    public void suanShu() {
        System.out.println("1234567890");
    }

    @JianCha
    public void jiafa() {
        System.out.println("1+1=" + 1 + 1);
    }

    @JianCha
    public void jiefa() {
        System.out.println("1-1=" + (1 - 1));
    }

    @JianCha
    public void chengfa() {
        System.out.println("3 x 5=" + 3 * 5);
    }

    @JianCha
    public void chufa() {
        System.out.println("6 / 0=" + 6 / 0);
    }

    public void ziwojieshao() {
        System.out.println("保佑程序没有 bug!");
    }
}
