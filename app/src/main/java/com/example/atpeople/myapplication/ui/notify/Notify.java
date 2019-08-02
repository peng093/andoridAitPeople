package com.example.atpeople.myapplication.ui.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.notify.model.NotifyBean;
import com.example.atpeople.myapplication.util.NotifyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/8/2
 */
public class Notify extends AppCompatActivity {

    @BindView(R.id.listview)
    RecyclerView  listview;

    private NotifyUtil currentNotify;
    private List<NotifyBean> mDataList;

    Context mContext;
    private int requestCode = (int) SystemClock.uptimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        ButterKnife.bind(this);
        mContext = this;
        initListView();
    }



    private void initListView() {
        listview.setLayoutManager(new LinearLayoutManager(this));
        NotifyAdapter adapter = new NotifyAdapter( new ArrayList<NotifyBean>(),this);
        listview.setAdapter(adapter);
        // 设置数据源
        adapter.setNewData(NotifyUtil.initDatas());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        notify_normal_singLine();
                        break;
                    case 1:
                        notify_normal_moreLine();

                        break;
                    case 2:
                        notify_mailbox();

                        break;
                    case 3:
                        notify_bigPic();

                        break;
                    case 4:
                        notify_customview();

                        break;
                    case 5:
                        notify_buttom();

                        break;
                    case 6:
                        notify_progress();

                        break;
                    case 7:
                        notify_headUp();
                        break;
                    case 8:
                        if (currentNotify != null) {
                            currentNotify.clear();
                        }
                        break;
                }
            }
        });
    }




    /**
     * 高仿淘宝
     */
    private void notify_normal_singLine() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.tb_bigicon;
        String ticker = "您有一条新通知";
        String title = "双十一大优惠！！！";
        String content = "仿真皮肤充气娃娃，女朋友带回家！";

        //实例化工具类，并且调用接口
        NotifyUtil notify1 = new NotifyUtil(mContext, 1);
        notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);
        currentNotify = notify1;
    }

    /**
     * 高仿网易新闻
     */
    private void notify_normal_moreLine() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.netease_bigicon;
        String ticker = "您有一条新通知";
        String title = "朱立伦请辞国民党主席 副主席黄敏惠暂代党主席";
        String content = "据台湾“中央社”报道，国民党主席朱立伦今天(18日)向中常会报告，为败选请辞党主席一职，他感谢各位中常委的指教包容，也宣布未来党务工作由副主席黄敏惠暂代，完成未来所有补选工作。";
        //实例化工具类，并且调用接口
        NotifyUtil notify2 = new NotifyUtil(mContext, 2);
        notify2.notify_normal_multiline(pIntent, smallIcon, ticker, title, content, true, true, false);
        currentNotify = notify2;
    }

    /**
     * 收件箱样式
     */
    private void notify_mailbox() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int largeIcon = R.drawable.fbb_largeicon;
        int smallIcon = R.drawable.wx_smallicon;
        String ticker = "您有一条新通知";
        String title = "冰冰";
        ArrayList<String> messageList = new ArrayList<String>();
        messageList.add("文明,今晚有空吗？");
        messageList.add("晚上跟我一起去玩吧?");
        messageList.add("怎么不回复我？？我生气了！！");
        messageList.add("我真生气了！！！！！你听见了吗!");
        messageList.add("文明，别不理我！！！");
        String content = "[" + messageList.size() + "条]" + title + ": " + messageList.get(0);
        //实例化工具类，并且调用接口
        NotifyUtil notify3 = new NotifyUtil(mContext, 3);
        notify3.notify_mailbox(pIntent, smallIcon, largeIcon, messageList, ticker,
                title, content, true, true, false);
        currentNotify = notify3;
    }

    /**
     * 高仿系统截图通知
     */
    private void notify_bigPic() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.xc_smaillicon;
        int largePic = R.drawable.screenshot;
        String ticker = "您有一条新通知";
        String title = "已经抓取屏幕截图";
        String content = "触摸可查看您的屏幕截图";
        //实例化工具类，并且调用接口
        NotifyUtil notify4 = new NotifyUtil(mContext, 4);
        notify4.notify_bigPic(pIntent, smallIcon, ticker, title, content, largePic, true, true, false);
        currentNotify = notify4;
    }

    /**
     * 此方法可作为简单测试使用
     */
    private void notifyTest() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager == null){return;}

        Notification notification;

        if (Build.VERSION.SDK_INT >= 26) {
            //当sdk版本大于26
            Notification.BigPictureStyle picStyle = new Notification.BigPictureStyle();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.screenshot, options);
            picStyle.bigPicture(bitmap);
            picStyle.bigLargeIcon(bitmap);

            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);

            //设置字体颜色span
            SpannableStringBuilder ssb = new SpannableStringBuilder("This is a content title");
            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            notification = new Notification.Builder(this, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(ssb)
                    .setContentText(ssb)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setStyle(picStyle)
                    .build();
        } else {
            //当sdk版本小于26
            NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.screenshot, options);
            picStyle.bigPicture(bitmap);
            picStyle.bigLargeIcon(bitmap);

            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setContentIntent(pendingIntent)
                    .setStyle(picStyle)
                    .build();
        }
        manager.notify(1, notification);
    }


    /**
     * 高仿应用宝
     */
    private void notify_customview() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";

        //设置自定义布局中按钮的跳转界面
        Intent btnIntent = new Intent(mContext, OtherActivity.class);
        btnIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //如果是启动activity，那么就用PendingIntent.getActivity，如果是启动服务，那么是getService
        PendingIntent Pintent = PendingIntent.getActivity(mContext,
                (int) SystemClock.uptimeMillis(), btnIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 自定义布局
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.yyb_notification);
        remoteViews.setImageViewResource(R.id.image, R.drawable.yybao_bigicon);
        remoteViews.setTextViewText(R.id.title, "垃圾安装包太多");
        remoteViews.setTextViewText(R.id.text, "3个无用安装包，清理释放的空间");
        remoteViews.setOnClickPendingIntent(R.id.button, Pintent);
        int smallIcon = R.drawable.yybao_smaillicon;
        //实例化工具类，并且调用接口
        NotifyUtil notify5 = new NotifyUtil(mContext, 5);
        notify5.notify_customview(remoteViews, pIntent, smallIcon, ticker, true, true, false);
        currentNotify = notify5;
    }

    /**
     * 高仿Android更新提醒样式
     */
    private void notify_buttom() {
        //设置想要展示的数据内容
        String ticker = "您有一条新通知";
        int smallIcon = R.drawable.android_bigicon;
        int lefticon = R.drawable.android_leftbutton;
        String lefttext = "以后再说";
        Intent leftIntent = new Intent();
        leftIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent leftPendIntent = PendingIntent.getActivity(mContext,
                requestCode, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int righticon = R.drawable.android_rightbutton;
        String righttext = "安装";
        Intent rightIntent = new Intent(mContext, OtherActivity.class);
        rightIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(mContext,
                requestCode, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //实例化工具类，并且调用接口
        NotifyUtil notify6 = new NotifyUtil(mContext, 6);
        notify6.notify_button(smallIcon, lefticon, lefttext, leftPendIntent, righticon, righttext, rightPendIntent, ticker, "系统更新已下载完毕", "Android 6.0.1", true, true, false);
        currentNotify = notify6;
    }


    /**
     * 高仿Android系统下载样式
     */
    private void notify_progress() {
        //设置想要展示的数据内容
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.android_bigicon;
        String ticker = "您有一条新通知";
        //实例化工具类，并且调用接口
        NotifyUtil notify7 = new NotifyUtil(mContext, 7);
        notify7.notify_progress(rightPendIntent, smallIcon, ticker, "Android 6.0.1 下载", "正在下载中", true, false, false);
        currentNotify = notify7;
    }

    /**
     * Android 5。0 新特性：悬浮式通知
     */
    private void notify_headUp() {
        //设置想要展示的数据内容
        int smallIcon = R.drawable.hl_smallicon;
        int largeIcon = R.drawable.fbb_largeicon;
        String ticker = "您有一条新通知";
        String title = "范冰冰";
        String content = "文明，今晚在希尔顿酒店2016号房哈";
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        int lefticon = R.drawable.hl_message;
        String lefttext = "回复";
        Intent leftIntent = new Intent();
        leftIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent leftPendingIntent = PendingIntent.getActivity(mContext,
                requestCode, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int righticon = R.drawable.hl_call;
        String righttext = "拨打";
        Intent rightIntent = new Intent(mContext, OtherActivity.class);
        rightIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendingIntent = PendingIntent.getActivity(mContext,
                requestCode, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //实例化工具类，并且调用接口
        NotifyUtil notify8 = new NotifyUtil(mContext, 8);
        notify8.notify_HeadUp(pendingIntent, smallIcon, largeIcon, ticker, title, content, lefticon, lefttext, leftPendingIntent, righticon, righttext, rightPendingIntent, true, true, false);
        currentNotify = notify8;
    }


}
