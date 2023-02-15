package com.yokogawa.xc.utils;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.bean.PadBean;
import com.yokogawa.xc.event.MineRefresh;
import com.yokogawa.xc.worknet.BaseResult;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class netUtils {
    public static void submiEmpty(RequestBody requestBody) {
        RetrofitNet.getInstance().getApi().insertRecord(Utils.getTokenMsg(), requestBody)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
    }

    public static void getPadMessage() {
        RetrofitNet.getInstance().getApi().getPadMessage(DeviceUtils.getAndroidId(MyApplication.getInstance())).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String str = response.body().getData();
                PadBean padBean = GsonUtil.GsonToBean(str, PadBean.class);
                SpUtils.getInstance(MyApplication.getInstance()).save("padId", padBean.getId());
                SpUtils.getInstance(MyApplication.getInstance()).save("padNo", padBean.getPadNo());
                SpUtils.getInstance(MyApplication.getInstance()).save("groupId", padBean.getGroupId());
                SpUtils.getInstance(MyApplication.getInstance()).save("stationId", padBean.getStationId());
                SpUtils.getInstance(MyApplication.getInstance()).save("groupName", padBean.getGroupName());
                SpUtils.getInstance(MyApplication.getInstance()).save("stationName", padBean.getStationName());
                EventBus.getDefault().post(new MineRefresh());
            }

            @Override
            public void onFail(String message) {
            }
        });
    }

    public static void addErrorLog(String errorMsg, String url) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("creator", "");
        paramsMap.put("errorMessage", errorMsg);
        paramsMap.put("interfaceName", url);
        RetrofitNet.getInstance().getApi().addErrorLog(Utils.getRequestBody(paramsMap)).enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {

            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {

            }
        });
    }
}
