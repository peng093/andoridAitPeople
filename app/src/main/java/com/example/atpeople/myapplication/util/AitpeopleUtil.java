package com.example.atpeople.myapplication.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.example.atpeople.myapplication.atPeople.model.AtBean;
import com.example.atpeople.myapplication.ui.circularProgressView.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by peng on 2019/7/5
 */
public class AitpeopleUtil {
    private static final String TAG = "AitpeopleUtil";
    private static Pattern PATTERN = Pattern.compile("(?<=\\{\\[)(.+?)(?=\\]\\})");

    /**
     * 将符合格式{[@wawa,99]} 替换为文字@wawa,并将名字和id存到集合对象中
     * @param content
     * @param activity
     * @return spannableString
     */
    public static SpannableString getSpStr(String content,final Activity activity) {
        List<AtBean> aitList = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(content);
        String text = "";
        while (matcher.find()) {
            String target = "{[" + matcher.group() + "]}";
            String[] str = matcher.group().split(",");
            String name = str[0];
            int id = Integer.valueOf(str[1].trim());
            // 把文字块内容保存到集合中，索引位置暂时不保存
            AtBean bean = new AtBean(id, str[0], 0, 0);
            aitList.add(bean);
            // 把大括号替换掉
            if (text == "") {
                text = content.replace(target, name);
            } else {
                text = text.replace(target, name);
            }
        }
        String lastStr=text == "" ? content : text;
        // 这里是把所有文字块全部替换为需要显示的文字，然后在找文字块索引
        getAtBeanList(lastStr,aitList);
        return getClickSpannableString(lastStr,aitList,activity);
    }

    /**
     * 查找文字块索引
     * @param str
     * @param aitList
     */
    public static void getAtBeanList(String str, List<AtBean>aitList) {
        int end=0;
        for (AtBean atBean : aitList) {
            int s1=str.indexOf(atBean.getName(),end); // 从上次发现文字块索引位置开始往后寻找
            if(s1==-1) continue;
            end=s1+atBean.getName().length();
            atBean.setStartPos(s1);
            atBean.setEndPos(end);
        }
    }


    /**
     * 给文字块集合设置点击事件
     * @param str
     * @param atBeanList
     * @param activity
     * @return
     */
    public static SpannableString getClickSpannableString(String str, List<AtBean> atBeanList, final Activity activity) {
        SpannableString spannableStr = new SpannableString(str);
        for (final AtBean atBean : atBeanList) {
            Clickable clickableSpan=new Clickable(v -> {
                Intent intent = new Intent(activity.getApplicationContext(), CircularProgressView.class);
                intent.putExtra("id", atBean.getId());
                activity.startActivity(intent);
            },atBean.getName().startsWith("@")?Color.RED:Color.GREEN);
            spannableStr.setSpan(clickableSpan, atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStr;
    }

    public static class Clickable extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener mListener;
        int color;
        public Clickable(View.OnClickListener mListener,int _color) {
            this.mListener = mListener;
            this.color=_color==0?Color.BLUE : _color;
        }

        // 设置显示样式
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);         // 设置颜色
            ds.setUnderlineText(false); // 设置下划线
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
