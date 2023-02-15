package com.yokogawa.xc.ui.act;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.LoginBean;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TimeCount timeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_span_page);
        initData();
    }

    private void initData() {
        timeCount = new TimeCount(2000, 1000);
        timeCount.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        String token = SpUtils.getInstance(SplashActivity.this).getString("token", "");
        if (token.isEmpty()) {
            //token为空 未登录
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            //TODO !!!更新一下token
//            updataToken();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    private void updataToken() {
        RetrofitNet.getInstance().getApi().updataToken(Utils.getTokenMsg())
                .enqueue(new ResultCallback<HttpResult<LoginBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<LoginBean>> response) {
                        LoginBean data = response.body().getData();
                        saveMsg(data);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    private void saveMsg(LoginBean data) {
        SpUtils.getInstance(this).save("token", data.getToken());
        SpUtils.getInstance(this).save("tokenHead", data.getTokenHead());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
