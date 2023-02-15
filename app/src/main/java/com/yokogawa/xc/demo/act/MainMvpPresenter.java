package com.yokogawa.xc.demo.act;


import androidx.lifecycle.LifecycleOwner;

import com.yokogawa.xc.base.BasePresenter;

public class MainMvpPresenter extends BasePresenter<MainMvpContract.View> implements MainMvpContract.Presenter {

    private final MainMvpModel mainMvpModel;

    public MainMvpPresenter(LifecycleOwner owner) {
        super(owner);
        mainMvpModel = new MainMvpModel();
    }

    @Override
    public void getData() {
        if (!isViewAttached()) {
            return;
        }
        mainMvpModel.login();
    }
}
