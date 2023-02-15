package com.yokogawa.xc.ui.act;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.yokogawa.xc.R;
import com.yokogawa.xc.adapter.TabFragmentPagerAdapter;
import com.yokogawa.xc.bean.AppBean;
import com.yokogawa.xc.bean.TokenBean;
import com.yokogawa.xc.event.MainFinish;
import com.yokogawa.xc.event.MineRefresh;
import com.yokogawa.xc.event.RefresToken;
import com.yokogawa.xc.event.RefreshMain;
import com.yokogawa.xc.fragment.MainFragment;
import com.yokogawa.xc.fragment.MineFragment;
import com.yokogawa.xc.receiver.LoginUIReceiver;
import com.yokogawa.xc.utils.DownloadProgressDialog;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.netUtils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    TabFragmentPagerAdapter mAdapter;
    @BindView(R.id.rbMain)
    RadioButton rbMain;
    @BindView(R.id.rbMine)
    RadioButton rbMine;
    @BindView(R.id.bottomRg)
    RadioGroup bottomRg;
    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.tv_code)
    TextView tv_code;
    private List<Fragment> mList;
    MainFragment mainFragment = new MainFragment();
    MineFragment mineFragment = new MineFragment();
    private LoginUIReceiver loginUIReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
        initUI();
        getVersion(this);
    }

    private void getVersion(Context context) {
        RetrofitNet.getInstance().getApi().updateApp(Utils.getTokenMsg()).enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();

                AppBean appBean = new Gson().fromJson(data, AppBean.class);
                try {
                    if (appBean.getVersion().equals(Utils.getVersionName())) {
                    } else {
                        Log.e("TAG", "onResponse==" + Utils.getVersionName());
                        switch (appBean.getType()) {
                            case 0:
                                new AlertDialog.Builder(context)
                                        .setCancelable(true)
                                        .setTitle("发现新版本，是否安装？")
                                        .setMessage(appBean.getUpgradePoint())
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onClickUpdate(context, appBean.getApkUrl());
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                                break;
                            case 1:
                                new AlertDialog.Builder(context)
                                        .setCancelable(false)
                                        .setTitle("发现新版本，是否安装？")
                                        .setMessage(appBean.getUpgradePoint())
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onClickUpdate(context, appBean.getApkUrl());
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFail(String message) {

            }
        });
    }

    private void initView() {
        getTokenDetail();
        initListener();
        if (Utils.getStationId().isEmpty()) {
            netUtils.getPadMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginUIReceiver = new LoginUIReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yokogawa.xc.goLogin");
        registerReceiver(loginUIReceiver, intentFilter);
        if (mineFragment != null) {
//            mineFragment.getTokenMsg(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        unregisterReceiver(loginUIReceiver);
    }

    private void initListener() {
        bottomRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbMain:
                        vp_view.setCurrentItem(0, false);
                        break;
                    case R.id.rbMine:
                        vp_view.setCurrentItem(1, false);
                        mineFragment.onHiddenChanged(false);
                        break;
                }
            }
        });
    }

    public void getTokenDetail() {
        RetrofitNet.getInstance().getApi().getTokenMsg(Utils.getTokenMsg())
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String str = response.body().getData();
                        TokenBean data = GsonUtil.GsonToBean(str, TokenBean.class);
                        //保存员工信息
                        SpUtils.getInstance(MainActivity.this).save("jobNumber", data.getJobNumber());
                        SpUtils.getInstance(MainActivity.this).save("jobId", data.getId());
                        SpUtils.getInstance(MainActivity.this).save("autographUrl", data.getAutographUrl());
                        if (data.getAutographUrl().equals("默认") || data.getAutographUrl().isEmpty()) {
                            //去签名
                            T.show("请先签名");
                            startActivity(new Intent(MainActivity.this, DrawNameActivity.class));
                        }
                    }

                    @Override
                    public void onFail(String message) {

                    }

                });
    }

    private void initUI() {
        mList = new ArrayList<>();
        mList.add(mainFragment);
        mList.add(mineFragment);
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), mList);
        rbMain.setChecked(true);
        vp_view.setOffscreenPageLimit(3);
        vp_view.setAdapter(mAdapter);
        vp_view.setCurrentItem(0);
    }

    private long firstime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondtime = System.currentTimeMillis();
            if (secondtime - firstime > 3000) {
                T.show("再按一次返回键退出");
                firstime = System.currentTimeMillis();
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe
    public void refresh(RefreshMain refreshMain) {
        mainFragment.queryThisDayCheck("", 1);
    }

    @Subscribe
    public void refreshMine(MineRefresh mineRefresh) {
        mineFragment.getPadMessage();
        mainFragment.queryThisDayCheck("", 1);
    }

    @Subscribe
    public void refreshToken(RefresToken refresToken) {
        getTokenDetail();
    }

    @Subscribe
    public void mainFinish(MainFinish mainFinish) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    String codeNum = "";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e("single", "KeyEvent===" + event.toString());
            if (event.getKeyCode() != KeyEvent.KEYCODE_SHIFT_LEFT) {
                char singKey = (char) event.getUnicodeChar();
                codeNum += singKey;
                Log.e("single", "字符串===" + codeNum);
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            tv_code.setText(codeNum);
            //保存最终数据 清空临时变量
            codeNum = "";
            String codeStr = tv_code.getText().toString().trim();
            Log.e("total", "最终结果====" + codeStr);
            mainFragment.getGroupList(codeStr);
        }
        return super.dispatchKeyEvent(event);
    }

    private DownloadProgressDialog downloadProgressDialog;

    private void onClickUpdate(Context mContext, String url) {
        if (mContext != null) {
            PermissionX.init((FragmentActivity) mContext)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest()
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                            if (allGranted) {
                                if (null == downloadProgressDialog) {
                                    downloadProgressDialog = new DownloadProgressDialog(mContext);
                                }
                                downloadProgressDialog.show();
                                downloadProgressDialog.downLoad(url);
                            } else {
                                Toast.makeText(mContext, "这些权限被拒绝：" + deniedList.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}
