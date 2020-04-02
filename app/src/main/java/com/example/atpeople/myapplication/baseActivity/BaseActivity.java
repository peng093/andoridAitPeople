package com.example.atpeople.myapplication.baseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.callback.BaseCallBack;

import butterknife.ButterKnife;

/**
 * Create by peng on 2019/10/12
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    private boolean isAllowScreenRoate = true;
    private static Toast toast;
    public Context context;

    private Toolbar toolbar;
    private FrameLayout viewContent;
    private TextView mTitle;

    private int menuId;
    private Bitmap bitmap;
    private String menuStr;
    //左边图标的点击事件
    private OnClickListener onClickListenerTopRight;
    //定义接口
    public interface OnClickListener {
        void onClick();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar_base);
        //设置布局
        toolbar = findViewById(R.id.toolbar);
        viewContent = findViewById(R.id.view_content);
        mTitle = findViewById(R.id.tv_title);

        setSupportActionBar(toolbar);
        // 使用自带返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;
        //activity管理
        ActivityCollector.addActivity(this);
        // 默认显示状态栏
        setShowStatusBar(true);
        LayoutInflater.from(this).inflate(initLayout(),viewContent);
        ButterKnife.bind(this);
        //设置屏幕是否可旋转
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //初始化控件
        initView();
        //设置数据
        initData();
    }

    /**
     * 初始化布局
     *
     * @return 布局id
     */
    protected abstract int initLayout();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置数据
     */
    protected abstract void initData();

    /**
     * 设置是否显示状态栏
     *
     * @param showStatusBar true or false
     */
    public void setShowStatusBar(boolean showStatusBar) {
        if (!showStatusBar) {
            // 隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            // 显示状态栏及背景色
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color,null));
        }
    }
    public  void setStatusBarFontColor(Activity activity, int fontColor){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            if(fontColor==Color.WHITE){
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }else {
                // 灰色字体
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
    /**
     * 是否允许屏幕旋转
     *
     * @param allowScreenRoate true or false
     */
    public void setAllowScreenRoate(boolean allowScreenRoate) {
        isAllowScreenRoate = allowScreenRoate;
    }

    /**
     * 保证同一按钮在1秒内只会响应一次点击事件
     */
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮之间的间隔，目前为1000ms
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View view) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(view);
            }
        }
    }

    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }else if(item.getItemId() == R.id.menu_1) {
            onClickListenerTopRight.onClick();
        }
        //自己处理点击事件
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar,menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuId);
        }
        if(bitmap!=null){
            Drawable drawable = new BitmapDrawable(null,bitmap);
            menu.findItem(R.id.menu_1).setIcon(drawable);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_1).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 设置标题 右侧标题及点击事件
     *
     * @param menuStr       the menu str
     * @param rightListener the right listener
     */
    protected void setTopRightButton(String menuStr,OnClickListener rightListener) {
        this.onClickListenerTopRight = rightListener;
        this.menuStr = menuStr;
    }

    /**
     * 设置标题 右侧图标及点击事件
     *
     * @param menuStr       the menu str
     * @param menuId        the menu id
     * @param rightListener the right listener
     */
    protected void setTopRightButton(String menuStr,int menuId, OnClickListener rightListener) {
        this.menuStr = menuStr;
        this.menuId = menuId;
        this.onClickListenerTopRight = rightListener;
    }

    /**
     * 设置标题 右侧图标及点击事件
     *
     * @param menuStr       the menu str
     * @param bitmap        the bitmap
     * @param rightListener the right listener
     */
    protected void setTopRightButton2(String menuStr, Bitmap bitmap, OnClickListener rightListener) {
        this.menuStr = menuStr;
        this.bitmap = bitmap;
        this.onClickListenerTopRight = rightListener;
    }

    /**
     * 设置页面返回按钮的图标
     *
     * @param iconId the icon id
     */
    protected void setTopLeftButton(int iconId) {
        if(iconId!=0){
            toolbar.setNavigationIcon(iconId);
        }
    }

    /**
     * 设置当前页面标题
     *
     * @param title the title
     */
    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    /**
     * 隐藏通用的标题，用于特殊页面，自定义标题 默认显示
     *
     * @param toolbarShow the toolbar show
     */
    protected void setToolbarShow(boolean toolbarShow) {
        toolbar.setVisibility(toolbarShow?View.VISIBLE:View.GONE);
    }


    /**
     * 普通弹窗 展示.
     *
     * @param title    the title
     * @param content  the content
     * @param callBack the call back
     */
    protected void showNormalAlertDialog(String title, String content, final BaseCallBack callBack){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(content);

        dialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.failed(null);
            }
        });
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.success(null);
            }
        });
        dialog.setCancelable(true);
        AlertDialog tmp = dialog.create();
        tmp.setCanceledOnTouchOutside(true);
        tmp.show();
    }

    /**
     * Show edit alert dialog.
     *
     * @param title       the title
     * @param defaultContent 默认显示内容
     * @param callBack    the call back
     */
    protected void showEditAlertDialog(String title, String defaultContent, final BaseCallBack callBack){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        final EditText editText=new EditText(this);
        if(!TextUtils.isEmpty(defaultContent)){
            editText.setText(defaultContent);
            editText.setSelection(defaultContent.length());
        }
        dialog.setView(editText);
        dialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.failed(null);
            }
        });
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.success(editText.getText().toString());
            }
        });
        dialog.setCancelable(true);
        AlertDialog tmp = dialog.create();
        tmp.setCanceledOnTouchOutside(true);
        tmp.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity管理
        ActivityCollector.removeActivity(this);
    }
}
