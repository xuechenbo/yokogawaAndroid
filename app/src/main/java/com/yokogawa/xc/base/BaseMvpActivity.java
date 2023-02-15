package com.yokogawa.xc.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {
    protected T mPresenter;
    //调度
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();

        mCompositeDisposable.dispose();
    }
}
