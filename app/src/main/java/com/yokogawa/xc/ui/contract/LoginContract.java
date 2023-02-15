package com.yokogawa.xc.ui.contract;


import com.yokogawa.xc.base.BaseView;
import com.yokogawa.xc.bean.LoginBean;
import com.yokogawa.xc.worknet.HttpResult;

import java.util.HashMap;

import retrofit2.Response;

public interface LoginContract {

    interface View extends BaseView {
        void lgoinsuccess(LoginBean response);
        void loginFaile(String msg);
    }
    interface  Presenter{
        void login(HashMap paramsMap);
    }

    interface Model {
        String log();
    }
}
