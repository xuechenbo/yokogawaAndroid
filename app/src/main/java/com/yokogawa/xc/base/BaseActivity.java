package com.yokogawa.xc.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.utils.SpUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    //共通操作
//    private Operation mBaseOperation = null;
    public Activity context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutId());
        unbinder = ButterKnife.bind(this);
//        mBaseOperation = new Operation(this);
        context = this;
        //添加到栈中
//        ActivityManager.getInstance().add(this);
        setStatusBar();
        initView();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();


    /**
     * 状态栏
     */
    public void setStatusBar() {
//        StatusBarUtil.setColor(this,getResources().getColor(R.color.status_color),0);
    }

    /**
     * Context
     * @return
     */
//    @Override
//    public APP getApplicationContext() {
//        // TODO Auto-generated method stub
//        return (APP) super.getApplicationContext();
//    }

    /**
     * 加载进度
     *
     * @param message
     */
    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void showProgress(String message, boolean cancelable) {

    }

    public void hideProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ButterKnife
     */
    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    //登录员工token
    public String getTokenMsg() {
        String token = SpUtils.getInstance(this).getString("token", "");
        String tokenHead = SpUtils.getInstance(this).getString("tokenHead", "");
        return tokenHead + token;
    }

    //pad绑定的组立线id
    public String getGroupId() {
        return SpUtils.getInstance(MyApplication.getInstance()).getString("groupId", "");
    }

    //pad绑定的工位id
    public String getStationId() {
        return SpUtils.getInstance(MyApplication.getInstance()).getString("stationId", "");
    }
}
