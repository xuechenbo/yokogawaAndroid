package com.yokogawa.xc.worknet;

import android.content.Intent;
import android.util.Log;

import com.yokogawa.xc.MyApplication;

import com.yokogawa.xc.utils.Utils;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ResultCallback<T extends HttpResult> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().code() == 200) {
            try {
                if (response.body() == null) {
                    return;
                }
                int code = response.body().getCode();
                if (code == 200) {
                    onSuccess(response);
                } else if (code == 401) {
                    //发送消息 跳转到登录页面
                    com.yokogawa.xc.utils.T.show("token过期请重新登录");
                    Intent intent = new Intent("com.yokogawa.xc.goLogin");
                    MyApplication.getInstance().sendBroadcast(intent);
                } else if (code == 402) {
                    com.yokogawa.xc.utils.T.show(response.body().getMessage());
                    Intent intent = new Intent("com.yokogawa.xc.goLogin");
                    MyApplication.getInstance().sendBroadcast(intent);
//                    onFail(response.body().getMessage());
                } else {
                    onFail(response.body().getMessage());
                }
            } catch (Exception e) {
                onFail("");
                Log.e("TAG", "catch200");
            }
        } else {//失败响应
            try {
                Request request = response.raw().request();
                String url = request.url().toString();
                try {
                    String string = response.errorBody().string();
                    addErrorLog(string, url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onFailure(call, new RuntimeException(response.raw().message()));
            } catch (Exception e) {
                Log.e("TAG", "catch_else");
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable e) {
        String msgStr = "";
        if (e instanceof ConnectException || e instanceof SocketException) {
            //连接超时   请求超时
            msgStr = "网络连接超时，请检查您的网络状态！";
//            msgStr = "服务器请求超时";
        } else if (e instanceof SocketTimeoutException) {
            msgStr = "服务器响应超时";
        } else if (e instanceof RuntimeException) {
            msgStr = "数据请求错误";
        } else {
            msgStr = "数据异常";
        }
        onFail(msgStr);
    }

    public abstract void onSuccess(Response<T> response);

    public abstract void onFail(String message);

    public void addErrorLog(String errorMsg, String url) {
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
