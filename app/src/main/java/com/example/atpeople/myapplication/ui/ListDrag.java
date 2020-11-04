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
import android.widget.Button;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.bt_button)
    Button bt_button;


    String path = "";
    List<FileBean> fileList;

    // 定义正则表达式
    /** @人*/
    private static final String AT = "@[\u4e00-\u9fa5\\w]+";
    /** ##话题*/
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";
    // 表情
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";
    // url
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    protected int initLayout() {
        return R.layout.activity_drag_list;
    }

    @Override
    protected void initView() {
        rec_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String text = "@张三 hahhah@李四#你好##合帮# 呵呵 http://www.cocos.com/docs ";
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
        FilesListAdapter adapter = new FilesListAdapter(new ArrayList<>());
        rec_list.setAdapter(adapter);
        // 设置数据源
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "fileList: " + fileList.get(position).getAbsolutePath());
            }
        });
        bt_button.setOnClickListener(v -> {
            String path = Environment.getExternalStorageDirectory() + File.separator + 360;
            fileList= getFilesAllName(path);
            adapter.setNewData(fileList);
        });
    }

    public static List<FileBean> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<FileBean> fileBeanList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            FileBean fileBean=new FileBean();
            fileBean.setName(files[i].getName());
            fileBean.setAbsolutePath(files[i].getAbsolutePath());
            File file1=new File(files[i].getAbsolutePath());
            fileBean.setLastModifiedTime(getFileLastModifiedTime(file1));
            fileBean.setSize(FormetFileSize(getFileSize(file1)));
            fileBeanList.add(fileBean);
        }
        return fileBeanList;
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

    private static final String mformatType = "yyyy/MM/dd HH:mm:ss";

    /**
     * 获取文件最后改动时间
     * @param file
     * @return
     */
    public static String getFileLastModifiedTime(File file) {
        Calendar cal = Calendar.getInstance();
        long time = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(mformatType);
        cal.setTimeInMillis(time);

        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }
    /**
     　　* 获取指定文件大小
     　　* @param f
     　　* @return
     　　* @throws Exception
     */
    public static long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }
    /**
     　　* 转换文件大小
     　　* @param fileS
     　　* @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
