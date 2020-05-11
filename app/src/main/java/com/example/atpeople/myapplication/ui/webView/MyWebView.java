package com.example.atpeople.myapplication.ui.webView;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.util.GsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.kongzue.baseokhttp.util.Parameter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        WebSettings settings = mContentWv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置后才可以发送 postMessage事件通知
        settings.setSupportMultipleWindows(true);
        //支持js
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 本地文件跨域
        settings.setAllowFileAccessFromFileURLs(true);
        // 允许浏览器调试
        WebView.setWebContentsDebuggingEnabled(true);

        // 图片跨域问题修复
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            settings.setAllowUniversalAccessFromFileURLs(true);
        }else{
            try {
                Class<?> clazz = settings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(settings, true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        mContentWv.loadUrl("https://www.baidu.com/");
        mContentWv.loadUrl("file:///android_asset/newOchart/index.html");
        mContentWv.addJavascriptInterface(new WebAppInterface(), "Android");
        mContentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

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
        HttpRequest.POST(this, "/company/getOchartList", new Parameter().add("id", "4200"), new ResponseListener() {
            @Override
            public void onResponse(String response, Exception error) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(response).getAsJsonObject();
                String json=jsonObject.get("data").toString();
                String base64Sign = null;
                try {
                    base64Sign = Base64.encodeToString(json.getBytes("utf-8"),Base64.NO_WRAP);
                    mContentWv.evaluateJavascript("showInfoFromJava('"+base64Sign+"')",null);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
//        String token="b9b7ca83c4bf652484715e61ac38bdff";
//        int id=4200;
//        mContentWv.loadUrl("javascript:showInfoFromJava('" + id + "','" + token + "')");
    }

    public class WebAppInterface{
        @JavascriptInterface
        public void requestData() {
            Logger.d("被js调用");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     sendPost();
                }
            });

        }
    }
}
