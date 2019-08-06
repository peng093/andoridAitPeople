package com.example.atpeople.myapplication.atpeople;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atpeople.model.AtBean;
import com.example.atpeople.myapplication.ui.ActivityAlert;
import com.example.atpeople.myapplication.ui.activityalert.MainViewModel;
import com.example.atpeople.myapplication.util.AitpeopleUtil;
import com.example.atpeople.myapplication.util.UtilMoreText;
import com.example.atpeople.myapplication.util.ViewSpan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/7/24
 */
public class AitPeople extends AppCompatActivity {
    private static final String TAG = "AitPeople";
    /**
     * 原理是这样,@的时候跳转到第二个页面,然后返回的时候拼接字符串格式如: [@test2,99]
     * 拿到字符串之后呢,先把字符串处理,取出里面的id和name,分解来,并截取name和id转整形
     * 然后生成Span,并设置点击事件,返回SpannableString
     */
    @BindView(R.id.copy_wechat)
    EditText mCopyWeChat;
    @BindView(R.id.bt_add)
    Button tv_text;
    @BindView(R.id.show_tv)
    TextView show_tv;
    @BindView(R.id.bt_haha)
    Button bt_haha;
    @BindView(R.id.tv_folding)
    TextView tv_folding;


    private final String mMentionTextFormat = "{[%s, %s]}";
    static List<AtBean> aitList = new ArrayList<>();

    public static AitPeople instance;

    private String msg ="在Android开发中，有许多信息展示需要通过TextView来展现有许多" +
            "信息展示需要通过TextView来展现有许多信息展示需要通过TextView来展现有许多信" +
            "息展示需要通过TextView来展现，如果只是普通的信息展现，使用TextView setText(CharSequence " +
            "str)设置即可，但是当在TextView里的这段内容需要截取某一部分字段，可以被点击以及响应响应的操" +
            "作，这时候就需要用到SpannableString了，下面通过一段简单的代码实现部分文字被点击响应，及富文本表情的实现";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ait_poeple);
        ButterKnife.bind(this);
        instance = this;
        bt_haha.setBackgroundColor(R.color.material_red_900);
        initData();

    }

    private void initData() {
        tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddText();
            }
        });
        new UtilMoreText(tv_folding,msg,"...More","Pick up",5,Color.BLUE);
    }

    private void AddText() {
        String josnArray = "[1,2,3,4,5,6,7,888,666,999]";
        List<Integer> list = JSONObject.parseArray(josnArray, Integer.class);
        List<Integer> list2= GsonToList(josnArray, Integer.class);
        Log.e(TAG, "gson: " + list2.toString());
        Log.e(TAG, "json: " + list.toString());

        // 从资源文件取数组
        String[] testNum = getResources().getStringArray(R.array.testNum);
        List<String> str=Arrays.asList(getResources().getStringArray(R.array.testString));
        List<String> testList=Arrays.asList(testNum);
        String test="";
        for (String s : str) {
            test+=s;
        }


        MainViewModel viewModel = new MainViewModel("mvvm测试标题",test,"取消","确定",this);
        Intent intent=new Intent(this,ActivityAlert.class);
        intent.putExtra("data",viewModel);
        startActivity(intent);
    }

    public static <T> List<T> GsonToList(String gsonStr, Class<T> cls) {
        Gson gson=new Gson();
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonStr, new TypeToken<List<T>>() {}.getType());
        }
        return list;
    }



    public static SpannableString getSpan(String usrStr) {
        String name = usrStr.split(":")[0];
        name = name.substring(2, name.length());
        final String phone = usrStr.split(":")[1];
        final int id = Integer.valueOf(phone.substring(0, phone.length() - 2));

        SpannableString spanText = new SpannableString(name);
        // 把带有@的字符串,赋值到控件上
        TextView textView = new TextView(instance);
        textView.setText(name + " ");
        textView.setTextColor(Color.RED);
        // 再把带有@内容的控件创建一个ViewSpan(就是把文字转成整体图像)
        ViewSpan span = new ViewSpan(textView, textView.getMaxWidth());
        spanText.setSpan(span, 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //添加点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(instance, "id:" + id, Toast.LENGTH_SHORT).show();
            }
        };
        spanText.setSpan(clickableSpan, 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //mentionUser(99,"@QQ");
        }
    }

    public void getString(View view) {
        String test = "发给打个{[@haha,99]}梵蒂{[#haha,99]}冈地方{[@ppp,11]}妇{[@ASFFDdfdsfdsf4864864sdfsffsdfsf,55]}asdasdadadasdsda{[@萨达所大所多撒爱仕达大所多啊实打实大啊啊四大阿斯顿撒多,55]}";
        String tt = "@haha 发给#tttt @美滋滋 打个@haha 梵蒂冈地方@pppp 个 ";
        //激活点击事件
        show_tv.setMovementMethod(LinkMovementMethod.getInstance());
        // {[@haha,99]}替换为@haha ,并保存id和name
        String newString = getStr(ToDBC(test));
        // 匹配@name 或者#name,并记录起始位置
        List<AtBean> atBeanList = AitpeopleUtil.getAtBeanList(newString, aitList);
        // 给集合对象增加点击事件
        SpannableString spannableStr = AitpeopleUtil.getClickSpannableString(newString, atBeanList, this);
        show_tv.setText(spannableStr);
    }

    private static final String AT = "\"@[^,，：:\\\\s@]+\"";

    public void pipei(String str) {
        SpannableString spannableString = new SpannableString(str);
        Pattern pattern = Pattern.compile(AT);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            Log.e(TAG, "pipei: " + matcher.group());
        }
    }


    private static Pattern PATTERN = Pattern.compile("(?<=\\{\\[)(.+?)(?=\\]\\})");

    /**
     * @Author Peng
     * @Date 2019/6/11 17:50
     * @Describe 将符合格式{[@wawa,99]} 替换为文字@wawa ,并将名字和id存到集合对象中
     */
    public static String getStr(String content) {
        aitList.clear();
        String newStr = content;
        Matcher matcher = PATTERN.matcher(content);
        String text = "";
        while (matcher.find()) {
            String NEW = "{[" + matcher.group() + "]}";
            String[] str = matcher.group().split(",");
            // 拼接出一个@name ,带有空格格式
            String name = str[0] + " ";
            int id = Integer.valueOf(str[1]);
            AtBean bean = new AtBean(id, name, 0, 0);
            aitList.add(bean);
            if (text == "") {
                text = newStr.replace(NEW, name);
            } else {
                text = text.replace(NEW, name);
            }
        }
        return text == "" ? content : text;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }
}
