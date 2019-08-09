package com.example.atpeople.myapplication.ui.launchwithvideo;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.CustomVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/8/9
 */
public class LaunchWithVideo extends AppCompatActivity {
    @BindView(R.id.welcome_videoview)
    CustomVideoView welcome_videoview;
    @BindView(R.id.welcome_button)
    Button welcome_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_video);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            // 状态栏白色字体
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        welcome_videoview.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
        welcome_videoview.start();
        welcome_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                welcome_videoview.start();

            }
        });
        welcome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(welcome_videoview.isPlaying()){
                    welcome_videoview.stopPlayback();
                    welcome_videoview=null;
                }
                finish();
            }
        });
    }
}
