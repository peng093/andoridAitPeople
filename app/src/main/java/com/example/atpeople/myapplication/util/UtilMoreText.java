package com.example.atpeople.myapplication.util;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.atpeople.myapplication.AppStart;


/**
 * Create by peng on 2019/8/2
 */
public class UtilMoreText {
    private static final String TAG = "UtilMoreText";
    private TextView mTextView;
    private String mOriMsg;
    /**
     * 是否展开
     */
    private boolean spread = true;
    /**
     * 文字颜色
     */
    private int mSpanTextColor;
    /**
     * 设置图片
     */
    private Drawable mDrawableOpen;
    private Drawable mDrawableClose;
    /**
     * 文字
     */
    private String mTextOpen = "...展开";
    private String mTextClose = "收起";
    /**
     * 行数
     */
    private int mLines = 0;
    /**
     * @param textView  文本框
     * @param oriMsg    原始信息
     * @param textOpen  展开性质的文字
     * @param textClose 关闭性质的文字
     */
    public UtilMoreText(final TextView textView, final String oriMsg, String textOpen, String textClose, int lines,int color) {
        mTextView = textView;
        mOriMsg = oriMsg;
        mTextOpen = textOpen;
        mTextClose = textClose;
        mLines = lines;
        mSpanTextColor=color;
        createString();
    }
    /**
     * @param textView      文本框
     * @param oriMsg        原始文字
     * @param drawableOpen  展开图标
     * @param drawableColse 关闭图标
     */
    public UtilMoreText(final TextView textView, String oriMsg, Drawable drawableOpen, Drawable drawableColse,int lines) {
        mTextView = textView;
        mOriMsg = oriMsg + "XX";
        mDrawableOpen = drawableOpen;
        mDrawableClose = drawableColse;
        mLines = lines;
        createImg();
    }

    /**
     * 创建文本形式的结尾
     */
    public void createString() {
        mTextView.setText(mOriMsg);
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                int lines = mTextView.getLineCount();
                Log.e(TAG, "显示行数: " + mTextView.getLineCount());
                if (lines > mLines) {
                    //必须设置否则无效
                    mTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    mTextView.setText(compressedWithString());
                } else {
                    mTextView.setText(mOriMsg);
                    return;
                }

            }
        });

    }

    /**
     * 创建图片形似的结尾
     */
    public void createImg() {
        mTextView.setText(mOriMsg);
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                int lines = mTextView.getLineCount();
                Log.e(TAG, "显示行数: " + mTextView.getLineCount());
                if (lines > mLines) {
                    //必须设置否则无效
                    mTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    mTextView.setText(compressedWithImg());
                } else {
                    mTextView.setText(mOriMsg);
                    return;
                }

            }
        });


    }

    class spanImgClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            Log.i("colick", "onClick: ");
            TextView textView = (TextView) widget;
            if (spread) {//调用展开的方法
                spread = false;
                textView.setText(getSpannableImg(mOriMsg, mDrawableClose));

            } else {
                spread = true;
                textView.setText(getSpannableImg(textView.getText().subSequence(0, mOriMsg.length() >> 1), mDrawableOpen));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
        }
    }

    class SpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (spread) {//调用展开的方法
                spread = false;
                mTextView.setText(getSpannableString(mOriMsg + mTextClose, mTextClose));
            } else {
                spread = true;
                mTextView.setText(compressedWithString());
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mSpanTextColor);
            ds.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    private SpannableString compressedWithImg() {
        mTextView.setLines(mLines);
        // 取第三行的最后一个字符串的index，0是第一行
        int end = mTextView.getLayout().getLineEnd(mLines-1);
        mTextView.setLines(Integer.MAX_VALUE);
        // 利用index，截取至第mLines行，然后留出开关图片，
        return getSpannableImg(mOriMsg.substring(0, end), mDrawableOpen);
    }

    /**
     * 压缩字符串
     *
     * @return 富文本字符串
     */
    private SpannableString compressedWithString() {
        mTextView.setLines(mLines);
        // 取第三行的最后一个字符串的index，0是第一行
        int end = mTextView.getLayout().getLineEnd(mLines-1);
        // 利用index，截取至第mLines行，然后留出开关文字
        String resultText = mOriMsg.substring(0, end - 5) + mTextOpen;
        mTextView.setLines(Integer.MAX_VALUE);
        // 这里应该跟行数关联起来
        return getSpannableString(resultText, mTextOpen);
    }

    /**
     * 文字
     *
     * @param text 文字
     * @return SpannableString
     */
    private SpannableString getSpannableString(CharSequence text, String switchStr) {
        SpannableString spanableInfo = new SpannableString(text);
        spanableInfo.setSpan(new UtilMoreText.SpanTextClickable(), text.length() - switchStr.length(), text.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    /**
     * 设置富文本 图片
     *
     * @param text     文字
     * @param drawable 图片
     * @return SpannableString
     */
    private SpannableString getSpannableImg(CharSequence text, Drawable drawable) {
        int len = text.length();

        SpannableString spanableInfo = new SpannableString(text);
        int drawHeight = drawable.getMinimumHeight();
        drawable.setBounds(0, 0, drawHeight, drawHeight);
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        spanableInfo.setSpan(imageSpan, len - 5, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new UtilMoreText.spanImgClickable(), text.length() - 5, text.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }
}
