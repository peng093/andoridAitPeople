package com.example.atpeople.myapplication.ui.camera;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.util.FileUtil;
import com.google.android.cameraview.CameraImpl;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.pqpo.smartcameralib.MaskView;
import me.pqpo.smartcameralib.SmartCameraView;
import me.pqpo.smartcameralib.SmartScanner;


/**
 * Create by peng on 2019/7/30
 */
public class SmartsCamera extends AppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.camera_view)
    SmartCameraView mCameraView;
    @BindView(R.id.image)
    ImageView ivPreview;

    private static final String TAG = "SmartsCamera";
    private AlertDialog alertDialog;
    private boolean granted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smarts_camera);
        ButterKnife.bind(this);

        initPermissions();
        initMaskView();
        initScannerParams();
        initCameraView();
    }
    private void initPermissions() {
        // 实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            // 状态栏白色字体
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraView.takePicture();
                mCameraView.stopScan();
            }
        });

        new RxPermissions(this).request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        SmartsCamera.this.granted = granted;
                        if (granted) {
                            MaskView maskView = (MaskView) mCameraView.getMaskView();
                            maskView.setShowScanLine(true);
                            mCameraView.start();
                            mCameraView.startScan();
                        } else {
                            Toast.makeText(SmartsCamera.this, "请开启相机权限！", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initScannerParams() {
        SmartScanner.DEBUG = true;
        /**
          canny 算符阈值
          1. 低于阈值1的像素点会被认为不是边缘；
          2. 高于阈值2的像素点会被认为是边缘；
          3. 在阈值1和阈值2之间的像素点,若与第2步得到的边缘像素点相邻，则被认为是边缘，否则被认为不是边缘。
         */
        // canny 算符阈值1
        SmartScanner.cannyThreshold1 = 20;
        // canny 算符阈值2
        SmartScanner.cannyThreshold2 = 50;
        /**
         * 霍夫变换检测线段参数
         * 1. threshold: 最小投票数，要检测一条直线所需最少的的曲线交点，增大该值会减少检测出的线段数量。
         * 2. minLinLength: 能组成一条直线的最少点的数量, 点数量不足的直线将被抛弃。
         * 3. maxLineGap: 能被认为在一条直线上的点的最大距离，若出现较多断断续续的线段可以适当增大该值。
         */
        SmartScanner.houghLinesThreshold = 130;
        SmartScanner.houghLinesMinLineLength = 80;
        SmartScanner.houghLinesMaxLineGap = 10;
        /**
         * 高斯模糊半径，用于消除噪点，必须为正奇数。
         */
        SmartScanner.gaussianBlurRadius = 3;

        // 检测范围比例, 比例越小表示待检测物体要更靠近边框
        SmartScanner.detectionRatio = 0.1f;
        // 线段最小长度检测比例
        SmartScanner.checkMinLengthRatio = 0.8f;
        // 为了提高性能，检测的图片会缩小到该尺寸之内
        SmartScanner.maxSize = 300;
        // 检测角度阈值
        SmartScanner.angleThreshold = 5;
        // don't forget reload params
        SmartScanner.reloadParams();
    }

    private void initCameraView() {
        mCameraView.getSmartScanner().setPreview(true);
        mCameraView.setOnScanResultListener(new SmartCameraView.OnScanResultListener() {
            @Override
            public boolean onScanResult(SmartCameraView smartCameraView, int result, byte[] yuvData) {
                // 这里是预览模式得到的图片，会一直调用这里，类似监听扫描回调
                Bitmap previewBitmap = smartCameraView.getPreviewBitmap();
                if (previewBitmap != null) {
                    ivPreview.setImageBitmap(previewBitmap);
                }
                return false;
            }
        });

        mCameraView.addCallback(new CameraImpl.Callback() {

            @Override
            public void onCameraOpened(CameraImpl camera) {
                super.onCameraOpened(camera);
            }

            @Override
            public void onPictureTaken(CameraImpl camera, byte[] data) {
                super.onPictureTaken(camera, data);
                mCameraView.cropJpegImage(data, new SmartCameraView.CropCallback() {
                    @Override
                    public void onCropped(Bitmap cropBitmap) {
                        if (cropBitmap != null) {
                            showPicture(cropBitmap);
                        }
                    }
                });
            }

        });
    }

    private void initMaskView() {
        final MaskView maskView = (MaskView) mCameraView.getMaskView();
        maskView.setMaskLineColor(0xff00adb5);
        maskView.setShowScanLine(false);
        maskView.setScanLineGradient(0xff00adb5, 0x0000adb5);
        maskView.setMaskLineWidth(2);
        maskView.setMaskRadius(5);
        maskView.setScanSpeed(6);
        maskView.setScanGradientSpread(80);
        mCameraView.post(new Runnable() {
            @Override
            public void run() {
                int width = mCameraView.getWidth();
                int height = mCameraView.getHeight();
                if (width < height) {
                    maskView.setMaskSize((int) (width * 0.6f), (int) (width * 0.6f / 0.63));
                    maskView.setMaskOffset(0, -(int)(width * 0.1));
                } else {
                    maskView.setMaskSize((int) (width * 0.6f), (int) (width * 0.6f * 0.63));
                }
            }
        });
        mCameraView.setMaskView(maskView);
    }

    private void showPicture(final Bitmap bitmap) {
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.preview_imgs, null);
            ImageView iv_img=view.findViewById(R.id.iv_img);
            MaterialButton mb_close=view.findViewById(R.id.mb_close);
            MaterialButton mb_sure=view.findViewById(R.id.mb_sure);

            iv_img.setImageBitmap(bitmap);
            mb_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    mCameraView.startScan();
                }
            });
            mb_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path= FileUtil.saveBitmap(getBaseContext(),bitmap);
                    Log.e(TAG, "onCropped:保存路径=== "+path );
                    // 把图片路径传回
                    alertDialog.dismiss();
                    finish();
                }
            });
            alertDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mCameraView.startScan();
                }
            });
            Window window = alertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.color.colorTrans);
            }
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request Camera permission first!
        if (granted) {
            mCameraView.start();
            mCameraView.startScan();
        }
    }


    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        mCameraView.stopScan();
    }
}
