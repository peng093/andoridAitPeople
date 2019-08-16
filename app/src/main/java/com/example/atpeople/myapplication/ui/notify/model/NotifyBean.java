package com.example.atpeople.myapplication.ui.notify.model;

/**
 * Create by peng on 2019/8/2
 */
public class NotifyBean {
    int imageId;
    private String titleId;
    private String typeId;

    public NotifyBean(int imageId, String titleId, String typeId) {
        this.imageId = imageId;
        this.titleId = titleId;
        this.typeId = typeId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
