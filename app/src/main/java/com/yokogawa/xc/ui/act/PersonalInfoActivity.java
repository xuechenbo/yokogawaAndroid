package com.yokogawa.xc.ui.act;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.T;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.tv_groupName)
    TextView tv_groupName;
    @BindView(R.id.tv_ProjectName)
    TextView tv_ProjectName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initView() {
        String jobNumber = SpUtils.getInstance(this).getString("jobNumber", "");
        String staffName = SpUtils.getInstance(this).getString("staffName", "");
        tvTitle.setText("个人信息");
        tv_name.setText(staffName);
        tv_id.setText(jobNumber);
    }

    @OnClick({R.id.tv_back, R.id.rl_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_sign:
                String autographUrl = SpUtils.getInstance(PersonalInfoActivity.this).getString("autographUrl", "");
                if (autographUrl.equals("默认") || autographUrl.isEmpty()) {
                    T.show( "暂无签名");
                    return;
                }
                startActivity(new Intent(PersonalInfoActivity.this, ShowSignActivity.class).putExtra("url", autographUrl));
                break;
        }
    }
}
