package com.example.atpeople.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.luckyandyzhang.mentionedittext.MentionEditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
    * 原理是这样,@的时候跳转到第二个页面,然后返回的时候拼接字符串格式如: [@test2,99]
    * 拿到字符串之后呢,先把字符串处理,取出里面的id和name,分解来,并截取name和id转整形
     * 然后生成Span,并设置点击事件,返回SpannableString
    * */

    EditText mCopyWeChat;
    Button tv_text;
    TextView show_tv;
    public static MainActivity instance;

    private final String mMentionTextFormat = "{[%s, %s]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        mCopyWeChat = findViewById(R.id.copy_wechat);
        tv_text = findViewById(R.id.bt_add);
        show_tv= findViewById(R.id.show_tv);
        initData();
    }
    private void initData() {
        mCopyWeChat.setMovementMethod(LinkMovementMethod.getInstance());
//        mCopyWeChat.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    return CopyWeChatEditText.KeyDownHelper(mCopyWeChat.getText());
//                }
//                return false;
//            }
//        });

        tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddText();
            }
        });

    }
    private void AddText() {
        //注意添加需要自己拼接@ 符号
        SpannableString sps=MainActivity.getSpan("{[@娃哈哈:99]}");
        mCopyWeChat.getText().insert(mCopyWeChat.getSelectionEnd(),sps);
    }

    public static SpannableString getSpan( String usrStr){
        String name = usrStr.split(":")[0];
        name=name.substring(2,name.length());
        final String phone = usrStr.split(":")[1];
        final int id= Integer.valueOf(phone.substring(0,phone.length()-2)) ;

        SpannableString spanText = new SpannableString(name);

        TextView textView = new TextView(instance);// 把带有@的字符串,赋值到控件上
        textView.setText(name+" ");
        textView.setTextColor(Color.RED);
        ViewSpan span = new ViewSpan(textView,textView.getMaxWidth());// 再把带有@内容的控件创建一个ViewSpan(就是把文字转成整体图像)
        spanText.setSpan(span, 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //添加点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(instance, "id:"+id, Toast.LENGTH_SHORT).show();
            }
        };
        spanText.setSpan(clickableSpan,0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            //mentionUser(99,"@QQ");
        }
    }

    public void getString(View view) {
        String test="发给打个{[@haha,99]}梵蒂冈地方{[@ppp,11]}个";
        String tt="@haha 发给#tttt @美滋滋 打个@haha 梵蒂冈地方@pppp 个 ";
        //pipei(mCopyWeChat.getText().toString());


        List<AtBean> atBeanList = getAtBeanList(tt);
        SpannableString spannableStr = getClickSpannableString(tt, atBeanList);
        show_tv.setText(spannableStr);
        //激活点击事件
        show_tv.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private static final String AT = "\"@[^,，：:\\\\s@]+\"";
    public void pipei(String str){
        SpannableString spannableString = new SpannableString(str);
        Pattern pattern = Pattern.compile(AT);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            Log.e(TAG, "pipei: "+matcher.group());
        }
    }


    private List<AtBean> getAtBeanList(String str) {

        List<AtBean> atBeanList = new ArrayList<>();

        // 正则表达式
        //String NAME_RULE = "\\{\\[[^\\}]*\\]\\}";
        String NAME_RULE = "(@|#)[a-zA-Z_\u4e00-\u9fa5]{1,30} ";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(NAME_RULE);

        Matcher m = pattern.matcher(str);

        while (m.find()) {
            AtBean bean = new AtBean(m.group(), m.start(), m.end());
            atBeanList.add(bean);
            Log.i("Find AT String", bean.toString());
        }
        return atBeanList;
    }

    private class Clickable extends ClickableSpan implements View.OnClickListener {
        private View.OnClickListener mListener;
        private Context context;

        private Clickable(Context context,View.OnClickListener mListener) {
            this.context = context;
            this.mListener = mListener;
        }

        //设置显示样式
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));//设置颜色
            ds.setUnderlineText(false);//设置下划线
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
    private SpannableString getClickSpannableString(String str, List<AtBean> atBeanList) {
        SpannableString spannableStr = new SpannableString(str);
        for (final AtBean atBean : atBeanList) {
            spannableStr.setSpan(new Clickable(MainActivity.this,new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每个 @用户名 字符串的点击事件
                    Toast.makeText(MainActivity.this, "点击了 ————> " + atBean.getName(), Toast.LENGTH_SHORT).show();
                }
            }), atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStr.setSpan(new ForegroundColorSpan(atBean.getName().startsWith("@")?Color.BLUE:Color.RED), atBean.getStartPos(), atBean.getEndPos(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return spannableStr;
    }

}
