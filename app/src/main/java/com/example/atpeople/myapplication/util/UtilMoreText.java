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
     * 计算文字行数，没有超出则返回
     */
    public void createString() {
        mTextView.setText(mOriMsg);
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                int lines = mTextView.getLineCount();
                // Log.e(TAG, "显示行数: " + mTextView.getLineCount());
                if (lines > mLines) {
                    //必须设置否则无效
                    mTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    SpannableString content_more=compressedWithString();
                    mTextView.setText(content_more);
                } else {
                    mTextView.setText(mOriMsg);
                    return;
                }

            }
        });

    }
    /**
     * 重写点击事件
     * */
    class SpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (spread) {
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
            // 斜体
            ds.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            // 去除超链接的下划线
            ds.setUnderlineText(false);
        }
    }

    /**
     * 给textview设置行数
     * 并获取限制行数最后一个索引位置
     * 截取五个字符串，在末尾拼接+"更多"
     * 然后马上去掉行数限制
     * @return SpannableString
     */
    private SpannableString compressedWithString() {
        mTextView.setLines(mLines);
        // 取第三行的最后一个字符串的index，0是第一行
        int end = mTextView.getLayout().getLineEnd(mLines-1);
        // 截取需要拼接的文字长度+拼接的文字
        String resultText = mOriMsg.substring(0, end - mTextOpen.length()) + mTextOpen;
        mTextView.setLines(Integer.MAX_VALUE);
        // 给拼接的文字加入点击事件
        return getSpannableString(resultText, mTextOpen);
    }

    /**
     * 文字
     * @param text 所有文字
     * @param "更多" 拼接的文字
     * @return SpannableString
     * 给后面拼接的文字加入点击事件
     */
    private SpannableString getSpannableString(CharSequence text, String switchStr) {
        SpannableString spanableInfo = new SpannableString(text);
        spanableInfo.setSpan(new UtilMoreText.SpanTextClickable(), text.length() - switchStr.length(), text.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }
}
