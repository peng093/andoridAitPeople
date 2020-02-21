package com.example.atpeople.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Create by peng on 2019/12/31
 */
public class PictureUtil {
    /**
     * @Author Peng
     * @Date 2019/12/31 11:08
     * @Describe Fresco下载图片
     */
    public static void downPic(final ImageView imageView, Context mContext) {
        Uri uri = Uri.parse("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg");
        ImageRequest imageRequest0=ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        // 预加载到硬盘
        // imagePipeline.prefetchToDiskCache(imageRequest, mContext);
        // 预加载到内存
        // imagePipeline.prefetchToBitmapCache(imageRequest, callerContext);
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest0, mContext);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                // 获取图片的bitmap
                Log.d("down", "down在bitmap做了点啥" + (bitmap == null));
                imageView.setImageBitmap(bitmap);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        }, UiThreadImmediateExecutorService.getInstance());
    }
}
