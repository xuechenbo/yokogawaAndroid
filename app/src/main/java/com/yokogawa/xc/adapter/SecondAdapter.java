package com.yokogawa.xc.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.view.ItemNbDecoration;

import java.util.List;

public class SecondAdapter extends BaseQuickAdapter<NewExamBean.Project.Check, BaseViewHolder> {
    private boolean isEnable = true;
    List<Boolean> columnNameIsShow;
    private Context context;
    private String modeName;

    public SecondAdapter(@Nullable List<NewExamBean.Project.Check> data, boolean isEnable, List<Boolean> columnNameIsShow, Context context, String modeName) {
        super(R.layout.second_item, data);
        this.isEnable = isEnable;
        this.columnNameIsShow = columnNameIsShow;
        this.context = context;
        this.modeName = modeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewExamBean.Project.Check item) {
        TextView tv_Name = helper.getView(R.id.tv_FirstName);
        tv_Name.setText(item.getCheckName().trim());
        tv_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_Name.getText().toString().trim().length() > 22) {
                    final XPopup.Builder builder = new XPopup.Builder(context)
                            .watchView(tv_Name);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builder.asAttachList(new String[]{item.getCheckName().trim()}, null,
                                    new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String text) {
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        });
        List<NewExamBean.Project.Check.Children> children = item.getChildren();
        //TODO 对children进行增加规则
        FormalExamItemAdapter examItemAdapter = new FormalExamItemAdapter(children, isEnable, columnNameIsShow, context, modeName);
        RecyclerView recycler = helper.getView(R.id.rectangles);
        if (recycler.getItemDecorationCount() > 0) {
            RecyclerView.ItemDecoration itemDecorationAt = recycler.getItemDecorationAt(0);
            if (itemDecorationAt == null) {
                recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, false));
            }
        } else {
            recycler.addItemDecoration(new ItemNbDecoration(MyApplication.getInstance(), 0, 5, false));
        }
        examItemAdapter.bindToRecyclerView(recycler);
    }
}
