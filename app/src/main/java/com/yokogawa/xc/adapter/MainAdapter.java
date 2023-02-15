package com.yokogawa.xc.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.view.ItemNbDecoration;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<NewExamBean.Project, BaseViewHolder> {
    private boolean isEnable = true;
    private boolean isVisible = false;
    private SecondAdapter specAdapterTest;
    List<Boolean> columnNameIsShow;
    private Context context;
    private String modeName;

    //数据源，是否可编辑，检查者、检查日是否可见  上下文  型名
    public MainAdapter(@Nullable List<NewExamBean.Project> data, boolean isEnable, boolean isVisible, Context context, String modeName) {
        super(R.layout.exam_item, data);
        this.isEnable = isEnable;
        this.isVisible = isVisible;
        this.context = context;
        this.modeName = modeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewExamBean.Project item) {
        List<NewExamBean.Project.Check> check = item.getCheck();
        helper.setText(R.id.tv_FirstName, item.getProjectName().trim());
        specAdapterTest = new SecondAdapter(check, isEnable, columnNameIsShow, context, modeName);
        RecyclerView recycler = helper.getView(R.id.rectangles);
        if (recycler.getItemDecorationCount() > 0) {
            RecyclerView.ItemDecoration itemDecorationAt = recycler.getItemDecorationAt(0);
            if (itemDecorationAt == null) {
                recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
            }
        } else {
            recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, true));
        }
        specAdapterTest.bindToRecyclerView(recycler);
    }

    public void setIsVisible(List<Boolean> columnNameIsShow) {
        this.columnNameIsShow = columnNameIsShow;
    }
}
