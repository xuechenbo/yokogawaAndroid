package com.yokogawa.xc.demo.act;

import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseMvpActivity;

public class MainMvpActivity extends BaseMvpActivity<MainMvpPresenter> implements MainMvpContract.View {
    private MainMvpPresenter mainMvpPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.main_item_1;
    }

    @Override
    public void initView() {
        mainMvpPresenter = new MainMvpPresenter(this);
        mainMvpPresenter.attachView(this);
        mainMvpPresenter.getData();
    }

    @Override
    public void success(String msg) {
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

}
