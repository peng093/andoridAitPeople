package com.example.atpeople.myapplication.ui.activityalert;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by peng on 2019/7/26
 */
public class MainViewModel implements Parcelable {
    private String title ;
    private String content ;
    private String cancle_text;
    private String sure_text;

    private Context mContext ;

    protected MainViewModel(Parcel in) {
        title = in.readString();
        content = in.readString();
        cancle_text = in.readString();
        sure_text = in.readString();
    }

    public static final Creator<MainViewModel> CREATOR = new Creator<MainViewModel>() {
        @Override
        public MainViewModel createFromParcel(Parcel in) {
            return new MainViewModel(in);
        }

        @Override
        public MainViewModel[] newArray(int size) {
            return new MainViewModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCancle_text() {
        return cancle_text;
    }

    public void setCancle_text(String cancle_text) {
        this.cancle_text = cancle_text;
    }

    public String getSure_text() {
        return sure_text;
    }

    public void setSure_text(String sure_text) {
        this.sure_text = sure_text;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public MainViewModel(String title, String content, String cancle_text, String sure_text, Context mContext) {
        this.title = title;
        this.content = content;
        this.cancle_text = cancle_text;
        this.sure_text = sure_text;
        this.mContext = mContext;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(cancle_text);
        dest.writeString(sure_text);
    }
}
