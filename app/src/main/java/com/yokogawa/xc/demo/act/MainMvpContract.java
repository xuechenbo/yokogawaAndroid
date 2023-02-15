package com.yokogawa.xc.demo.act;


import com.yokogawa.xc.base.BaseView;

public interface MainMvpContract {

    interface View extends BaseView {
        void success(String msg);
    }
    interface  Presenter{
        void getData();
    }

    interface Model {
        String log();
    }
}
