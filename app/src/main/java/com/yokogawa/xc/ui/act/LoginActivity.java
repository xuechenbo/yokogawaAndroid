package com.yokogawa.xc.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseMvpActivity;
import com.yokogawa.xc.bean.LoginBean;
import com.yokogawa.xc.ui.contract.LoginContract;
import com.yokogawa.xc.ui.presenter.LoginPresenter;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.MethodInputUtil;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.view.TextCheckView;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.captcha_edit)
    EditText captchaEdit;
    @BindView(R.id.tv_changeNet)
    TextView tv_changeNet;

    private LoginPresenter loginPresenter;

    //
    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loginPresenter = new LoginPresenter(this);
        loginPresenter.attachView(this);
        String jobNumber = SpUtils.getInstance(this).getString("jobNumber", "");
        String password = SpUtils.getInstance(this).getString("password", "");
        phoneEdit.setText(jobNumber);
        captchaEdit.setText(password);
        if (SpUtils.getInstance(MyApplication.getInstance()).getBoolean("isNetWorkType", true)){
            tv_changeNet.setText("当前环境：本地环境      切换到外网环境");
        }else{
            tv_changeNet.setText("当前环境：外网环境      切换到本地环境");
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void lgoinsuccess(LoginBean response) {
        SpUtils.getInstance(this).save("token", response.getToken());
        SpUtils.getInstance(this).save("tokenHead", response.getTokenHead());
        SpUtils.getInstance(this).save("level", response.getLevel());
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refresh", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFaile(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_login, R.id.tv_changeNet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                String phone = phoneEdit.getText().toString();
                String password = captchaEdit.getText().toString();
                SpUtils.getInstance(this).save("jobNumber", phoneEdit.getText().toString());
                SpUtils.getInstance(this).save("password", captchaEdit.getText().toString());
                if (phone.isEmpty() || password.length() < 6 || password.length() > 16) {
                    T.show("请检查账号密码");
                    return;
                }
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("jobNumber", phone);
                paramsMap.put("password", password);
                paramsMap.put("groupId", getGroupId());
                paramsMap.put("stationId", getStationId());
                loginPresenter.login(paramsMap);
                break;
            case R.id.tv_changeNet:
                RetrofitNet.getInstance().clearRetrofit();
                boolean isNetWorkType = SpUtils.getInstance(MyApplication.getInstance()).getBoolean("isNetWorkType", true);
                SpUtils.getInstance(MyApplication.getInstance()).save("isNetWorkType", !isNetWorkType);
                if (isNetWorkType) {
                    //本地环境
                    tv_changeNet.setText("当前环境：外网环境      切换到本地环境");
                } else {
                    //外网环境
                    tv_changeNet.setText("当前环境：本地测试      切换到外网环境");
                }
                break;
        }


    }
}
