package com.yokogawa.xc.ui.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.base.BasePresenter;
import com.yokogawa.xc.bean.CheckItemBean;
import com.yokogawa.xc.ui.contract.CheckListContract;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import retrofit2.Response;

public class CheckListPresenter extends BasePresenter<CheckListContract.View> implements CheckListContract.Presenter {
    public CheckListPresenter(LifecycleOwner owner) {
        super(owner);
    }

    @Override
    public void getCheckList(int page) {
        if (!isViewAttached()) {
            return;
        }
        int jobId = SpUtils.getInstance(MyApplication.getInstance()).getInt("jobId", 0);
        RetrofitNet.getInstance().getApi().queryProgressByStaffId(Utils.getTokenMsg(), String.valueOf(jobId), 20, page)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        CheckItemBean checkItemBean = new Gson().fromJson(data, CheckItemBean.class);
                        mView.success(checkItemBean.getList(), page);
                    }

                    @Override
                    public void onFail(String message) {
                        mView.fail();
                    }
                });
    }

    //刷新
    @Override
    public void refresh(int page) {
        getCheckList(page);
    }

    //加载
    @Override
    public void loadMore(int page) {
        getCheckList(page);
    }
}
