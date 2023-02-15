package com.yokogawa.xc.ui.act;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yokogawa.xc.R;
import com.yokogawa.xc.adapter.CheckAdapter;
import com.yokogawa.xc.base.BaseMvpActivity;
import com.yokogawa.xc.bean.CheckItemBean;
import com.yokogawa.xc.ui.contract.CheckListContract;
import com.yokogawa.xc.ui.presenter.CheckListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


//检查列表
public class CheckListActivity extends BaseMvpActivity<CheckListPresenter> implements CheckListContract.View {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sartRefresh)
    SmartRefreshLayout smartRefreshLayout;
    private CheckListPresenter checkListPresenter;
    List<CheckItemBean.ChildLiST> mList;
    private CheckAdapter checkAdapter;
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.act_checklist;
    }

    @Override
    public void initView() {
        tv_title.setText("提交记录");
        mList = new ArrayList<>();
        checkAdapter = new CheckAdapter(mList);
        checkAdapter.bindToRecyclerView(recyclerView);
        checkAdapter.setEmptyView(R.layout.empty_item, recyclerView);
        initListener();
        checkListPresenter = new CheckListPresenter(this);
        checkListPresenter.attachView(this);
        checkListPresenter.getCheckList(page);
    }

    private void initListener() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                checkListPresenter.refresh(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                checkListPresenter.loadMore(page);
            }
        });
        checkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CheckItemBean.ChildLiST checkItemBean = (CheckItemBean.ChildLiST) adapter.getData().get(position);
                Log.e("TAG", "onItemClick===" + checkItemBean.toString());
                Intent intent = new Intent(CheckListActivity.this, ExaminationFromActivity.class);
                intent.putExtra("typeStatus", "3");   //查看标记
                intent.putExtra("checkContentJson", checkItemBean.getCheckContentJson());   //展示内容
                intent.putExtra("title", checkItemBean.getCheckName());   //标题

                intent.putExtra("calculateNumber", checkItemBean.getCalculateNumber());//计番
                intent.putExtra("seriesNumber", checkItemBean.getSeriesNumber());//连番
                intent.putExtra("modeName", checkItemBean.getModeName());//型名

                intent.putExtra("id", checkItemBean.getProgressId() + "");//检查表id
                startActivity(intent);
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }


    @Override
    public void success(List<CheckItemBean.ChildLiST> list, int status) {
        if (status == 1) {
            mList.clear();
            mList.addAll(list);
            if (smartRefreshLayout != null) {
                smartRefreshLayout.finishRefresh();
            }
        } else {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.finishLoadMore();
            }
            mList.addAll(list);
        }
        checkAdapter.setNewData(mList);
    }

    @Override
    public void fail() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
