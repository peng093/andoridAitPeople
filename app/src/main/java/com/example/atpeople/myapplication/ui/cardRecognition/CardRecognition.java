package com.example.atpeople.myapplication.ui.cardRecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.LoadingDialog;
import com.example.atpeople.myapplication.ui.camera.SmartsCamera;
import com.example.atpeople.myapplication.util.Base64Util;
import com.example.atpeople.myapplication.util.GsonUtil;
import com.example.atpeople.myapplication.youTuApi.YouTuApi;
import com.example.atpeople.myapplication.youTuApi.model.BusinessCardBean;
import com.example.atpeople.myapplication.youTuApi.model.BusinessCardInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Create by peng on 2019/8/1
 */
public class CardRecognition extends AppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.tv_content)
    TextView tv_content;

    private YouTuApi youTuApi;
    private LoadingDialog mLoadingDialog;
    private Handler mHandler;
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mLoadingDialog.dismiss();
                            //tv_content.setText(responseBody);
                            Log.e("wei", "识别返回: "+ responseBody);
                            BusinessCardInfo businessCardInfo=getDataInfo(responseBody);
                            tv_content.setText(GsonUtil.toJson(businessCardInfo));
                        } catch (Exception e) {

                        }

                    }
                });
            }
            @Override
            public void onFailure(final int statusCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.dismiss();
                        Log.e("wei", "错误码: "+ statusCode);
                    }
                });
            }
        });
    }

    private BusinessCardInfo getDataInfo(String responseBody) {
        BusinessCardBean businessCardBean = JSON.parseObject(responseBody, BusinessCardBean.class);
        BusinessCardInfo info = new BusinessCardInfo();
        if (businessCardBean.errorcode == 0) {
            List<BusinessCardBean.ItemsBean> items = businessCardBean.items;
            List<String>titles=new ArrayList<>();
            List<String>company=new ArrayList<>();
            List<String>addr=new ArrayList<>();
            List<String>email=new ArrayList<>();
            List<String>phones=new ArrayList<>();
            List<String>qq=new ArrayList<>();
            List<String>wechat=new ArrayList<>();
            List<String>website=new ArrayList<>();
            if (items != null && items.size() > 0) {
                for (BusinessCardBean.ItemsBean item : items) {
                    switch (item.item){
                        case "姓名":
                            info.setName(item.itemstring);
                            break;
                        case "英文姓名":
                            info.setEn_name(item.itemstring);
                            break;
                        case "职位":
                            titles.add(item.itemstring);
                            break;
                        case "英文职位":
                            titles.add(item.itemstring);
                            break;
                        case "公司":
                            company.add(item.itemstring);
                            break;
                        case "英文公司":
                            company.add(item.itemstring);
                            break;
                        case "地址":
                            addr.add(item.itemstring);
                            break;
                        case "邮箱":
                            email.add(item.itemstring);
                            break;
                        case "手机":
                            phones.add(item.itemstring);
                            break;
                        case "QQ":
                            qq.add(item.itemstring);
                            break;
                        case "微信":
                            wechat.add(item.itemstring);
                        case "网址":
                            website.add(item.itemstring);
                            break;
                    }
                }
                info.setTitle(titles);
                info.setCompany(company);
                info.setAddress(addr);
                info.setEmail(email);
                info.setPhone(phones);
                info.setQq(qq);
                info.setWechat(wechat);
                info.setWebsite(website);
            }
        }
        return info;
    }
    private void init() {
        mHandler = new Handler();
        mLoadingDialog = new LoadingDialog(this);
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
            final Bitmap bitmap=getLoacalBitmap(path);
            iv_img.setImageBitmap(bitmap);
            mLoadingDialog.setText("识别中...");
            mLoadingDialog.show();
            // tx优图识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //St
                        String base64= Base64Util.bitmapToBase64(bitmap);
                        youTuApi.nameCardOcr(base64);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
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
