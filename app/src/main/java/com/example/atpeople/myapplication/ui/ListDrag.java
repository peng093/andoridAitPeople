package com.example.atpeople.myapplication.ui;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.callback.ItemDragHelperCallBack;
import com.example.atpeople.myapplication.ui.notify.NotifyAdapter;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;
import com.example.atpeople.myapplication.util.AitpeopleUtil;
import com.example.atpeople.myapplication.util.FilePathUtil;
import com.example.atpeople.myapplication.util.NotifyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Create by peng on 2020/10/26
 */
public class ListDrag extends BaseActivity {
    @BindView(R.id.rec_list)
    RecyclerView rec_list;
    @BindView(R.id.tv_path)
    TextView tv_path;


    String path = "";


    // 定义正则表达式
    private static final String AT = "@[\u4e00-\u9fa5\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";// 表情
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";// url

    @Override
    protected int initLayout() {
        return R.layout.activity_drag_list;
    }

    @Override
    protected void initView() {
        rec_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String text = "@张三 hahhah@李四#你好##合帮# 呵呵https://www.cocos.com/docs";
        String REGEX = "(" + AT + ")" + "|" + "(" + TOPIC + ")" + "|" + "(" + EMOJI + ")" + "|" + "(" + URL + ")";
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            final String tagert = matcher.group();
            Log.e(TAG, "tagert: "+tagert );
            if (tagert.startsWith("@")) {
                // 获取匹配位置
                int start = matcher.start();
                int end = matcher.end();
                AitpeopleUtil.Clickable clickableSpan=new AitpeopleUtil.Clickable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "点击了用户：" + tagert, Toast.LENGTH_LONG).show();
                    }
                }, Color.RED);
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (tagert.startsWith("#")) {
                int start = matcher.start();
                int end = matcher.end();
                AitpeopleUtil.Clickable clickableSpan=new AitpeopleUtil.Clickable(v ->
                        Toast.makeText(context, "点击了话题：" + tagert, Toast.LENGTH_LONG).show(),
                        Color.BLUE);
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (tagert.startsWith("http")) {
                int start = matcher.start();
                int end = matcher.end();
                AitpeopleUtil.Clickable clickableSpan=new AitpeopleUtil.Clickable(v ->
                        Toast.makeText(context, "点击了网址：" + tagert, Toast.LENGTH_LONG).show(),
                        Color.BLUE);
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv_path.setText(spannableString);
        tv_path.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void initData() {
        NotifyAdapter adapter = new NotifyAdapter(new ArrayList<>(), this);
        rec_list.setAdapter(adapter);
        // 设置数据源
        List<NotifyBean> list = NotifyUtil.initDatas();
        list.addAll(NotifyUtil.initDatas());
        adapter.setNewData(list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String path = Environment.getExternalStorageDirectory() + File.separator + 360;
                List<String> fileList = getFilesAllName(path);
                Log.e(TAG, "fileList: " + fileList.toString());
            }
        });

        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(new ItemDragHelperCallBack.OnItemDragListener() {
            @Override
            public void onItemMove(int startPos, int endPos) {
                Collections.swap(list, startPos, endPos);
                adapter.notifyItemMoved(startPos, endPos);
            }

            @Override
            public void onItemMoveStartAndEnd(RecyclerView.ViewHolder viewHolder, int status) {
                Log.e(TAG, "viewHolder: " + viewHolder);
                if (status != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setScaleX(1.1f);
                    viewHolder.itemView.setScaleY(1.1f);
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.main_color, null));
                    // 获取系统震动服务
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(150);
                    return;
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 完成移动，选中的改变样式
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
                viewHolder.itemView.setBackground(null);
            }
        });

        // 增加长按拖动功能
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBack);
        // 关联RecyclerView
        itemTouchHelper.attachToRecyclerView(rec_list);
    }

    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
                tv_path.setText(path);
                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            // 4.4以后
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                path = FilePathUtil.getPath(this, uri);
                tv_path.setText(path);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {
                // 4.4以下下系统调用方法
                path = FilePathUtil.getRealPathFromURI(uri, this);
                tv_path.setText(path);
                Toast.makeText(ListDrag.this, path + "222222", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
