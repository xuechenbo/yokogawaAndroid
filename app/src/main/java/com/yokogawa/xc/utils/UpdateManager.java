package com.yokogawa.xc.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.bean.AppBean;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author liuchao
 */
public class UpdateManager {
    private static UpdateManager instance;

    public static UpdateManager getInstance() {
        if (instance == null) {
            synchronized (UpdateManager.class) {
                if (instance == null) {
                    instance = new UpdateManager();
                }
            }
        }
        return instance;
    }

    private Activity mContext;

    public void getVersion(final Activity context) {
        this.mContext = context;
        RetrofitNet.getInstance().getApi().updateApp(Utils.getTokenMsg()).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();

                AppBean appBean = new Gson().fromJson(data, AppBean.class);
                try {
                    if (appBean.getVersion().equals(Utils.getVersionName())) {
                        T.show( "已经是最新版本");
                    } else {
                        Log.e("TAG", "onResponse==" + Utils.getVersionName());
                        switch (appBean.getType()) {
                            case 0:
                                new AlertDialog.Builder(context)
                                        .setCancelable(true)
                                        .setTitle("发现新版本，是否安装？")
                                        .setMessage(appBean.getUpgradePoint())
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onClickUpdate(appBean.getApkUrl());
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                break;
                            case 1:
                                new AlertDialog.Builder(context)
                                        .setCancelable(false)
                                        .setTitle("发现新版本，是否安装？")
                                        .setMessage(appBean.getUpgradePoint())
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onClickUpdate(appBean.getApkUrl());
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
//
//        new AlertDialog.Builder(context)
//                .setCancelable(true)
//                .setTitle("发现新版本，是否安装？")
//                .setMessage("测试版本更新")
//                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        onClickUpdate("https://raw.githubusercontent.com/jenly1314/ZXingLite/master/app/release/app-release.apk");
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .show();
    }


    private DownloadProgressDialog downloadProgressDialog;

    private void onClickUpdate(String url) {
        if (mContext != null) {
            PermissionX.init((FragmentActivity) mContext)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest()
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                            if (allGranted) {
                                if (null == downloadProgressDialog) {
                                    downloadProgressDialog = new DownloadProgressDialog(mContext);
                                }
                                downloadProgressDialog.show();
                                downloadProgressDialog.downLoad(url);
                            } else {
                                Toast.makeText(mContext, "这些权限被拒绝：" + deniedList.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
