package com.example.atpeople.myapplication.ui.mediaPlayBySeekbar;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;

import butterknife.BindView;

/**
 * Create by peng on 2019/11/4
 */
public class MediaPlayBySeekbar extends BaseActivity {
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.pause)
    Button pause;
    @BindView(R.id.sb)
    SeekBar sb;

    MediaPlayer mp;
    Handler handler = new Handler();
    int Duration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.media_play;
    }

    @Override
    protected void initView() {
        setTitle("测试滑动音频文件");
        // 回退按钮监听
        setTopLeftButton(0);
        mp = MediaPlayer.create(this, Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.twins));
        //后面的参数必须是URI形式的，所以要把相应路径转换成URI
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(start);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 监听拖动完成后，跳到指定播放位置
                mp.seekTo(sb.getProgress());
            }
        });
        //监听器
        Duration = mp.getDuration();
        //音乐文件持续时间
        sb.setMax(Duration);
        //设置SeekBar最大值为音乐文件持续时间
    }

    @Override
    protected void initData() {

    }

    Runnable start = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            mp.start();
            //用一个handler更新SeekBar
            handler.post(updatesb);
        }
    };
    Runnable updatesb = new Runnable() {
        @Override
        public void run() {
            // 每一秒，获取当前播放位置，同步倒控件上
            sb.setProgress(mp.getCurrentPosition());
            handler.postDelayed(updatesb, 1000);
        }
    };
}
