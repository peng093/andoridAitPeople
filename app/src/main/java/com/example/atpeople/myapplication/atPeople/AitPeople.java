package com.example.atpeople.myapplication.atPeople;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.interFace.FormatRange;
import com.example.atpeople.myapplication.atPeople.interFace.InsertData;
import com.example.atpeople.myapplication.atPeople.model.AtBean;
import com.example.atpeople.myapplication.atPeople.model.Topic;
import com.example.atpeople.myapplication.atPeople.model.User;
import com.example.atpeople.myapplication.customview.AitEditText;
import com.example.atpeople.myapplication.util.AitpeopleUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by peng on 2019/7/24
 */
public class AitPeople extends AppCompatActivity {
    private static final String TAG = "AitPeople";
    @BindView(R.id.copy_wechat)
    AitEditText et_view;
    @BindView(R.id.bt_add)
    Button tv_text;
    @BindView(R.id.show_tv)
    TextView show_tv;
    @BindView(R.id.bt_haha)
    Button bt_haha;
    @BindView(R.id.tv_folding)
    TextView tv_folding;
    // <<左移运算，>>右移bai运算 首先把1转为二进制数字,二进制数字向左移动2或3位，后面补0 最后将得到的二进制数字转回对应类型的十进制数
    public static final int REQUEST_USER_APPEND = 1 << 2;   // 1 << 2=4
    public static final int REQUEST_TAG_APPEND = 1 << 3;    // 1 << 3=8


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ait_poeple);
        ButterKnife.bind(this);
        bt_haha.setBackgroundColor(R.color.material_red_900);
        initView();
    }

    private void initView() {
        et_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 第一个字符对应的start为0
                Editable editable = et_view.getText();
                // 只有删除文字 才会出现start < editable.length()
                if (start < editable.length()) {
                    int end = start + count;
                    // 删除之前的数量count 删除后的数量after
                    int offset = after - count;
                    whenDelText(start,end,offset);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(s)) {
                    char mentionChar = s.toString().charAt(start);
                    int selectionStart = et_view.getSelectionStart();
                    if (mentionChar == '@') {
                        startActivityForResult(UserList.getUserListIntent(AitPeople.this), REQUEST_USER_APPEND);
                        // 这里是把刚刚输入的@删除掉
                        et_view.getText().delete(selectionStart - 1, selectionStart);
                    } else if (mentionChar == '#') {
                         startActivityForResult(TopicList.getTopicListIntent(AitPeople.this), REQUEST_TAG_APPEND);
                        et_view.getText().delete(selectionStart - 1, selectionStart);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 监听删除按键，如果删除的是文字块，利用et_view.getText().delete进行删除文字块
//        et_view.setOnKeyListener((v, keyCode, event) -> {
//           if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                return false;
//            }else if(event.getAction() == KeyEvent.ACTION_UP){
//               if(keyCode==KeyEvent.KEYCODE_DEL){
//                   int selectionStart = et_view.getSelectionStart();
//                   int selectionEnd = et_view.getSelectionEnd();
//                   Range closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd);
//                   if(closestRange!=null){
//                       int to=closestRange.getTo()>et_view.getText().length()?et_view.getText().length():closestRange.getTo();
//                       et_view.getText().delete(closestRange.getFrom(),to);
//                       return true; // 如果是文字块，删除事件消费掉，不再继续传递
//                   }
//                   return false;
//               }
//               return false;
//           }
//           return true;
//        });
        // 监听光标发生改变，不能让光标插入到文字块中
        et_view.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void sendAccessibilityEvent(View host, int eventType) {
                super.sendAccessibilityEvent(host, eventType);
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED){

                }
            }
        });
    }

    /**
     * 每删除一个字符，都要遍历缓存队列，判断是否是删除了队列中的数据
     * 如果删除是文字块前面的文字，对于后面的文字块要往前移位
     * 如果是删除文字块，则把文字块在缓存列表删除后，对于后面的文字块要往前移位
     * @param start
     * @param end
     * @param offset
     */
    private void whenDelText(int start, int end,int offset){
        Iterator iterator = et_view.mRangeManager.iterator();
        while (iterator.hasNext()) {
            Range range = (Range) iterator.next();
            // 判断起始位置是否包裹了文字块，如果包裹了，则把文字块相关信息在内存列表删除
            if (range.isWrapped(start, end)) {
                iterator.remove();
                continue;
            }
            // 将end之后的span，挪动offset个位置
            if (range.getFrom() >= end) {
                range.setOffset(offset);
            }
        }
    }

    @OnClick({R.id.bt_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_add:
//                String formatStr = getUploadFormatText();
//                Log.e(TAG, "formatStr: " + formatStr);
//                et_view.getText().delete(3,9);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case REQUEST_USER_APPEND:
                    User user = (User) data.getSerializableExtra(UserList.RESULT_USER);
                    insertText(user);
//                    et_view.getText().insert(0, "");
                    break;
                case REQUEST_TAG_APPEND:
                    Topic topic = (Topic) data.getSerializableExtra(TopicList.RESULT_TOPIC);
                    insertText(topic);
//                    et_view.getText().insert(topic);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 把选中用户或话题 插入输入框
     *
     * @param insertData
     */
    private void insertText(InsertData insertData) {
        if (insertData == null) return;
        String showText = insertData.showText();
        String uploadFormatText = insertData.uploadFormatText();
        int color = insertData.color();

        Editable editable = et_view.getText();
        int start = et_view.getSelectionStart();
        int end = start + showText.length();
        // 插入到指定位置
        editable.insert(start, showText);
        // 设置对应颜色
        editable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 保存文字块的起始位置+展示字符+发给后端的格式字符
        FormatRange range = new FormatRange(start, end);
        range.setUploadFormatText(uploadFormatText);
        et_view.mRangeManager.add(range);
    }

    /**
     * 获取上传给服务端的格式化数据
     * @return
     */
    private String getUploadFormatText() {
        String text = et_view.getText().toString();
        Collections.sort(et_view.mRangeManager);

        int lastRangeTo = 0;
        StringBuilder builder = new StringBuilder("");
        String newChar;
        for (FormatRange range : et_view.mRangeManager) {
            builder.append(text.substring(lastRangeTo, range.getFrom()));
            // 获取需要上传给后端的数据格式
            newChar = range.getUploadFormatText();
            builder.append(newChar);
            lastRangeTo = range.getTo();
        }
        builder.append(text.substring(lastRangeTo));
        return builder.toString();
    }


    public void getString(View view) {
        String text=getUploadFormatText();
        // 激活点击事件
        show_tv.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString spannableStr =AitpeopleUtil.getSpStr(text,this);
        show_tv.setText(spannableStr);
    }
}
