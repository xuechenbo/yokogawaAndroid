package com.yokogawa.xc.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.yokogawa.xc.R;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.ModelTypeUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import retrofit2.Response;

public class ErrorCodePopup1 extends CenterPopupView {
    private Context context;
    private EditText ed_modiCode;

    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public ErrorCodePopup1(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.bookmsg_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        EditText ed_ModeName = findViewById(R.id.ed_ModeName);
        EditText ed_seriesNumber = findViewById(R.id.ed_seriesNumber);
        ed_modiCode = findViewById(R.id.ed_modiCode);
        TextView tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                String modeName = ed_ModeName.getText().toString().trim();
                if (!modeName.isEmpty()) {
                    //型名头部
                    getKey(modeName);
                } else {
                    T.show("不能为空");
                }
            }
        });
        findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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


    protected void getKey(String name) {
        RetrofitNet.getInstance().getApi().queryWebService(name)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        if (data == null || data.isEmpty() || "null".equals(data)) {
                            T.show("没有该指图书信息");
                        } else {
                            onSubmitListener.onsubmitClick1(data, name);
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
        void onsubmitClick1(String trim, String barCode);
    }

}
