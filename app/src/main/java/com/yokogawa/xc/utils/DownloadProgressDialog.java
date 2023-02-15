package com.yokogawa.xc.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * @author Ren-yibo
 * @date 2018/9/25
 * @description
 */
public class DownloadProgressDialog extends ProgressDialog {

    private Context context;
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;//android 10及以上版本

    public DownloadProgressDialog(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        setTitle("更新提示");
        setMessage("正在下载中，请稍后...");
        setIndeterminate(false);
        setMax(100);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    public void downLoad(String url) {
        String rootPath = "";
        if (isAndroidQ) {
            rootPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator +
                    "com.yokogawa.xc";
        } else {
            rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator +
                    "com.yokogawa.xc";
        }

        if (new File(rootPath).exists()) {
            new File(rootPath).mkdirs();
        }
        final String localPath = rootPath + File.separator + url.substring(url.lastIndexOf("/") + 1, url.length());
        LogUtil.e(localPath);
        LogUtil.e(rootPath);
        FileDownloader.setup(context);
        FileDownloader.getImpl().create(url)
                .setPath(localPath)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        long progress = (long) soFarBytes * 100 / totalBytes;
                        setProgress((int) (progress));
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        setProgress(100);
                        File localFile = new File(localPath);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //判断是否是AndroidN以及更高的版本
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(context, "com.yokogawa.xc".concat(".fileProvider"), localFile);
                            intent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                        } else {
                            intent.setDataAndType(Uri.fromFile(localFile), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        T.show("下载失败，请稍后重试");
                    }
                }).start();
    }

}
