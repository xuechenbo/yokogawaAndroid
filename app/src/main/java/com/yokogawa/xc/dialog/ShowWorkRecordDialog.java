package com.yokogawa.xc.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.view.ItemNbDecoration;

import java.util.ArrayList;
import java.util.List;


public class ShowWorkRecordDialog extends DialogFragment {
    private DialogAdapter dialogAdapter;
    ArrayList<NewExamBean> list;
    private ExamDetailsBean examDetailsBean;
    private TextView tv_jf;
    private TextView tv_lf;
    private TextView tv_xm;
    private String typeStatus;
    private LinearLayout lin_lf;
    private LinearLayout lin_jf;
    private LinearLayout lin_zh;
    private LinearLayout lin_xm;
    private LinearLayout lin_Specification, lin_RemarkBench, lin_sprule;

    public static ShowWorkRecordDialog getInstance(ExamDetailsBean examDetailsBean, String typeStatus) {
        ShowWorkRecordDialog showResumeMsgDialog = new ShowWorkRecordDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("examDetailsBean", examDetailsBean);
        bundle.putString("typeStatus", typeStatus);
        showResumeMsgDialog.setArguments(bundle);
        return showResumeMsgDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MyDialog);
        examDetailsBean = (ExamDetailsBean) getArguments().getSerializable("examDetailsBean");
        typeStatus = getArguments().getString("typeStatus");
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
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.85), (int) (dm.heightPixels * 0.8));
        }
        dialog.setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.dialog_workrecord, container);
        initData(inflate);
        return inflate;
    }

    private void initData(View view) {
        list = new ArrayList<>();
        tv_jf = view.findViewById(R.id.tv_jf);
        tv_lf = view.findViewById(R.id.tv_lf);
        tv_xm = view.findViewById(R.id.tv_xm);
        lin_lf = view.findViewById(R.id.lin_lf);
        lin_jf = view.findViewById(R.id.lin_jf);
        lin_zh = view.findViewById(R.id.lin_zh);
        lin_xm = view.findViewById(R.id.lin_xm);
        lin_Specification = view.findViewById(R.id.lin_Specification);
        lin_RemarkBench = view.findViewById(R.id.lin_RemarkBench);
        lin_sprule = view.findViewById(R.id.lin_sprule);

        //检查日检查者
        LinearLayout linearLayout = view.findViewById(R.id.lin_examDR);
        linearLayout.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new ItemNbDecoration(getActivity(), 0, 5, true));

        dialogAdapter = new DialogAdapter(list, getActivity(), examDetailsBean.getList().get(0).getModeName());
        dialogAdapter.bindToRecyclerView(recyclerView);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        merge();
    }

    private void merge() {
        List<ExamDetailsBean.ChildList> mList = examDetailsBean.getList();
        //整合数据
        for (int i = 0; i < mList.size(); i++) {
            String checkContentJson = mList.get(i).getCheckContentJson();
            Log.e("TAG", "merge===" + checkContentJson);
            NewExamBean newExamBean = GsonUtil.GsonToBean(checkContentJson, NewExamBean.class);
            list.add(newExamBean);
        }
        if (mList.size() != 0) {
            tv_jf.setText(mList.get(0).getCalculateNumber());
            tv_lf.setText(mList.get(0).getSeriesNumber());
            tv_xm.setText(mList.get(0).getModeName());
        }
        if (list.size() != 0) {
            dialogAdapter.setIsVisible(list.get(0).getColumnIsShow());
            initHeadVisible(list.get(0).getColumnIsShow());
        }
        dialogAdapter.setNewData(list);
    }


    private void initHeadVisible(List<Boolean> columnIsShow) {
        Boolean aBoolean = columnIsShow.get(3);
        Boolean aBoolean1 = columnIsShow.get(5);
        if (aBoolean || aBoolean1) {
            lin_sprule.setVisibility(View.VISIBLE);
        }
        if (aBoolean) {
            //规格显示
            lin_Specification.setVisibility(View.VISIBLE);
        }
        if (aBoolean1) {
            lin_RemarkBench.setVisibility(View.VISIBLE);
        }
    }
}
