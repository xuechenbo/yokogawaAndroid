package com.yokogawa.xc.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseMvpActivity;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.ui.contract.ExaminationContract;
import com.yokogawa.xc.ui.presenter.ExaminationPresenter;
import com.yokogawa.xc.adapter.MainAdapter;
import com.yokogawa.xc.bean.RefersBookManage;
import com.yokogawa.xc.bean.RefresBean;
import com.yokogawa.xc.dialog.ShowResumeMsgDialog;
import com.yokogawa.xc.dialog.ShowWorkRecordDialog;
import com.yokogawa.xc.dialog.SubmitShowNgDialog;
import com.yokogawa.xc.event.RefreshMain;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.DateUtil;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.KvUtils;
import com.yokogawa.xc.utils.RuleListUtils;
import com.yokogawa.xc.utils.SoftKeyBoardListener;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.TeRuleListUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.netUtils;
import com.yokogawa.xc.view.CustomPopup;
import com.yokogawa.xc.view.ItemNbDecoration;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ExaminationFromActivity extends BaseMvpActivity<ExaminationPresenter> implements ExaminationContract.View {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.tv_workRecord)
    TextView tv_workRecord;
    @BindView(R.id.tv_xm)
    TextView tv_xm;
    @BindView(R.id.tv_jf)
    TextView tv_jf;
    @BindView(R.id.tv_lf)
    TextView tv_lf;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.lin_Specification)
    LinearLayout lin_Specification;

    @BindView(R.id.lin_lf)
    LinearLayout lin_lf;
    @BindView(R.id.lin_jf)
    LinearLayout lin_jf;
    @BindView(R.id.lin_zh)
    LinearLayout lin_zh;
    @BindView(R.id.lin_xm)
    LinearLayout lin_xm;
    @BindView(R.id.lin_RemarkBench)
    LinearLayout lin_RemarkBench;
    @BindView(R.id.lin_sprule)
    LinearLayout lin_sprule;

    private MainAdapter mainAdapter;
    private ExaminationPresenter mainExaminationPresenter;
    private ArrayList<NewExamBean.Project> list;
    private long startTime;
    private String templateId;
    private String typeStatus;//????????????
    private String id = "";  //?????????id
    private int jobId;  //??????
    private String groupId;
    private String isLast;  //????????????????????????
    private NewExamBean newExamBean;
    private String level;
    private String masterUrl = "";
    private String modeName = "";
    private String seriesNumber = "";
    private RefersBookManage refersBookManage;
    private RefresBean refresBean;
    private ShowWorkRecordDialog showWorkRecordDialog;
    private String calculateNumber = "";
    private String linkage = "";
    private ShowResumeMsgDialog showResumeMsgDialog;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        level = SpUtils.getInstance(MyApplication.getInstance()).getString("level", "");
        groupId = SpUtils.getInstance(MyApplication.getInstance()).getString("groupId", "");
        jobId = SpUtils.getInstance(MyApplication.getInstance()).getInt("jobId", 0);
        startTime = System.currentTimeMillis();
        //?????? ????????????????????????
        seriesNumber = getIntent().getStringExtra("seriesNumber");    //??????
        calculateNumber = getIntent().getStringExtra("calculateNumber");//??????
        //??????????????????????????????
        linkage = getIntent().getStringExtra("linkage");

        tv_lf.setText(seriesNumber);
        modeName = getIntent().getStringExtra("modeName");
        tv_xm.setText(modeName);
        tv_jf.setText(calculateNumber);
        tv_title.setText(getIntent().getStringExtra("title"));
        tv_right.setVisibility(View.VISIBLE);
        //??????==1  ????????????===2  ???????????????===3
        typeStatus = getIntent().getStringExtra("typeStatus");
        id = getIntent().getStringExtra("id");  //?????????id
        if (typeStatus.equals("3")) {
            //??????????????????
            line.setVisibility(View.GONE);
        } else {
            //???????????? ?????????1,??????0
            isLast = getIntent().getStringExtra("isLast");  //???????????????????????????
            submit.setText(isLast.equals("1") ? "????????????" : "??????");
            templateId = getIntent().getStringExtra("templateId");//??????id
            tv_workRecord.setVisibility(View.VISIBLE);
        }
        recyclerView.addItemDecoration(new ItemNbDecoration(context, 0, 5, true));
        initData();
    }

    private void initData() {
        mainExaminationPresenter = new ExaminationPresenter(this);
        mainExaminationPresenter.attachView(this);
        //?????????  ???????????????  ????????????????????????????????????  ?????????  ??????
        mainAdapter = new MainAdapter(list, !typeStatus.equals("3"), false, context, modeName);
        recyclerView.setAdapter(mainAdapter);
        //????????????
        lookMsg();
        //???????????????????????????
        SoftKeyBoardListener.setListener(ExaminationFromActivity.this, new SoftKeyBoardListener.OnSoftKeyboardChangeListener() {
            @Override
            public void keyBoardShow() {

            }

            @Override
            public void keyBoardHide() {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    if (currentFocus instanceof EditText) {
                        currentFocus.clearFocus();
                    }
                }
            }
        });
    }

    private void lookMsg() {
        refersBookManage = (RefersBookManage) getIntent().getSerializableExtra("RefresBean");
        if (refersBookManage != null) {
            String refersBooksData = refersBookManage.getRefersBooksData();
            refresBean = new Gson().fromJson(refersBooksData, RefresBean.class);
            Log.e("???????????????===", refresBean.toString());
        }
        String examitContent = getIntent().getStringExtra("checkContentJson");
        String temporaryStr = KvUtils.getString(jobId + seriesNumber + Utils.getStationId());
        if (typeStatus.equals("3") && !Utils.getStrIs(examitContent)) {
            newExamBean = GsonUtil.GsonToBean(examitContent, NewExamBean.class);
        } else if (!temporaryStr.isEmpty()) {
            newExamBean = GsonUtil.GsonToBean(temporaryStr, NewExamBean.class);
        } else {
            if (!Utils.getStrIs(examitContent)) {
                //???????????? ??????????????????
                newExamBean = GsonUtil.GsonToBean(examitContent, NewExamBean.class);
            }
        }
        //???????????????
        if (Utils.getStrIs(examitContent)) {
            return;
        }
        list.addAll(newExamBean.getProject());
        //??????????????????
        initHeadVisible(newExamBean.getColumnIsShow());
        mainAdapter.setIsVisible(newExamBean.getColumnIsShow());
        //TODO ??????????????????
        if (typeStatus.equals("3")) {
            return;
        }
        getRuleMsg();
    }

    /**
     * ???????????? ???????????????????????????
     */
    private void getRuleMsg() {
        try {
            ArrayList<NewExamBean.Project> ruleMsg = RuleListUtils.getRuleMsg(list, refresBean, modeName);
//            ArrayList<NewExamBean.Project> ruleMsg = TeRuleListUtils.getRuleMsg(list, refresBean, modeName);
            Log.e("TAG", "??????mList===" + ruleMsg);
            mainAdapter.setNewData(ruleMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initHeadVisible(List<Boolean> columnIsShow) {
        Boolean aBoolean = columnIsShow.get(3);
        Boolean aBoolean1 = columnIsShow.get(5);
        if (aBoolean || aBoolean1) {
            lin_sprule.setVisibility(View.VISIBLE);
        }
        if (aBoolean) {
            //????????????
            lin_Specification.setVisibility(View.VISIBLE);
        }
        if (aBoolean1) {
            lin_RemarkBench.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.submit, R.id.tv_back, R.id.tv_right, R.id.tv_workRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                //????????????
                if (ClickUtils.isFastClick()) {
                    if (showResumeMsgDialog != null && showResumeMsgDialog.getDialog() != null && showResumeMsgDialog.getDialog().isShowing()) {
                        Log.e("TAG", "showResumeMsgDialog===showing");
                    } else {
                        showResumeMsgDialog = ShowResumeMsgDialog.getInstance(id);
                        showResumeMsgDialog.show(getSupportFragmentManager(), "");
                    }
                }
                break;
            case R.id.tv_back:
                if (ClickUtils.isFastClick()) {
                    showSaveDialog();
                }
                break;
            case R.id.submit:
                if (ClickUtils.isFastClick1()) {
                    isFinishSubmit(isLast.equals("1"));
                }
                break;
            case R.id.tv_workRecord:
                //???????????????????????????
                mainExaminationPresenter.queryWorkRecord(id, groupId);
                break;
        }
    }

    //TODO

    /**
     * 1?????????????????????ng????????????ng????????????????????????????????????
     * 2?????????ng????????????????????????????????????
     * 3???level ?????????????????????????????????????????????????????????==0???????????????????????????
     * 4????????????????????????ng??????ng?????????????????????
     *
     * @param isLast ???????????????????????????
     */
    void isFinishSubmit(boolean isLast) {
        List<NewExamBean.Project> data = mainAdapter.getData();
        if (newExamBean == null) {
            return;
        }
        newExamBean.setProject(data);
        Log.e("TAG", "?????????===" + newExamBean.toString());
        String ngMsg = Utils.getNgMsg(newExamBean);
        if (ngMsg.equals("NG")) {
            showDialog(isLast, newExamBean);
            return;
        }
        //????????????????????????
        if (Utils.isFinish(newExamBean)) {
            if (ngMsg.equals("NG")) {
                showDialog(isLast, newExamBean);
            } else {
                submit(isLast, newExamBean, "OK");
//                if (level.equals("0")) {
//                    showMasterDialog(isLast, newExamBean, "OK");
//                } else {
//                    submit(isLast, newExamBean, "OK");
//                }
            }
        } else {
            T.show("??????????????????");
        }
    }

    void submit(boolean isLast, NewExamBean newExamBean, String ngMsg) {
        showLoadingDialog();
        if (isLast) {//????????????????????????
            finishProgress(newExamBean, ngMsg);
        } else {
            mainExaminationPresenter.subMitMsg(IntegrationMsg(newExamBean, true, ngMsg), true);
        }
    }

    //????????????????????????
    @Override
    public void queryWorkRecordSucess(ExamDetailsBean examDetailsBean) {
        if (showWorkRecordDialog != null && showWorkRecordDialog.getDialog() != null && showWorkRecordDialog.getDialog().isShowing()) {
            Log.e("TAG", "ShowWorkRecordDialog===showing");
        } else {
            showWorkRecordDialog = ShowWorkRecordDialog.getInstance(examDetailsBean, typeStatus);
            showWorkRecordDialog.show(getSupportFragmentManager(), "");
        }
    }

    //??????
    @Override
    public void subMitMsgSuccess() {
        try {
            dissLoadingDialog();
            T.show("????????????");
            KvUtils.removeKey(jobId + seriesNumber + Utils.getStationId());
            finish();
        } catch (Exception e) {
            netUtils.addErrorLog("subMitMsgSuccess", "catch");
        } finally {
            finish();
        }
    }

    //????????????
    @Override
    public void finishProgress_Success() {
        try {
            dissLoadingDialog();
            T.show("????????????");
            KvUtils.removeKey(jobId + seriesNumber + Utils.getStationId());
            finish();
        } catch (Exception e) {
            netUtils.addErrorLog("finishProgress_Success", "catch");
        } finally {
            finish();
        }
    }


    //????????????????????????????????? ??????
    @Override
    public void staffScheduleSuccess(String id) {
        this.id = id;
    }


    //TODO NG??????
    void showDialog(boolean isLast, NewExamBean newExamBean) {
        Log.e("TAG", "showDialog====NG??????");
        SubmitShowNgDialog instance = SubmitShowNgDialog.getInstance();
        instance.setOnSubmitListener(new SubmitShowNgDialog.OnSubmitListener() {
            @Override
            public void onsubmitClick() {
//                if (level.equals("0")) {
//                    //TODO ????????????  ??????????????????
//                    showMasterDialog(isLast, newExamBean, "NG");
//                } else {
//                    submit(isLast, newExamBean, "NG");
//                }
                submit(isLast, newExamBean, "NG");
                instance.dismiss();
            }
        });
        instance.show(getSupportFragmentManager(), "");
    }

    //???????????????
    public void showMasterDialog(boolean isLast, NewExamBean newExamBean, String ng) {
        CustomPopup customPopup = new CustomPopup(context, "2");
        customPopup.setOnSubmitListener(new CustomPopup.OnSubmitListener() {
            @Override
            public void onsubmitClick(String trim) {
                Log.e("TAG", "????????????===" + trim);
                //??????????????????
                masterUrl = trim;
                //???????????? ?????????
                submit(isLast, newExamBean, ng);
            }
        });
        new XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(customPopup)
                .show();
    }

    //?????????????????????json
    void finishProgress(NewExamBean newExamBean, String type) {
        RetrofitNet.getInstance().getApi().queryWorkRecord(Utils.getTokenMsg(), id, groupId, "0")
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String string = response.body().getData();
                        RequestBody merge = merge(string, newExamBean, type);
                        mainExaminationPresenter.finishProgress_Second(merge);
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }
                });
    }


    //????????????????????????
    private RequestBody merge(String string, NewExamBean examiBean, String type) {
        ArrayList<NewExamBean> list = new ArrayList<>();
        ExamDetailsBean examDetailsBean = GsonUtil.GsonToBean(string, ExamDetailsBean.class);
        List<ExamDetailsBean.ChildList> mList = examDetailsBean.getList();
        //????????????
        for (int i = 0; i < mList.size(); i++) {
            String checkContentJson = mList.get(i).getCheckContentJson();
            Log.e("TAG", "merge===" + checkContentJson);
            NewExamBean newExamBean = GsonUtil.GsonToBean(checkContentJson, NewExamBean.class);
            list.add(newExamBean);
        }
        list.add(examiBean);
        List<HashMap<String, String>> mHashList = new ArrayList<>();
        //TODO ??????????????????,??????
        examiBean.setNgStatus(type);
        examiBean.setMasterUrl(masterUrl);
        examiBean.setCreateName(SpUtils.getInstance(context).getString("autographUrl", ""));
        examiBean.setCreateDate(DateUtil.getMillon1(startTime));
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("progressId", id);  //?????????id
        paramsMap.put("checkContentJson", new Gson().toJson(list));//	?????????????????????
        paramsMap.put("progressCheckContentJson", new Gson().toJson(examiBean));//	?????????????????????
        //????????????
        paramsMap.put("result", type);  //	??????NG/OK
        paramsMap.put("startTime", DateUtil.getMillon1(startTime));//????????????
        paramsMap.put("endTime", DateUtil.getMillon1(System.currentTimeMillis()));//????????????
        paramsMap.put("status", "0");  //???????????????
        paramsMap.put("groupId", getGroupId());
        paramsMap.put("stationId", getStationId());
        mHashList.add(paramsMap);
        RequestBody requestBody = Utils.getRequestBody(mHashList);
        return requestBody;
    }

    //????????????
    private RequestBody IntegrationMsg(NewExamBean examiBean, boolean isPause, String type) {
        List<HashMap<String, String>> mHashList = new ArrayList<>();
        //TODO ??????????????????,??????
        examiBean.setNgStatus(type);
        examiBean.setMasterUrl(masterUrl);
        examiBean.setCreateName(SpUtils.getInstance(context).getString("autographUrl", ""));
        examiBean.setCreateDate(DateUtil.getMillon1(startTime));
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", id);  //?????????id
        paramsMap.put("checkContentJson", isPause ? new Gson().toJson(examiBean) : "");//	???????????????json
        //????????????
        paramsMap.put("result", type);
        paramsMap.put("startTime", DateUtil.getMillon1(startTime));//????????????
        paramsMap.put("endTime", DateUtil.getMillon1(System.currentTimeMillis()));//????????????
        paramsMap.put("status", "0");  //???????????????
        paramsMap.put("groupId", getGroupId());
        paramsMap.put("stationId", getStationId());
        mHashList.add(paramsMap);
        RequestBody requestBody = Utils.getRequestBody(mHashList);
        return requestBody;
    }


    @Override
    public void showLoading() {
        popupView = new XPopup.Builder(ExaminationFromActivity.this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asLoading("?????????...")
                .show();
    }

    @Override
    public void hideLoading() {
        if (popupView != null && popupView.isShow()) {
            popupView.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showWorkRecordDialog != null) {
            showWorkRecordDialog.onDestroy();
        }
        if (showResumeMsgDialog != null) {
            showResumeMsgDialog.onDestroy();
        }
        EventBus.getDefault().post(new RefreshMain());
    }

    void showSaveDialog() {
        if (typeStatus.equals("3")) {
            finish();
            return;
        }
        new AlertDialog.Builder(ExaminationFromActivity.this)
                .setTitle("??????")
                .setMessage("??????????????????????????????????????????????????????????????????????????????")
                .setPositiveButton("??????", (dialog, which) -> {
                    List<NewExamBean.Project> data = mainAdapter.getData();
                    newExamBean.setProject(data);
                    KvUtils.encode(jobId + seriesNumber + Utils.getStationId(), new Gson().toJson(newExamBean));
                    netUtils.submiEmpty(IntegrationMsg(new NewExamBean(), false, "OK"));
                    finish();
                })
                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KvUtils.removeKey(jobId + seriesNumber + Utils.getStationId());
                        netUtils.submiEmpty(IntegrationMsg(new NewExamBean(), false, "OK"));
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSaveDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private BasePopupView popupView;

    //??????????????????
    public void showLoadingDialog() {
//        if (Utils.isDestroy((Activity) ExaminationFromActivity.this)) {
//            return;
//        }
        popupView = new XPopup.Builder(ExaminationFromActivity.this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asLoading("?????????...")
                .show();
    }

    public void dissLoadingDialog() {
        if (popupView != null && popupView.isShow()) {
            popupView.dismiss();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("TAG", "onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        final long start = System.currentTimeMillis();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.d("TAG", "displaye cost:" + (System.currentTimeMillis() - start));
                return false;
            }
        });
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
