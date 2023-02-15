package com.yokogawa.xc.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.MastBean;
import com.yokogawa.xc.dialog.SubmitShowNgDialog;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import retrofit2.Response;

public class CustomPopup extends CenterPopupView {
    public String type = "0";

    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public CustomPopup(@NonNull Context context, String type) {
        super(context);
        this.type = type;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvshow = findViewById(R.id.tv_ng);
        tvshow.setText(type.equals("2") ? "师带徒确认" : "检测到NG");
        EditText editText = findViewById(R.id.ed_key);
        editText.setHint(type.equals("2") ? "请师傅确认密钥" : "请联系管理员输入密钥");
        findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = editText.getText().toString().trim();
                if (!trim.isEmpty()) {
                    getKey(trim);
                } else {
                    T.show("请先输入密钥");
                }

            }
        });
    }

    // 设置最大宽度，看需要而定，
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }


    protected void getKey(String keyCode) {
        RetrofitNet.getInstance().getApi().secretKeyCheck(Utils.getTokenMsg(), type, keyCode, Utils.getGroupId())
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        if (type.equals("2")) {
                            //师带徒
                            MastBean mastBean = GsonUtil.GsonToBean(data, MastBean.class);
                            onSubmitListener.onsubmitClick(mastBean.getAutographUrl());
                        }
                        dismiss();
                    }

                    @Override
                    public void onFail(String message) {
                        T.show(message);
                    }
                });
    }


    public OnSubmitListener onSubmitListener = null;

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener {
        void onsubmitClick(String trim);
    }

}
