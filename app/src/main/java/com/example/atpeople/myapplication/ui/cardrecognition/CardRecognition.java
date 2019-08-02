package com.example.atpeople.myapplication.ui.cardrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.camera.SmartsCamera;
import com.example.atpeople.myapplication.youtuapi.YouTuApi;
import com.example.atpeople.myapplication.youtuapi.model.BusinessCardBean;
import com.example.atpeople.myapplication.youtuapi.model.BusinessCardInfo;
import com.hanvon.HWCloudManager;
import com.tbruyelle.rxpermissions2.RxPermissions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Create by peng on 2019/8/1
 */
public class CardRecognition extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.tv_content)
    TextView tv_content;


    private HWCloudManager hwCloudManagerBcard;
    private YouTuApi youTuApi;
    public static final String key1="12531cc8-0168-444b-99e8-accdb3ca7fb2";
    Boolean iskeyOver=false;
    public static final String key2="d3b35298-74f8-45b7-a33c-4eb3cbd9ea2b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);
        ButterKnife.bind(this);
        init();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(CardRecognition.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ){
                requestReadAndWritePermission();
            } else {
                hwCloudManagerBcard = new HWCloudManager(this,iskeyOver?key1:key2);
                youTuApi =new YouTuApi("appid","secret_id","secret_key");
                youTuApi.setRequestListener(new YouTuApi.OnRequestListener() {
                    @Override
                    public void onSuccess(int statusCode, final String responseBody) {
                        // 切换回主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_content.setText(responseBody);
                                Log.e("wei", "识别返回: "+ responseBody);
                                BusinessCardInfo businessCardInfo=getDataInfo(responseBody);
                            }
                        });

                    }

                    @Override
                    public void onFailure(int statusCode) {
                        Log.e("wei", "错误码: "+ statusCode);
                    }
                });
            }
        }
    }

    private BusinessCardInfo getDataInfo(String responseBody) {
        BusinessCardBean businessCardBean = JSON.parseObject(responseBody, BusinessCardBean.class);
        BusinessCardInfo info = new BusinessCardInfo();
        if (businessCardBean.errorcode == 0) {
            List<BusinessCardBean.ItemsBean> items = businessCardBean.items;
            if (items != null && items.size() > 0) {
                for (BusinessCardBean.ItemsBean item : items) {
                    switch (item.item){
                        case "姓名":
                            info.setName(item.itemstring);
                            break;
                        case "职位":
                            info.setTitle(item.itemstring);
                            break;
                        case "公司":
                            info.setCompany(item.itemstring);
                            break;
                        case "地址":
                            info.setAddress(item.itemstring);
                            break;
                        case "邮箱":
                            info.setEmail(item.itemstring);
                            break;
                        case "手机":
                            info.setPhone(item.itemstring);
                            break;
                        case "QQ":
                            info.setQq(item.itemstring);
                            break;
                        case "微信":
                            info.setWechat(item.itemstring);
                            break;
                    }
                }

            }
        }
        return info;
    }
    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SmartsCamera.class);
                startActivityForResult(intent,10);
            }
        });
    }

    private void requestReadAndWritePermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.BLUETOOTH)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(!aBoolean){
                            Toast.makeText(CardRecognition.this, "请开启权限！", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                        hwCloudManagerBcard = new HWCloudManager(CardRecognition.this,iskeyOver?key1:key2);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && data != null){
            String path = data.getStringExtra("path");
            Bitmap bitmap=getLoacalBitmap(path);
            iv_img.setImageBitmap(bitmap);
            card(path);
            // tx优图识别
//            String base64=Base64Convert.StringToBase64(path,2);
//            try {
//                youTuApi.nameCardOcr(base64);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
    /**
     * @Author Peng
     * @Date 2019/8/1 15:06
     * @Describe 开启一线程，来进行图片识别
     */
    String result="";
    private void card(final String picPath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result= hwCloudManagerBcard.cardLanguage("chns", picPath);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                // 切换回主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result==null){
                            return;
                        }
                        try {
                            JSONObject obj = new JSONObject(result);
                            int code = obj.getInt("code");
                            if(code==434){
                                // 识别次数
                                iskeyOver=true;
                                return;
                            }else if(code!=0){
                                return;
                            }
                            tv_content.setText(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    /**
     * 加载本地图片
     * @param url
     * @return Bitmap
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
