package com.example.atpeople.myapplication.youtuapi.model;

import java.util.List;

/**
 * Create by peng on 2019/8/1
 */
public class BusinessCardInfo {
    /**
    * 姓名 ，职位，公司，地址，邮箱，手机，QQ，微信
    * */

    private String name;
    private String en_name;
    private List<String> title;
    private List<String> company;
    private List<String> address;
    private List<String> email;
    private List<String> phone;
    private List<String> qq;
    private List<String> wechat;
    private List<String> website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getCompany() {
        return company;
    }

    public void setCompany(List<String> company) {
        this.company = company;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getQq() {
        return qq;
    }

    public void setQq(List<String> qq) {
        this.qq = qq;
    }

    public List<String> getWechat() {
        return wechat;
    }

    public void setWechat(List<String> wechat) {
        this.wechat = wechat;
    }

    public List<String> getWebsite() {
        return website;
    }

    public void setWebsite(List<String> website) {
        this.website = website;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }
}
