package com.example.atpeople.myapplication.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.callback.BaseCallBack;
import com.example.atpeople.myapplication.callback.PermissionCallback;

import java.util.ArrayList;
import java.util.List;

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

    private PermissionCallback cb;
    private AlertDialog dialog;

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
        LayoutInflater.from(this).inflate(initLayout(), viewContent);
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
        } else {
            // 显示状态栏及背景色
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, null));
        }
    }

    public void setStatusBarFontColor(Activity activity, int fontColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            if (fontColor == Color.WHITE) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
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
        } else if (item.getItemId() == R.id.menu_1) {
            onClickListenerTopRight.onClick();
        }
        //自己处理点击事件
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuId);
        }
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(null, bitmap);
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
    protected void setTopRightButton(String menuStr, OnClickListener rightListener) {
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
    protected void setTopRightButton(String menuStr, int menuId, OnClickListener rightListener) {
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
        if (iconId != 0) {
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
        toolbar.setVisibility(toolbarShow ? View.VISIBLE : View.GONE);
    }


    /**
     * 普通弹窗 展示.
     *
     * @param title    the title
     * @param content  the content
     * @param callBack the call back
     */
    protected void showNormalAlertDialog(String title, String content, final BaseCallBack callBack) {
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
     * @param title          the title
     * @param defaultContent 默认显示内容
     * @param callBack       the call back
     */
    protected <T> void showEditAlertDialog(String title, String defaultContent, final BaseCallBack<T> callBack) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        final EditText editText = new EditText(this);
        if (!TextUtils.isEmpty(defaultContent)) {
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
                callBack.success((T) editText.getText().toString());
            }
        });
        dialog.setCancelable(true);
        AlertDialog tmp = dialog.create();
        tmp.setCanceledOnTouchOutside(true);
        tmp.show();
    }

    /**
     * Show custom view alert dialog.
     *
     * @param <T>      the type parameter
     * @param view     the view
     * @param callBack the call back
     */
    protected <T> void showCustomViewAlertDialog(View view, final BaseCallBack<T> callBack) {
        ImageView imageView = view.findViewById(R.id.tv_dialog_kefu_close);
        ImageView weixin_1 = view.findViewById(R.id.iv_feidao_kefu_weichat_copy);
        ImageView weixin_2 = view.findViewById(R.id.iv_feidao_kefu_weichat_copy_2);
        ImageView qq = view.findViewById(R.id.iv_feidao_kefu_qq_copy);
        List<ImageView> list = new ArrayList<>();
        list.add(imageView);
        list.add(weixin_1);
        list.add(weixin_2);
        list.add(qq);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        // 设置弹窗背景透明
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        for (final ImageView iv : list) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (iv.getId()) {
                        case R.id.tv_dialog_kefu_close:
                            dialog.dismiss();
                            break;
                        case R.id.iv_feidao_kefu_weichat_copy:
                        case R.id.iv_feidao_kefu_weichat_copy_2:
                        case R.id.iv_feidao_kefu_qq_copy:
                            showToast("复制成功");
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * Request perission.
     * 如需要申请华为手机 统一授权(仅限企业账号),请参考 https://developer.huawei.com/consumer/cn/doc/30701
     *
     * @param permissions the permissions
     * @param cb          the callback
     */
    protected void requestPerission(String[] permissions, PermissionCallback callback) {
        cb = callback;
        List<String> permissionList = new ArrayList<>();
        //1、 哪些权限需要申请
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //权限没有申请 添加到要申请的权限列表中
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //所有权限都同意了
            cb.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            List<String> deniedPermissions = new ArrayList<>();
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    // 如果没有同意,装进list
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permissions[i]);
                    }
                }
            }
            if (!deniedPermissions.isEmpty()) {
                cb.onDenied(deniedPermissions);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showTipsDialog();
                    }
                }
            } else {
                cb.onGranted();
            }
        } else {
            // 所有的权限都被接受了
            cb.onGranted();
        }
    }

    private void showTipsDialog() {
        // 跳转到应用设置界面
        dialog = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，允许使用权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("BaseActivity", "要开启进行权限设置");
                        // 跳转到应用设置界面
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity管理
        ActivityCollector.removeActivity(this);
    }
}
