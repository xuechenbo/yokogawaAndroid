package com.yokogawa.xc.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import androidx.core.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * created by yxl
 * on 2021/6/18
 */
public class PhotoUtils {
    /**
     * 调用系统裁剪
     *
     * @param uri        需要裁剪的图片路径
     * @param mImagePath 图片输出路径
     * @param size       裁剪框大小
     */
    public static Intent startPhotoZoom(Uri uri, Uri mImagePath, int size) {
        return startPhotoZoom(uri, mImagePath, size, size);
    }

    public static Intent startPhotoZoom(Uri uri, String mImagePath, int size) {
        return startPhotoZoom(uri, Uri.fromFile(new File(mImagePath)), size, size);
    }

    /**
     * 调用系统裁剪<br>
     * 注:华为手机默认
     *
     * @param uri        需要裁剪的图片路径
     * @param mImagePath 图片输出路径
     * @param sizeX      裁剪x
     * @param sizeY      裁剪y
     */
    public static Intent startPhotoZoom(Uri uri, Uri mImagePath, int sizeX, int sizeY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //裁剪后输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImagePath);
        //输入图片路径
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("circleCrop", "true");
        intent.putExtra("aspectX", 9998);//2019/5/8 修复华为手机默认为圆角裁剪的问题
        intent.putExtra("aspectY", 9999);//
        intent.putExtra("outputX", sizeX);
        intent.putExtra("outputY", sizeY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        return intent;
    }


    //-------------------------------------
    //TODO 暂时先这样。。
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    public static Uri getUri(Context context) {
        File photoFile = null;
        Uri photoUri = null;
        if (Build.VERSION.SDK_INT >= 29) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            photoFile = new File(path, SpUtils.getInstance(context).getString("token", "") + ".png");
        } else if (isAndroidQ) {
            // 适配android 10
            //获取图片的沙盒文件夹
            File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //命名存储图片路径
            Log.d("沙盒文件路径", "openCamera: " + Environment.DIRECTORY_PICTURES);
            String filePath = file.getAbsolutePath() + "/" + IMAGE_FILE_NAME;
            photoFile = new File(filePath);
        } else {
            try {
                photoFile = createImageFile(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (photoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", photoFile);
            } else {
                photoUri = Uri.fromFile(photoFile);
            }
        }
        return photoUri;
    }

    private static File createImageFile(Context context) throws IOException {
        File tempFile = new File(context.getExternalCacheDir(), IMAGE_FILE_NAME);
        return tempFile;
    }


    private static int output_X = 480;
    private static int output_Y = 480;

    //裁剪图片
    public static Intent getCropIntent(Uri uri, Context context, String path) {
        Log.d("裁剪的Url", "cropRawPhoto: " + uri.toString());
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", false);
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //android11 分区存储
        if (Build.VERSION.SDK_INT >= 29) {
            Log.e("TAG", "裁剪公域：：" + path);
            File mOnputFile11 = new File(path, SpUtils.getInstance(context).getString("token", "") + ".png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mOnputFile11.getAbsolutePath()));
        } else if (isAndroidQ) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                    new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "face-cropped.jpg")));
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                    new File(context.getExternalCacheDir(), "face-cropped.jpg")));
        }
        return intent;
    }


    public static File saveBitmap(Context context, Bitmap bitmap) throws IOException {
        File file = new File(context.getExternalCacheDir(), "head");
        if (!file.exists()) {
            file.createNewFile();
        }
        try (OutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        return file;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    //质量压缩
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int length = baos.toByteArray().length / 1024;

        if (length > 5000) {
            //重置baos即清空baos
            baos.reset();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        } else if (length > 4000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        } else if (length > 3000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        } else if (length > 2000) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        //循环判断如果压缩后图片是否大于1M,大于继续压缩
        while (baos.toByteArray().length / 1024 > 2048) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
}
