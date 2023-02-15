package com.yokogawa.xc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.ShowResumBean;
import com.yokogawa.xc.utils.DateUtil;

import java.util.List;

public class ShowResAdapter extends BaseQuickAdapter<ShowResumBean.ChildList, BaseViewHolder> {
    public ShowResAdapter(@Nullable List<ShowResumBean.ChildList> data) {
        super(R.layout.item_shoresume, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShowResumBean.ChildList item) {
        helper.setText(R.id.tv_position, String.valueOf(helper.getAdapterPosition()))
                .setText(R.id.tv_modifyContent, item.getModifyContent())
                .setText(R.id.tv_creator, item.getCreator())
                .setText(R.id.tv_createTime, DateUtil.getDay(item.getCreateTime()))
                .setText(R.id.tv_updater, item.getUpdater())
                .setText(R.id.tv_updateTime, DateUtil.getDay(item.getUpdateTime()));

    }
}
