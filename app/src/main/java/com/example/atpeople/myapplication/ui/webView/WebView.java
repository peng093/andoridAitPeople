package com.example.atpeople.myapplication.ui.webView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.atpeople.myapplication.R;

/**
 * Create by peng on 2019/8/2
 */
public class WebView extends AppCompatActivity {
    private android.webkit.WebView mContentWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        mContentWv = findViewById(R.id.wv_webview_content);
        initWebView();
    }

    private void initWebView() {

        mContentWv.getSettings().setJavaScriptEnabled(true);

        WebSettings settings = mContentWv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持js
        settings.setJavaScriptEnabled(true);
        // 采用电脑浏览器的模式访问
        settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //自动缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        //支持获取手势焦点
        mContentWv.requestFocusFromTouch();

        mContentWv.loadUrl("https://www.baidu.com/");
        //mContentWv.loadUrl("file:///android_asset/localH5/textAnimation/index.html");

        mContentWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mContentWv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 表示按返回键
                    if (keyCode == KeyEvent.KEYCODE_BACK && mContentWv.canGoBack()) {
                        mContentWv.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

    }

}
