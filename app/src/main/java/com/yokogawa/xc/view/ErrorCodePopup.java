package com.yokogawa.xc.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.impl.AttachListPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yokogawa.xc.R;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.ExamUtils;
import com.yokogawa.xc.utils.ModelTypeUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.List;

import retrofit2.Response;

public class ErrorCodePopup extends CenterPopupView {
    private Context context;
    private EditText ed_modiCode;

    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public ErrorCodePopup(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.error_popup;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        EditText ed_ModeName = findViewById(R.id.ed_ModeName);
        EditText ed_seriesNumber = findViewById(R.id.ed_seriesNumber);  //连番
        EditText ed_calculateNumber = findViewById(R.id.ed_calculateNumber);   //计番
        EditText ed_linkage = findViewById(R.id.ed_linkage);   //linkage
        ed_modiCode = findViewById(R.id.ed_modiCode);
        TextView tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                String modeName = ed_ModeName.getText().toString().trim();
                String calculateNumber = ed_calculateNumber.getText().toString().trim();
                String seriesNumber = ed_seriesNumber.getText().toString().trim();
                String linkage = ed_linkage.getText().toString().trim();
                if (modeName.isEmpty()) {
                    T.show("型名不能为空");
                    return;
                }
                if (calculateNumber.isEmpty()) {
                    T.show("计番不能为空");
                    return;
                }
                if (seriesNumber.isEmpty()) {
                    T.show("连番不能为空");
                    return;
                }
                if (linkage.isEmpty()) {
                    T.show("linkage不能为空");
                    return;
                }
                getKey(ExamUtils.getNoEmptStr(modeName), calculateNumber, seriesNumber, linkage);
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

    /**
     * @param modeName
     * @param calculateNumber 计番
     * @param seriesNumber    连番
     * @param linkage
     */
    protected void getKey(String modeName, String calculateNumber, String seriesNumber, String linkage) {
        /**
         * 计番   calculateNumber
         * 连番   seriesNumber
         * linkage
         */
        if (Utils.getGroupId().isEmpty() || Utils.getStationId().isEmpty()) {
            T.show("请检查是否绑定工位~");
            return;
        }
        RetrofitNet.getInstance().getApi().insertStaffSchedule(Utils.getTokenMsg(), modeName, calculateNumber, Utils.getGroupId(), Utils.getStationId(), seriesNumber, linkage)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        onSubmitListener.onsubmitClick(data, modeName, calculateNumber, seriesNumber, linkage);
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
        void onsubmitClick(String trim, String modeName, String calculateNumber, String seriesNumber, String linkage);
    }

}
