package com.yokogawa.xc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.CurrListBean;
import com.yokogawa.xc.utils.Utils;

import java.util.List;

public class MainFrgAdapter extends BaseQuickAdapter<CurrListBean.ChildList, BaseViewHolder> {
    public MainFrgAdapter(int layoutResId, @Nullable List<CurrListBean.ChildList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CurrListBean.ChildList item) {
        helper.setText(R.id.tv_tableName, item.getCheckTableName())
                .setText(R.id.tv_status, Utils.getTypeName(item.getStatus(),item.getResult()));   //TODO 多状态
    }
}
