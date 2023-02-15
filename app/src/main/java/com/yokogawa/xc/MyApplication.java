package com.yokogawa.xc;

import android.content.Context;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;
import com.yokogawa.xc.base.BaseApplication;
import com.yokogawa.xc.utils.KvUtils;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MyApplication extends BaseApplication {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        captureRxJava();
        initMMKV();
        initBugly();
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "b72ea07233", false);
    }

    private void initMMKV() {
        String mmkvPath = MMKV.initialize(this);
        KvUtils.getInstance();
        Log.e("TAG", "initMMKV_path ===" + mmkvPath);
    }

    public static Context getInstance() {
        return instance;
    }

    private void captureRxJava() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("TAG", "数据库操作异常----", throwable);
            }
        });
    }

}
