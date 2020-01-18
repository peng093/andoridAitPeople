package com.example.atpeople.myapplication.networkRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.OnDownloadListener;
import com.kongzue.baseokhttp.util.BaseOkHttp;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.baseokhttp.util.Parameter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by peng on 2019/8/21
 */
public class NormalRequest extends AppCompatActivity {

    @BindView(R.id.btn_http)
    Button btn_http;
    @BindView(R.id.btn_download)
    Button btn_download;

    @BindView(R.id.result_http)
    TextView resultHttp;
    @BindView(R.id.psg_download)
    ProgressBar psgDownload;



    private ProgressDialog progressDialog;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
        context=this;
        BaseOkHttp.DEBUGMODE = true;
        BaseOkHttp.serviceUrl = "https://www.apiopen.top";
        BaseOkHttp.overallHeader = new Parameter()
                .add("Charset", "UTF-8")
                .add("Content-Type", "application/json")
                .add("Accept-Encoding", "gzip,deflate");
        init();
    }

    private void init() {
        btn_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(context, "请稍候", "请求中...");
                HttpRequest.POST(context, "/femaleNameApi", new Parameter().add("page", "1"), new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        progressDialog.dismiss();
                        if (error == null) {
                            resultHttp.setText(main.toString());
                        } else {
                            resultHttp.setText("请求失败");
                            Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest.DOWNLOAD(
                        NormalRequest.this,
                        "http://cdn.to-future.net/apk/tofuture.apk",
                        new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "BaseOkHttpV3"), "to-future.apk"),
                        new OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                Toast.makeText(context, "文件已下载完成：" + file.getAbsolutePath(), Toast.LENGTH_LONG);
                            }

                            @Override
                            public void onDownloading(int progress) {
                                psgDownload.setProgress(progress);
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT);
                            }
                        }
                );
            }
        });
    }
}
