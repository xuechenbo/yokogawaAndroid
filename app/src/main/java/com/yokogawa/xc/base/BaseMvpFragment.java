package com.yokogawa.xc.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/***
 * @function 自定义基类Fragment
 * @stone
 * @date 2021/6/7
 *
 */
public abstract class BaseMvpFragment extends Fragment {

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initArgs();
        View contentView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, contentView);
        initView(contentView, savedInstanceState);
        fetchData();

        return contentView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    public void fetchData() {
        initData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void initArgs() {

    }

    protected abstract Integer getLayoutId();

    protected abstract void initView(View contentView, Bundle savedInstanceState);

    protected abstract void initData();


    public String getFragmentTitle() {
        return "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }


}
