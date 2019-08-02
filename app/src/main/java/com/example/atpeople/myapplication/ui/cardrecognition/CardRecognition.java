package com.example.atpeople.myapplication.ui.cardrecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.camera.SmartsCamera;
import com.example.atpeople.myapplication.util.Base64Convert;
import com.example.atpeople.myapplication.youtuapi.YouTuApi;
import com.example.atpeople.myapplication.youtuapi.model.BusinessCardBean;
import com.example.atpeople.myapplication.youtuapi.model.BusinessCardInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



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

    private YouTuApi youTuApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);
        ButterKnife.bind(this);
        init();
        youTuApi =new YouTuApi("10192317","AKIDLWP6VdMiLhqb5xe8IDsjKgrmyvd5Ei4E","5uSihmegU3XPRFm8HF4dvZ8UfdLmxub4");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && data != null){
            final String path = data.getStringExtra("path");
            Bitmap bitmap=getLoacalBitmap(path);
            iv_img.setImageBitmap(bitmap);
            // tx优图识别
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    try {
                        String base64=Base64Convert.StringToBase64(path,2);
                        youTuApi.nameCardOcr(base64);
                    } catch (Exception e) {

                    }

//                }
//            }).start();
        }
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
