package com.example.atpeople.myapplication.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.example.atpeople.myapplication.atpeople.model.AtBean;
import com.example.atpeople.myapplication.ui.circularprogressview.CircularProgressView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by peng on 2019/7/5
 */
public class AitpeopleUtil {
    private static final String TAG = "AitpeopleUtil";

    /**
     * @Author Peng
     * @Date 2019/7/5 8:35
     * @Describe 匹配@name 或者#name 的字符,并返回一个有name,有id,有起始位置,有结束位置的集合对象
     */
    private static String NAME_RULE = "(@|#)[a-zA-Z_\u4e00-\u9fa5_0-9]{1,100} ";
    public static List<AtBean> getAtBeanList(String str, List<AtBean>aitList) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(NAME_RULE);
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            for (AtBean atBean : aitList) {
                if(atBean.getName().equals(m.group())){
                    atBean.setStartPos(m.start());
                    atBean.setEndPos(m.end());
                }
            }

        }
        return aitList;
    }


    /**
     * @Author Peng
     * @Date 2019/7/5 8:33
     * @Describe 根据集合中的匹配对象进行增加点击事件
     */
    public static SpannableString getClickSpannableString(String str, List<AtBean> atBeanList, final Activity activity) {
        SpannableString spannableStr = new SpannableString(str);
        for (final AtBean atBean : atBeanList) {
            Clickable clickableSpan=new Clickable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CircularProgressView.class);
                    intent.putExtra("id", atBean.getId());
                    activity.startActivity(intent);
                }
            });
            spannableStr.setSpan(clickableSpan, atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // color
            spannableStr.setSpan(new ForegroundColorSpan(atBean.getName().startsWith("@")?
                            Color.parseColor("#508EF5"):Color.parseColor("#F46918")),
                    atBean.getStartPos(), atBean.getEndPos(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStr;
    }
    /**
     * @Author Peng
     * @Date 2019/7/5 9:21
     * @Describe 去掉缺省的下划线
     */
    private static class Clickable extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener mListener;

        private Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        //设置显示样式
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));//设置颜色
            ds.setUnderlineText(false);//设置下划线
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
