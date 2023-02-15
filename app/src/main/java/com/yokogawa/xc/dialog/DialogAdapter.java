package com.yokogawa.xc.dialog;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.adapter.MainAdapter;
import com.yokogawa.xc.view.ItemNbDecoration;

import java.util.List;

public class DialogAdapter extends BaseQuickAdapter<NewExamBean, BaseViewHolder> {
    private Context context;
    private String modName;
    private MainAdapter mainAdapter;
    List<Boolean> columnNameIsShow;

    public DialogAdapter(@Nullable List<NewExamBean> data, Context context, String modeName) {
        super(R.layout.dialog_item, data);
        this.context = context;
        this.modName = modeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewExamBean item) {
        helper.setGone(R.id.lin_examDR, true);
        helper.setText(R.id.tv_examDate, item.getCreateDate());
        ImageView imageView = helper.getView(R.id.iv_examer);
        ImageView iv_master = helper.getView(R.id.iv_master);
        Glide.with(context).load(item.getCreateName()).into(imageView);
        Glide.with(context).load(item.getMasterUrl()).into(iv_master);
        //添加检查者，检查日
        helper.setText(R.id.tv_FirstName, item.getStationName());
        //数据源，是否可编辑，检查者、检查日是否可见
        mainAdapter = new MainAdapter(item.getProject(), false, false, context, modName);
        mainAdapter.setIsVisible(columnNameIsShow);
        RecyclerView recycler = helper.getView(R.id.rectangles);
        if (recycler.getItemDecorationCount() > 0) {
            RecyclerView.ItemDecoration itemDecorationAt = recycler.getItemDecorationAt(0);
            if (itemDecorationAt == null) {
                recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
            }
        } else {
            recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
        }
        mainAdapter.bindToRecyclerView(recycler);
    }

    public void setIsVisible(List<Boolean> columnNameIsShow) {
        this.columnNameIsShow = columnNameIsShow;
    }
}
