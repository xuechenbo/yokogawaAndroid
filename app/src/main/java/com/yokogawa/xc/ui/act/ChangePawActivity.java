package com.yokogawa.xc.ui.act;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;
import com.yokogawa.xc.event.MainFinish;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class ChangePawActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_same_password)
    EditText etSamePassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initView() {
        tv_title.setText("修改密码1111");
}

    @OnClick({R.id.btn_commit, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (checkLength(etOldPassword.getText().toString().trim()) && checkLength(etNewPassword.getText().toString().trim()) && checkLength(etSamePassword.getText().toString().trim())) {
                    if (etSamePassword.getText().toString().trim().equals(etNewPassword.getText().toString().trim())) {
                        updateInfo();
                    } else {
                        T.show( "两次输入的密码不一致1 1的阿斯蒂芬阿斯蒂芬阿道夫");
                    }
                } else {
                    T.show( "请输入6~16位的密码");
                }
                break;
        }
    }

    private void updateInfo() {
        //更新带嘛
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("jobNumber", SpUtils.getInstance(this).getString("jobNumber", ""));
        paramsMap.put("newPassword", etNewPassword.getText().toString().trim());
        paramsMap.put("oldPassword", etOldPassword.getText().toString().trim());
        RetrofitNet.getInstance().getApi().updatePassword(getTokenMsg(), Utils.getRequestBody(paramsMap))
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        T.show( "修改成功,请重新登录");
                        SpUtils.getInstance(ChangePawActivity.this).remove("password");
                        SpUtils.getInstance(ChangePawActivity.this).remove("token");
                        SpUtils.getInstance(ChangePawActivity.this).remove("tokenHead");
                        EventBus.getDefault().post(new MainFinish());
                        startActivity(new Intent(ChangePawActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        T.show( message);
                    }

                });
    }

    boolean checkLength(String str) {
        return str.length() >= 6 && str.length() <= 16;
    }
}
