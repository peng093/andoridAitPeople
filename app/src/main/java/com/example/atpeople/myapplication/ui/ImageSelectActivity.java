package com.example.atpeople.myapplication.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.callback.BaseCallBack;
import com.example.atpeople.myapplication.util.BaseFileUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Create by peng on 2020/4/21
 */
public class ImageSelectActivity extends BaseActivity {
    @BindView(R.id.bt_open)
    Button bt_open;
    @BindView(R.id.sv_1)
    SimpleDraweeView sv_1;
    @BindView(R.id.sv_2)
    SimpleDraweeView sv_2;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;

    @Override
    protected int initLayout() {
        return R.layout.activity_image_select;
    }

    @Override
    protected void initView() {
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传入回调接口 选择图片后,全部完成压缩再进行回调
                openMulti(new BaseCallBack<List<String>>() {
                    @Override
                    public void success(List<String> list) {
                        sv_2.setImageURI("file://"+list.get(0));
                        String realm_size= BaseFileUtil.getAutoFileOrFilesSize(list.get(0));
                        tv_2.setText("lubanCompress=="+realm_size);
                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }
    private void openMulti(BaseCallBack callback) {
        RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                .with(ImageSelectActivity.this)
                .image()
                .multiple();
        rxGalleryFinal.maxSize(8)
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        List<MediaBean> list = imageMultipleResultEvent.getResult();
                        sv_1.setImageURI("file://"+list.get(0).getOriginalPath());
                        String realm_size= BaseFileUtil.getAutoFileOrFilesSize(list.get(0).getOriginalPath());
                        tv_1.setText("原图大小=="+realm_size);
                        // 鲁班压缩
                        List<String> paths= new ArrayList();
                        for (MediaBean mediaBean : list) {
                            paths.add(mediaBean.getOriginalPath());
                        }
                        lubanCompress(paths,callback);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
                    }
                })
                .openGallery();
    }

    /**
     * 鲁班压缩
     * @param paths
     * @param callBack
     * @param <T>
     */
    private <T> void lubanCompress(List<String> paths, BaseCallBack<T> callBack){
        List<String>newPath=new ArrayList<>();
        Luban.with(ImageSelectActivity.this)
                .load(paths)
                .ignoreBy(100)
//                        .setTargetDir(getPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {}

                    @Override
                    public void onSuccess(File file) {
                        newPath.add(file.getAbsolutePath());
                        if(newPath.size()==paths.size()){
                            callBack.success((T) newPath);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.failed(e);
                    }
                }).launch();
    }
}
