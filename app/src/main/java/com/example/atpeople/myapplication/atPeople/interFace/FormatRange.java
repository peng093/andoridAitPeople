package com.example.atpeople.myapplication.atPeople.interFace;

import com.example.atpeople.myapplication.atPeople.Range;

/**
 * Create by peng on 2020/11/13
 */
public class FormatRange extends Range {
    private String uploadFormatText;
    private String showText;
    public FormatRange(int from, int to) {
        super(from, to);
    }

    public String getUploadFormatText() {
        return uploadFormatText;
    }

    public void setUploadFormatText(String uploadFormatText) {
        this.uploadFormatText = uploadFormatText;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }
}
