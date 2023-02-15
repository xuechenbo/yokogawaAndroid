package com.yokogawa.xc.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.adapter.ShowResAdapter;
import com.yokogawa.xc.bean.ShowResumBean;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.view.ItemNbDecoration;
import com.yokogawa.xc.view.SubmitNgPopup;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class SubmitShowNgDialog extends DialogFragment {

    public static SubmitShowNgDialog getInstance() {
        SubmitShowNgDialog showResumeMsgDialog = new SubmitShowNgDialog();
        return showResumeMsgDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MyDialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.3), (int) (dm.heightPixels * 0.3));
        }
        dialog.setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.submit_ng_popup, container);
        initData(inflate);
        return inflate;
    }

    private void initData(View view) {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.tv_submit).setOnClickListener(view12 -> {
            if (ClickUtils.isFastClick()) {
                onSubmitListener.onsubmitClick();
            }
        });
        view.findViewById(R.id.tv_close).setOnClickListener(view1 -> dialog.dismiss());
    }



    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public OnSubmitListener onSubmitListener = null;

    public interface OnSubmitListener {
        void onsubmitClick();
    }

}
