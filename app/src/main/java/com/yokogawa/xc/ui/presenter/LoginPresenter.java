package com.yokogawa.xc.ui.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.yokogawa.xc.base.BasePresenter;
import com.yokogawa.xc.bean.LoginBean;
import com.yokogawa.xc.ui.contract.LoginContract;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;
import java.util.HashMap;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    public LoginPresenter(LifecycleOwner owner) {
        super(owner);
    }
    @Override
    public void login(HashMap paramsMap) {
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        RetrofitNet.getInstance().getApi().login(Utils.getRequestBody(paramsMap))
                .enqueue(new ResultCallback<HttpResult<LoginBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<LoginBean>> response) {
                        LoginBean data = response.body().getData();
                        mView.lgoinsuccess(data);
                    }

                    @Override
                    public void onFail(String message) {
                        mView.loginFaile(message);
                    }
                });
    }
}
