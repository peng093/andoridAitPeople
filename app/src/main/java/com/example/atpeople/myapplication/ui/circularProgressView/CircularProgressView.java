package com.example.atpeople.myapplication.ui.circularProgressView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.CircularProgress;
import com.example.atpeople.myapplication.customview.CompletedView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CircularProgressView extends AppCompatActivity {

    int id;

    private int mTotalProgress = 100;
    private int mCurrentProgress = 0;
    private int index = 0;
    //进度条
    private CompletedView mTasksView;
    private TextView tv_progress;
    TextView tv_name;
    CircularProgress progress_bar;
    /** 定时尽量不用线程，如果是比较耗时的线程没有执行完毕，退出activity会有内存泄漏问题*/
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secon_view);
        tv_name = findViewById(R.id.tv_name);
        tv_progress = findViewById(R.id.tv_progress);
        id = getIntent().getIntExtra("id", 0);
        tv_name.setText("id:" + id);

        mTasksView = findViewById(R.id.tasks_view);
        tv_name.setText("morris");
        tv_progress.setText("4/30");

        initView();
        disposable = Observable.interval(0, 10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e("CircularProgressView", "-----RxJava 定时轮询次数----" + aLong);
                        if (mCurrentProgress < mTotalProgress) {
                            mCurrentProgress += 1;
                            // 画进度条
                            mTasksView.setProgress(mCurrentProgress);
                        }

                        if (index>=100){
                            index = 0;
                        }else {
                            ++index;
                        }
                        progress_bar.setProgress(index);
                        progress_bar.setText("测试\n"+progress_bar.getProgress() + "%");
                    }
                });
    }

    private void initView() {
        progress_bar = (CircularProgress) findViewById(R.id.progress_bar);
        progress_bar.setProgress(0);
        progress_bar.setText("测试\n" + progress_bar.getProgress() + "%");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
