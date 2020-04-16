package com.example.atpeople.myapplication.ui.webView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.baseokhttp.util.Parameter;

/**
 * Create by peng on 2019/8/2
 */
public class MyWebView extends AppCompatActivity {
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
//        //支持js
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 跨域
        settings.setAllowFileAccessFromFileURLs(true);
        // 允许浏览器调试
        WebView.setWebContentsDebuggingEnabled(true);
//        mContentWv.loadUrl("https://www.baidu.com/");
        mContentWv.loadUrl("file:///android_asset/ochart/index.html");

        mContentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 加载完成后,发送请求
                // sendPost();
                triggerEvnet("androidMsg","hahha6666");
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
    private void sendPost(){
        // context 如果不是 Activity，本次请求在异步线程返回
        HttpRequest.POST(this, "/company/getOchartList", new Parameter().add("id", "4200"), new JsonResponseListener() {
            @Override
            public void onResponse(JsonMap main, Exception error) {
                if (error == null) {
                    String data=main.getString("data");
                    // 触发自定义事件,并传入参数,参数格式如下
                    // { origin: 'ochart-iframe', method: method, data: data }
                    final String param="{origin: 'ochart-iframe',method: 'ochart_init',data:"+data+"}";
                    triggerEvnet("message",param);
                } else {
                    Toast.makeText(getBaseContext(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void triggerEvnet(String eventName, String detail){
        if(eventName==null || eventName.length()==0){
            return;
        }
        if(detail==null || detail.length()==0){
            detail = "{}";
        }
        mContentWv.loadUrl("javascript:showInfoFromJava('" + eventName + "')");
    }

}
