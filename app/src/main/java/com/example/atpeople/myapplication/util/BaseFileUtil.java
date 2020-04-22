package com.example.atpeople.myapplication.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.atpeople.myapplication.AppStart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Create by peng on 2019/4/25
 */
public class BaseFileUtil {
    public static final String ROOT_DIR = AppStart.getContext().getPackageName();
    public static final String IMAGE_DIR = "image";
    public static final String AUDIO_DIR = "audio";
    public static final String FileProviderAuthorities = "com.ici.fms2.fileprovider";


    /**
     * 获取图片缓存目录
     */
    public static String getImageDir() {
        return getDir(IMAGE_DIR);
    }

    /**
     * 获取音频目录
     */
    public static String getAudiodDir() {
        return getDir(AUDIO_DIR);
    }

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getAppCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        createDirs(path);
        return path;
    }

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Author Peng
     * @Date 2019/8/13 14:26
     * @Describe 获取手机文件的uri
     */
    public static Uri getFileUri(File file) {
        String authority = FileProviderAuthorities;
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(AppStart.getContext(), authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * @Author Peng
     * @Date 2019/8/13 14:07
     * @Describe 加载本地图片Bitmap
     */
    public static Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断文件存在性，不存在则创建
     */

    public static File makeFileIfNotExists(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取SD下的应用目录
     */
    public static String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 获取应用的cache目录
     */
    private static String getAppCachePath() {
        File f = AppStart.getContext().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            //ToastUtil.showInfo("获取文件大小不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     * 数据存储是以“字节”（Byte）为单位，数据传输是以大多是以“位”（bit，又名“比特”）为单位，
     * 一个位就代表一个0或1（即二进制），每8个位（bit，简写为b）组成一个字节（Byte，简写为B），是最小一级的信息单位。
     * 字节（B，Byte）、千字节（KB，KiloByte）、兆字节（MB，MegaByte）、吉字节（GB，Gigabyte）、太字节（TB，TeraByte）
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * @Author Peng
     * @Date 2019/5/8 15:28
     * @Describe 删除文件, 递归删除
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return "b_card_" + System.currentTimeMillis();
    }

    /**
     * @param context
     * @param mBitmap
     * @return
     * @Author Peng
     * @Date 2019/9/24 9:12
     * 保存bitmap到本地
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        // 通知图库更新
        updatePhotoMedia(filePic, context);
        // 旧版的，有些机型更新不感到
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
        return filePic.getAbsolutePath();
    }

    /**
     * @Author Peng
     * @Date 2019/8/13 13:56
     * @Describe 通知图库更新
     */
    private static void updatePhotoMedia(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }


    /**
     * @Author Peng
     * @Date 2019/8/9 14:49
     * @Describe 根据uri获取文件名
     */
    public static String getRealPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        int columnCount = cursor.getColumnCount();
        Log.e("tage", columnCount + "");
        if (cursor.moveToNext()) {
            String textName = cursor.getString(cursor.getColumnIndex("_display_name"));
            return textName;
        }
        cursor.close();
        return null;
    }

    /**
     * @Author Peng
     * @Date 2019/8/9 14:46
     * @Describe 根据uri 把数据写入vcf文件
     */
    public static String getPathByUri(Uri contentUri, String dFileName, Context context) {
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + dFileName;
        AssetFileDescriptor fd;
        try {
            fd = context.getContentResolver().openAssetFileDescriptor(contentUri, "r");
            // 根据uri转化文件流形式
            FileInputStream fis = fd.createInputStream();
            byte[] data = new byte[1024 * 1];
            // 创建一个向指定 File 对象表示的文件中写入数据的文件输出流。
            FileOutputStream fout = new FileOutputStream(path, false);
            int len = -1;
            // 读取fis的流数据,写入到文件的流当中
            while ((len = fis.read(data)) != -1) {
                //把转化后的流写到vcf文件中
                fout.write(data, 0, len);
            }
            return path;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * @Author Peng
     * @Date 2019/8/9 11:24
     * @Describe 根据uri获取图片
     */
    public static String getImgPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }
        cursor.close();
        return null;
    }

    /**
     * @Author Peng
     * @Date 2020/2/19 8:30
     * @Describe 检测是否root，目前只有谷歌XL手机能测试，其他未测试
     */
    public static void haveRoot(Context context) {
        try {
            if (Runtime.getRuntime().exec("su").getOutputStream() == null) {
                Toast.makeText(context, "没有root!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "已经root!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppAlive(Context context, String packageName) {
        // 获取活动管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        // 获取正在运行的进程信息
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        // 遍历所有 进程
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            // 进程名字 一般和包名一样
            String processName = runningAppProcessInfo.processName;
            Log.e(TAG, "isAppAlive: " + processName);
        }
        return false;
    }


    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            Log.e(TAG, "GPS定位: 已经打开");
            return true;
        }
        Log.e(TAG, "GPS定位: 处于关闭状态");
        return false;
    }

    /**
     * 提示用户去开启定位服务
     **/
    public static void toOpenGPS(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Tips")
                .setMessage("Mobile phone location service is not enabled. Do you want to open it?")
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                return getDataColumn(context, contentUri, null, null);
                File file = new File(uri.getPath());
                return file.getAbsolutePath();
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if (start!=-1 && end!=-1) {
            return pathandname.substring(start+1, end);
        }
        else {
            return null;
        }
    }

    public static byte[] file2Bytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1;) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
