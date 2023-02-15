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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.adapter.ShowResAdapter;
import com.yokogawa.xc.bean.ShowResumBean;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.view.ItemNbDecoration;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class ShowResumeMsgDialog extends DialogFragment {
    private List<ShowResumBean.ChildList> mList;
    private ShowResAdapter showResAdapter;
    private String templateId;
    private int page = 1;
    private SmartRefreshLayout smartRefreshLayout;

    public static ShowResumeMsgDialog getInstance(String content) {
        ShowResumeMsgDialog showResumeMsgDialog = new ShowResumeMsgDialog();
        Bundle bundle = new Bundle();
        bundle.putString("templateId", content);
        showResumeMsgDialog.setArguments(bundle);
        return showResumeMsgDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MyDialog);
        templateId = (String) getArguments().getString("templateId");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.dialogfragment_showresume, container);
        initData(inflate);
        return inflate;
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
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.7), (int) (dm.heightPixels * 0.5));
        }
        dialog.setCanceledOnTouchOutside(false);
    }

    private void initData(View view) {
        mList = new ArrayList<>();
        Dialog dialog = getDialog();
//        dialog.setCanceledOnTouchOutside(false);
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.CENTER);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        smartRefreshLayout = view.findViewById(R.id.smartRefresh);
        view.findViewById(R.id.tv_submit).setOnClickListener(view12 -> dialog.dismiss());
        view.findViewById(R.id.close).setOnClickListener(view1 -> dialog.dismiss());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView.getItemDecorationCount() > 0) {
            RecyclerView.ItemDecoration itemDecorationAt = recyclerView.getItemDecorationAt(0);
            if (itemDecorationAt == null) {
                recyclerView.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
            }
        } else {
            recyclerView.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
        }
        showResAdapter = new ShowResAdapter(mList);
        showResAdapter.bindToRecyclerView(recyclerView);
        initLoading();
        getResuMsg(page);
    }

    private void initLoading() {
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getResuMsg(page);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mList.clear();
                getResuMsg(page);
            }
        });
    }

    void getResuMsg(int page) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMore(1000);
            smartRefreshLayout.finishRefresh();
        }
        RetrofitNet.getInstance().getApi().queryResumeList(Utils.getTokenMsg(), templateId, "0", 10, page)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        ShowResumBean showResumBean = new Gson().fromJson(data, ShowResumBean.class);
                        List<ShowResumBean.ChildList> list = showResumBean.getList();
                        mList.addAll(list);
                        showResAdapter.setNewData(mList);
                    }

                    @Override
                    public void onFail(String message) {
                        T.show(message);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
