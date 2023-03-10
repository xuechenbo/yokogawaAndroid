package com.yokogawa.xc.ui.act;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;
import com.yokogawa.xc.base.BaseMvpActivity;
import com.yokogawa.xc.bean.GroupBean;
import com.yokogawa.xc.bean.PadBean;
import com.yokogawa.xc.event.MineRefresh;
import com.yokogawa.xc.ui.contract.PadManagerContract;
import com.yokogawa.xc.ui.presenter.ExaminationPresenter;
import com.yokogawa.xc.ui.presenter.PadManagerPresenter;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.ConnectionChangedListener;
import com.yokogawa.xc.utils.DeviceUtils;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.JsonUtils;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class PadManagerActivity extends BaseMvpActivity<PadManagerPresenter> implements PadManagerContract.View {
    @BindView(R.id.tv_padId)
    TextView tv_padId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_groupName)
    TextView tv_groupName;
    @BindView(R.id.tv_stationName)
    TextView tv_stationName;
    @BindView(R.id.bt_submit)
    Button bt_submi;
    private String groupId = "";
    private String stationId = "";
    private PadBean padBean = null;
    private BasePopupView showBottomDialog = null;
    private PadManagerPresenter padManagerPresenter;
    private String padId = "";
    private ConnectionChangedListener listener = new ConnectionChangedListener();

    @Override
    public int getLayoutId() {
        return R.layout.act_padmanager;
    }

    @Override
    public void initView() {
        tvTitle.setText("Pad??????");
        tv_padId.setText("?????????:" + DeviceUtils.getAndroidId(context));

        padId = SpUtils.getInstance(MyApplication.getInstance()).getString("padId", "");

        groupId = SpUtils.getInstance(MyApplication.getInstance()).getString("groupId", "");
        stationId = SpUtils.getInstance(MyApplication.getInstance()).getString("stationId", "");
        String groupName = SpUtils.getInstance(MyApplication.getInstance()).getString("groupName", "");
        String stationName = SpUtils.getInstance(MyApplication.getInstance()).getString("stationName", "");
        tv_groupName.setText(groupName);
        tv_stationName.setText(stationName);

        padManagerPresenter = new PadManagerPresenter(this);
        padManagerPresenter.attachView(this);
//        DeviceBandwidthSampler.getInstance().startSampling();
//        getNewRoot();
        padManagerPresenter.getPadMessage(DeviceUtils.getAndroidId(MyApplication.getInstance()));
//        DeviceBandwidthSampler.getInstance().stopSampling();

    }


    @OnClick({R.id.tv_back, R.id.bt_submit, R.id.tv_groupName, R.id.tv_stationName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_groupName:
                if (ClickUtils.isFastClick()) {
                    padManagerPresenter.getGroupList();
                }
                break;
            case R.id.tv_stationName:
                if (ClickUtils.isFastClick()) {
                    if (groupId.isEmpty()) {
                        T.show("?????????????????????");
                        return;
                    }
                    padManagerPresenter.getStationList(groupId);
                }
                break;
            case R.id.bt_submit:
                if (ClickUtils.isFastClick()) {
                    if (groupId.isEmpty() || stationId.isEmpty()) {
                        T.show("???????????????????????????");
                        return;
                    }
                    //????????? ?????? ????????????
                    if (padBean != null || bt_submi.getText().toString().equals("????????????")) {
                        updatePadMessage(padId);
                    } else {
                        addPadDevice();
                    }
                }
                break;
        }
    }

    //????????????
    void updatePadMessage(String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", id);
        paramsMap.put("groupId", groupId);//?????????ID
        paramsMap.put("stationId", stationId);//??????ID
        paramsMap.put("status", "0");//?????????0:???APP?????????1???APP?????????
        paramsMap.put("padNo", DeviceUtils.getAndroidId(context));//?????????
        paramsMap.put("updater", "0");//??????id
        paramsMap.put("deleteFlag", "0");
        padManagerPresenter.updatePadMessage(paramsMap);
    }

    //??????pad
    void addPadDevice() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("groupId", groupId);//?????????ID
        paramsMap.put("stationId", stationId);//??????ID
        paramsMap.put("status", "1");//?????????0:???APP?????????1???APP?????????
        paramsMap.put("padNo", DeviceUtils.getAndroidId(context));//?????????
        paramsMap.put("creater", "0");//??????id
        padManagerPresenter.addPadDevice(paramsMap);
    }


    //????????????
    @Override
    public void showBottomDialog(List<GroupBean> listArray, String[] arr, String id) {
        if (showBottomDialog != null && showBottomDialog.isShow()) {
            return;
        }
        if (Utils.isDestroy((Activity) PadManagerActivity.this)) {
            return;
        }
        showBottomDialog = new XPopup.Builder(context)
                .animationDuration(0)
                .maxHeight(650)
                .asBottomList("???????????????", arr, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Log.e("TAG", "onSelect=====" + text);
                        if (id.equals("1")) {
                            //?????????
                            tv_groupName.setText(listArray.get(position).getGroupName());
                            groupId = listArray.get(position).getId();
                            stationId = "";
                            tv_stationName.setText("");
                        } else {
                            tv_stationName.setText(listArray.get(position).getName());
                            stationId = listArray.get(position).getId();
                        }
                    }
                });
        showBottomDialog.show();
    }

    @Override
    public void addDeviceSuccess() {
        SpUtils.getInstance(MyApplication.getInstance()).save("groupId", groupId);
        SpUtils.getInstance(MyApplication.getInstance()).save("stationId", stationId);
        T.show("????????????");
        finish();
    }

    @Override
    public void updateDeviceSuccess() {
        SpUtils.getInstance(MyApplication.getInstance()).save("groupId", groupId);
        SpUtils.getInstance(MyApplication.getInstance()).save("stationId", stationId);
        T.show("????????????");
        finish();
    }


    @Override
    public void msgInfo(PadBean padBean) {
        if (Utils.isDestroy((Activity) PadManagerActivity.this)) {
            return;
        }
        this.padBean = padBean;
        padId = padBean.getId();
        tv_groupName.setText(padBean.getGroupName());
        tv_stationName.setText(padBean.getStationName());
        bt_submi.setText("????????????");
        if (padBean.getStatus().equals("0")) {
            tv_groupName.setClickable(true);
            tv_stationName.setClickable(true);
            bt_submi.setVisibility(View.VISIBLE);
        } else {
            tv_groupName.setClickable(false);
            tv_stationName.setClickable(false);
            bt_submi.setVisibility(View.GONE);
        }
    }


    @Override
    public void showButton(String message) {
        if (Utils.isDestroy((Activity) PadManagerActivity.this)) {
            return;
        }
        if (message.equals("???????????????")) {
            bt_submi.setVisibility(View.VISIBLE);
            bt_submi.setText("????????????");
            tv_groupName.setText("??????????????????");
            tv_stationName.setText("???????????????");
            SpUtils.getInstance(MyApplication.getInstance()).remove("groupName");
            SpUtils.getInstance(MyApplication.getInstance()).remove("stationName");
            SpUtils.getInstance(MyApplication.getInstance()).remove("groupId");
            SpUtils.getInstance(MyApplication.getInstance()).remove("stationId");
        } else {
//            T.show(message);
            String groupName = SpUtils.getInstance(MyApplication.getInstance()).getString("groupName", "");
            String stationName = SpUtils.getInstance(MyApplication.getInstance()).getString("stationName", "");
            tv_groupName.setText(groupName);
            tv_stationName.setText(stationName);
        }

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MineRefresh());
    }


    private BasePopupView popupView;

    //??????????????????
    public void showLoadingDialog() {
        popupView = new XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asLoading("???????????????")
                .show();
    }

    public void dissLoadingDialog() {
        if (popupView != null && popupView.isShow()) {
            popupView.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ConnectionClassManager.getInstance().remove(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ConnectionClassManager.getInstance().register(listener);
    }

    void getNewRoot() {
        //ConnectionQuality
        ConnectionQuality currentBandwidthQuality = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        String name = currentBandwidthQuality.name();
        if (name.equals("POOR")) {
            Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
        }
        Log.e("TAG", "getNewRoot====" + name);
        //POOR  150??????
        //MODERATE  150 - 550
        //GOOD   550 - 2000
        //EXCELLENT  2000
        //UNKNOWN
    }
}
