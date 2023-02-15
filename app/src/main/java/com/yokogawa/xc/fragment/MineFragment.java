package com.yokogawa.xc.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.PadBean;
import com.yokogawa.xc.bean.TokenBean;
import com.yokogawa.xc.ui.act.ChangePawActivity;
import com.yokogawa.xc.ui.act.CheckListActivity;
import com.yokogawa.xc.ui.act.LoginActivity;
import com.yokogawa.xc.ui.act.PadManagerActivity;
import com.yokogawa.xc.ui.act.PersonalInfoActivity;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.DeviceUtils;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.KvUtils;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.UpdateManager;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.ui.act.DrawNameActivity;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class MineFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_back)
    RelativeLayout tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sartRefresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.pad_Manager)
    TextView pad_Manager;
    @BindView(R.id.now_version)
    TextView now_version;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_mine, container, false);
        unbinder = ButterKnife.bind(this, root);
        initData();
        return root;
    }

    private void initData() {
        tvTitle.setText("账号中心");
        tvBack.setVisibility(View.GONE);
        try {
            now_version.setText(String.format("当前版本:%s", Utils.getVersionName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getTokenMsg(true);
                getPadMessage();
            }
        });
        getPadMessage();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getTokenMsg(false);
        }
    }

    public void getTokenMsg(boolean type) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
        RetrofitNet.getInstance().getApi().getTokenMsg(Utils.getTokenMsg())
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String str = response.body().getData();
                        TokenBean data = GsonUtil.GsonToBean(str, TokenBean.class);
                        //保存员工信息
                        SpUtils.getInstance(getActivity()).save("staffName", data.getStaffName());
                        String staffName = SpUtils.getInstance(getActivity()).getString("staffName", "");
                        tvUsername.setText(staffName);
                        SpUtils.getInstance(getActivity()).save("jobNumber", data.getJobNumber());
                        SpUtils.getInstance(getActivity()).save("jobId", data.getId());
                        SpUtils.getInstance(getActivity()).save("autographUrl", data.getAutographUrl());
                        if ((data.getAutographUrl().equals("默认") || data.getAutographUrl().isEmpty()) && type) {
                            //去签名
                            T.show("请先签名");
                            startActivity(new Intent(getActivity(), DrawNameActivity.class));
                        }
                    }


                    @Override
                    public void onFail(String message) {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                        }
                        T.show(message);
                    }
                });
    }

    //获取pad信息
    public void getPadMessage() {
        RetrofitNet.getInstance().getApi().getPadMessage(DeviceUtils.getAndroidId(getActivity())).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                try {
                    String str = response.body().getData();
                    PadBean padBean = GsonUtil.GsonToBean(str, PadBean.class);
                    SpUtils.getInstance(getActivity()).save("padNo", padBean.getPadNo());
                    SpUtils.getInstance(getActivity()).save("groupId", padBean.getGroupId());
                    SpUtils.getInstance(getActivity()).save("stationId", padBean.getStationId());
                    SpUtils.getInstance(getActivity()).save("groupName", padBean.getGroupName());
                    SpUtils.getInstance(getActivity()).save("stationName", padBean.getStationName());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFail(String message) {
                if (message.equals("未找到信息")) {
                    SpUtils.getInstance(MyApplication.getInstance()).remove("groupName");
                    SpUtils.getInstance(MyApplication.getInstance()).remove("stationName");
                    SpUtils.getInstance(MyApplication.getInstance()).remove("groupId");
                    SpUtils.getInstance(MyApplication.getInstance()).remove("stationId");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_head, R.id.tvMaterial, R.id.loginout, R.id.versionUpdate,
            R.id.look_record, R.id.updatepsw, R.id.pad_Manager, R.id.rl_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMaterial: //个人资料
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                break;
            case R.id.pad_Manager:
                startActivity(new Intent(getActivity(), PadManagerActivity.class));
                break;
            case R.id.look_record:
                //查看提交记录
                startActivity(new Intent(getActivity(), CheckListActivity.class));
                break;
            case R.id.updatepsw:
                startActivity(new Intent(getActivity(), ChangePawActivity.class));
                break;
            case R.id.versionUpdate:
            case R.id.rl_update:
                UpdateManager.getInstance().getVersion(getActivity());
                break;
            case R.id.loginout:
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("退出登录？")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
        }
    }

    private void logout() {
        RetrofitNet.getInstance().getApi().logout(Utils.getTokenMsg())
                .enqueue(new ResultCallback<HttpResult<TokenBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<TokenBean>> response) {
                        //保存员工信息
                        SpUtils.getInstance(MyApplication.getInstance()).remove("jobNumber");
                        SpUtils.getInstance(MyApplication.getInstance()).remove("jobId");
                        SpUtils.getInstance(MyApplication.getInstance()).remove("password");
                        SpUtils.getInstance(MyApplication.getInstance()).remove("autographUrl");
                        SpUtils.getInstance(MyApplication.getInstance()).remove("token");
                        KvUtils.clearAll();
                        //TODO groupId  stationId  不清空
                        T.show("退出登录");
                        //打开登录页面
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        SpUtils.getInstance(getActivity()).remove("token");
                        Log.e("TAG", "onFail===" + message);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });
    }
}
