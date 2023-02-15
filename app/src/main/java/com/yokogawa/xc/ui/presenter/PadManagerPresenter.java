package com.yokogawa.xc.ui.presenter;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.base.BasePresenter;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.bean.GroupBean;
import com.yokogawa.xc.bean.PadBean;
import com.yokogawa.xc.ui.contract.ExaminationContract;
import com.yokogawa.xc.ui.contract.PadManagerContract;
import com.yokogawa.xc.utils.DeviceUtils;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Response;

public class PadManagerPresenter extends BasePresenter<PadManagerContract.View> implements PadManagerContract.Presenter {

    public PadManagerPresenter(LifecycleOwner owner) {
        super(owner);
    }


    //获取pad信息
    @Override
    public void getPadMessage(String id) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().getPadMessage(id).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String str = response.body().getData();
                PadBean padBean = GsonUtil.GsonToBean(str, PadBean.class);
                SpUtils.getInstance(MyApplication.getInstance()).save("padNo", padBean.getPadNo());
                SpUtils.getInstance(MyApplication.getInstance()).save("padId", padBean.getId());
                SpUtils.getInstance(MyApplication.getInstance()).save("groupId", padBean.getGroupId());
                SpUtils.getInstance(MyApplication.getInstance()).save("stationId", padBean.getStationId());
                SpUtils.getInstance(MyApplication.getInstance()).save("groupName", padBean.getGroupName());
                SpUtils.getInstance(MyApplication.getInstance()).save("stationName", padBean.getStationName());
                mView.msgInfo(padBean);
            }

            @Override
            public void onFail(String message) {
                mView.showButton(message);
            }
        });
    }

    //查询组立线
    @Override
    public void getGroupList() {
        if (!isViewAttached()) {
            return;
        }
        ArrayList<String> strings = new ArrayList<>();
        RetrofitNet.getInstance().getApi().getGroupList(Utils.getTokenMsg()).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String str = response.body().getData();
                List<GroupBean> groupBeanList = new Gson().fromJson(str, new TypeToken<List<GroupBean>>() {
                }.getType());
                for (int i = 0; i < groupBeanList.size(); i++) {
                    strings.add(groupBeanList.get(i).getGroupName());
                }
                String[] arr = strings.toArray(new String[strings.size()]);
                mView.showBottomDialog(groupBeanList, arr, "1");
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });
    }

    //查询工位
    @Override
    public void getStationList(String groupId) {
        if (!isViewAttached()) {
            return;
        }
        ArrayList<String> strings = new ArrayList<>();
        RetrofitNet.getInstance().getApi().getStationList(Utils.getTokenMsg(), groupId).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String str = response.body().getData();
                List<GroupBean> stationList = new Gson().fromJson(str, new TypeToken<List<GroupBean>>() {
                }.getType());
                if (stationList.size() == 0) {
                    T.show("该组立线暂无工位");
                    return;
                }
                for (int i = 0; i < stationList.size(); i++) {
                    strings.add(stationList.get(i).getName());
                }
                String[] arr = strings.toArray(new String[strings.size()]);
                mView.showBottomDialog(stationList, arr, "2");
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });


    }


    //更新信息
    @Override
    public void updatePadMessage(HashMap paramsMap) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().updatePadDevice(Utils.getRequestBody(paramsMap)).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();
                SpUtils.getInstance(MyApplication.getInstance()).save("ruleMap", data);
                Log.e("TAG", "ruleMap====" + data);
                mView.updateDeviceSuccess();
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });

    }

    //添加信息
    @Override
    public void addPadDevice(HashMap paramsMap) {
        if (!isViewAttached()) {
            return;
        }
        RetrofitNet.getInstance().getApi().addPadDevice(Utils.getRequestBody(paramsMap)).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();
                SpUtils.getInstance(MyApplication.getInstance()).save("ruleMap", data);
                Log.e("TAG", "ruleMap====" + data);
                mView.addDeviceSuccess();
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });

    }
}
