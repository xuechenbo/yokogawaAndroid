package com.yokogawa.xc.ui.act;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowSignActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_sing)
    ImageView iv_sing;

    @Override
    public int getLayoutId() {
        return R.layout.act_show_sign;
    }

    @Override
    public void initView() {
        tvTitle.setText("查看签名");
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(iv_sing);
    }

    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
