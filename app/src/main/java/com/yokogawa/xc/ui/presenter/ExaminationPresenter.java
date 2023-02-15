package com.yokogawa.xc.ui.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.yokogawa.xc.base.BasePresenter;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.ui.contract.ExaminationContract;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Response;

public class ExaminationPresenter extends BasePresenter<ExaminationContract.View> implements ExaminationContract.Presenter {

    public ExaminationPresenter(LifecycleOwner owner) {
        super(owner);
    }

    //作业记录
    @Override
    public void queryWorkRecord(String id, String groupId) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().queryWorkRecord(Utils.getTokenMsg(), id, groupId, "0")
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        ExamDetailsBean examDetailsBean = GsonUtil.GsonToBean(data, ExamDetailsBean.class);
                        List<ExamDetailsBean.ChildList> mList = examDetailsBean.getList();
                        if (mList.size() == 0) {
                            T.show("暂无该检查表的作业记录");
                        } else {
                            mView.queryWorkRecordSucess(examDetailsBean);
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        T.show(message);
                    }
                });
    }

    //提交
    @Override
    public void subMitMsg(RequestBody requestBody, boolean isPause) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().insertRecord(Utils.getTokenMsg(), requestBody)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        mView.subMitMsgSuccess();
                    }

                    @Override
                    public void onFail(String message) {
                        mView.hideLoading();
                        T.show(message);
                    }
                });
    }


    //最后一个工位提交
    @Override
    public void finishProgress_Second(RequestBody requestBody) {
        if (!isViewAttached()) {
            return;
        }
        //先获取之前的工作记录
        RetrofitNet.getInstance().getApi().finishProgress(Utils.getTokenMsg(), requestBody)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        mView.finishProgress_Success();
                    }

                    @Override
                    public void onFail(String message) {
                        mView.hideLoading();
                        T.show(message);
                    }
                });
    }

    @Override
    public void staffSchedule(RequestBody requestBody) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().staffSchedule(Utils.getTokenMsg(), requestBody)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String id = jsonObject.getString("id");//进度表id
                            mView.staffScheduleSuccess(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(String message) {

                    }

                });
    }

}
