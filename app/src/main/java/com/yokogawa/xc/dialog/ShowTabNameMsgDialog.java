package com.yokogawa.xc.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.yokogawa.xc.R;
import com.yokogawa.xc.utils.T;


public class ShowTabNameMsgDialog extends DialogFragment {
    public static ShowTabNameMsgDialog getInstance() {
        ShowTabNameMsgDialog showResumeMsgDialog = new ShowTabNameMsgDialog();
        return showResumeMsgDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MyDialog);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.dialog_tabname, container);
        initData(inflate);
        return inflate;
    }

    private void initData(View view) {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText etName = view.findViewById(R.id.et_name);
        view.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etName.getText().toString().isEmpty()) {
                    tabNameListener.getTabNameListener(etName.getText().toString());
                    dialog.dismiss();
                } else {
                    T.show( "请输入内容");
                }
            }
        });

        view.findViewById(R.id.close).setOnClickListener(view1 -> dialog.dismiss());
    }

    public interface TabNameListener {
        void getTabNameListener(String string);
    }

    TabNameListener tabNameListener = null;

    public void setTabNameListener(TabNameListener tabNameListener) {
        this.tabNameListener = tabNameListener;
    }

}
