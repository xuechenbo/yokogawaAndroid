package com.yokogawa.xc.base;

import androidx.lifecycle.LifecycleOwner;

import com.rxjava.rxlife.BaseScope;

public class BasePresenter<V extends BaseView> extends BaseScope {
    protected V mView;

    public BasePresenter(LifecycleOwner owner) {
        super(owner);
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        this.mView = view;
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        this.mView = null;
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return mView != null;
    }


}