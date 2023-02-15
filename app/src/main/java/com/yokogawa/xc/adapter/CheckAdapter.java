package com.yokogawa.xc.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.CheckItemBean;
import com.yokogawa.xc.utils.DateUtil;

import java.text.ParseException;
import java.util.List;

public class CheckAdapter extends BaseQuickAdapter<CheckItemBean.ChildLiST, BaseViewHolder> {
    public CheckAdapter(@Nullable List<CheckItemBean.ChildLiST> data) {
        super(R.layout.check_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckItemBean.ChildLiST item) {
        try {
            String crateTime = DateUtil.date2String(item.getCreateTime());
            helper.setText(R.id.tv_tableName, String.format("%s_%s_%s", item.getGroupName(), item.getStationName(), item.getCheckName()))
                    .setText(R.id.tv_status, crateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
