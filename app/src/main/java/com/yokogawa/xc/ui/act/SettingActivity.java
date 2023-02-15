package com.yokogawa.xc.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;
import com.yokogawa.xc.bean.TokenBean;
import com.yokogawa.xc.event.MainFinish;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.UpdateManager;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int getLayoutId() {
        return R.layout.act_settting;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tv_title.setText("设置");
    }

    @OnClick({R.id.versionUpdate, R.id.up_pws, R.id.logout, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.versionUpdate:
                //检查更新
                UpdateManager.getInstance().getVersion(this);
                break;
            case R.id.up_pws:
                startActivity(new Intent(SettingActivity.this, ChangePawActivity.class));
                break;
            case R.id.logout:
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("提示")
                        .setMessage("退出登录？")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
        }
    }

    private void logout() {
        RetrofitNet.getInstance().getApi().logout(getTokenMsg())
                .enqueue(new ResultCallback<HttpResult<TokenBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<TokenBean>> response) {
                        //保存员工信息
                        SpUtils.getInstance(SettingActivity.this).remove("jobNumber");
                        SpUtils.getInstance(SettingActivity.this).remove("jobId");
                        SpUtils.getInstance(SettingActivity.this).remove("groupName");
                        SpUtils.getInstance(SettingActivity.this).remove("projectName");
                        SpUtils.getInstance(SettingActivity.this).remove("level");
                        SpUtils.getInstance(SettingActivity.this).remove("status");
                        SpUtils.getInstance(SettingActivity.this).remove("token");
                        SpUtils.getInstance(SettingActivity.this).remove("tokenHead");
                        SpUtils.getInstance(SettingActivity.this).remove("password");
                        T.show( "退出登录");
                        //打开登录页面
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        EventBus.getDefault().post(new MainFinish());
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        T.show(message);
                    }
                });
    }

    @Subscribe
    public void mainFinish(MainFinish mainFinish) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
