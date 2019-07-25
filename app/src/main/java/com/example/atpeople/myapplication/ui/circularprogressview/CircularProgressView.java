package com.example.atpeople.myapplication.ui.circularprogressview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.CompletedView;

public class CircularProgressView extends AppCompatActivity {

    int id;

    private int mTotalProgress = 0;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;
    private TextView tv_progress;
    TextView tv_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secon_view);
        tv_name=findViewById(R.id.tv_name);
        tv_progress=findViewById(R.id.tv_progress);
        id = getIntent().getIntExtra("id", 0);
        tv_name.setText("id:"+id);

        mTasksView =findViewById(R.id.tasks_view);
        tv_name.setText("morris");
        // 定制总进度
        // double num=4/(double)30;
        // mTotalProgress=(int)(num*100);
        mTotalProgress=100;
        tv_progress.setText("4/30");
        new Thread(new ProgressRunable()).start();
    }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                // 画进度条
                mTasksView.setProgress(mCurrentProgress);
                try {
                    // 可以控制进度条完成速度
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
