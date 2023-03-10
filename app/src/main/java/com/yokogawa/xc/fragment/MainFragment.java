package com.yokogawa.xc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.adapter.MainFrgAdapter;
import com.yokogawa.xc.bean.CheduleBean;
import com.yokogawa.xc.bean.CurrListBean;
import com.yokogawa.xc.bean.RefresBean;
import com.yokogawa.xc.ui.act.ExaminationFromActivity;
import com.yokogawa.xc.utils.ClickUtils;
import com.yokogawa.xc.utils.DateUtil;
import com.yokogawa.xc.utils.GsonUtil;
import com.yokogawa.xc.utils.MethodInputUtil;
import com.yokogawa.xc.utils.RefresUtils;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.ui.act.DrawNameActivity;
import com.yokogawa.xc.view.ErrorCodePopup;
import com.yokogawa.xc.view.ErrorCodePopup1;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import retrofit2.Response;

public class MainFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_back)
    RelativeLayout tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sartRefresh)
    SmartRefreshLayout sartRefresh;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_workRecord)
    TextView tv_workRecord;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_updateRule)
    TextView tv_updateRule;
    private int mPage = 1;
    private MainFrgAdapter mainFrgAdapter;
    Unbinder unbinder;
    private List<CurrListBean.ChildList> mList = new ArrayList<>();
    private BasePopupView popupView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main, container, false);
        unbinder = ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tvTitle.setText("?????????");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("????????????");
        tv_workRecord.setVisibility(View.VISIBLE);
        tv_workRecord.setText("?????????");
        tv_updateRule.setVisibility(View.VISIBLE);
        tvBack.setVisibility(View.GONE);
        mainFrgAdapter = new MainFrgAdapter(R.layout.frg_main_item, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainFrgAdapter.bindToRecyclerView(recyclerView);
        mainFrgAdapter.setEmptyView(R.layout.empty_item, recyclerView);
        initListener();
        queryThisDayCheck("", mPage);
    }

    private void initListener() {
        sartRefresh.setOnRefreshListener(refreshLayout -> {
            etSearch.clearFocus();
            clearPage();
            queryThisDayCheck(etSearch.getText().toString().trim(), mPage);
        });
        sartRefresh.setOnLoadMoreListener(refreshLayout -> {
            mPage++;
            queryThisDayCheck(etSearch.getText().toString().trim(), mPage);
        });
        mainFrgAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (ClickUtils.isFastClick()) {
                CurrListBean.ChildList bean = (CurrListBean.ChildList) adapter.getData().get(position);
                String result = bean.getResult();
                String autographUrl = SpUtils.getInstance(getActivity()).getString("autographUrl", "");
                if (autographUrl.equals("??????") || autographUrl.isEmpty()) {
                    T.show("????????????");
                    startActivity(new Intent(getActivity(), DrawNameActivity.class));
                    return;
                }
                int status = bean.getStatus();
                if (result.equals("NG")) {
                    T.show("???????????????NG,?????????????????????");
                    return;
                }
                if (status == 1) {
                    //?????????   ????????????item
                    showLoadingDialog();
                    checkCurrentItem(String.valueOf(bean.getId()));
                } else if (status == 0) {
                    //?????????   ?????????????????????
                    showLoadingDialog();
                    queryWorkerStaff(String.valueOf(bean.getId()));
                }
            }
        });
        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {//????????????action
                MethodInputUtil.hideSoftInput(getActivity(), etSearch);
                String content = etSearch.getText().toString();
                clearPage();
                queryThisDayCheck(content, mPage);
                etSearch.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void clearPage() {
        mPage = 1;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_right, R.id.tv_workRecord, R.id.tv_updateRule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                etSearch.clearFocus();
                String content = etSearch.getText().toString();
                if (Utils.notNullOrEmpty(content)) {
                    etSearch.getText().clear();
                    clearPage();
                    queryThisDayCheck("", mPage);
                } else {
                    MethodInputUtil.hideSoftInput(getActivity(), etSearch);
                }
                break;
            case R.id.tv_workRecord:
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                showErrorDialog1();
                break;
            case R.id.tv_right:
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                //????????????
                showErrorDialog();
            case R.id.tv_updateRule:
                if (ClickUtils.isFastClick()) {
                    updateRule();
                }
                break;
        }
    }


    //??????????????????????????????
    public void queryThisDayCheck(String checkName, int page) {
        if (sartRefresh != null) {
            sartRefresh.finishRefresh();
            sartRefresh.finishLoadMore();
        }
        if (Utils.getStationId().isEmpty()) {
            return;
        }
        RetrofitNet.getInstance().getApi().queryThisDayCheck(Utils.getTokenMsg(), checkName, Utils.getGroupId(), Utils.getStationId(), String.valueOf(page), "15")
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        CurrListBean currListBean = GsonUtil.GsonToBean(data, CurrListBean.class);
                        if (page == 1) {
                            mPage = 1;
                            mList = currListBean.getList();
                        } else {
                            mList.addAll(currListBean.getList());
                        }
                        mainFrgAdapter.setNewData(mList);
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }
                });
    }


    //?????????????????????  ?????????????????????
    void showErrorDialog1() {
        ErrorCodePopup1 customPopup = new ErrorCodePopup1(getActivity());
        customPopup.setOnSubmitListener((data, barCode) -> {
            RefresBean refresBean = RefresUtils.toBean(data);
            //TODO ?????????????????????
            submitBookMsg(refresBean, data, barCode);
            ScanCode(refresBean);
        });
        new XPopup.Builder(getActivity())
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(customPopup)
                .show();
    }

    /**
     * @param barCode ??????
     */
    public void getGroupList(String barCode) {
        String autographUrl = SpUtils.getInstance(getActivity()).getString("autographUrl", "");
        if (autographUrl.equals("??????") || autographUrl.isEmpty()) {
            T.show("????????????");
            startActivity(new Intent(getActivity(), DrawNameActivity.class));
            return;
        }
        showLoadingDialog();
        RetrofitNet.getInstance().getApi().queryWebService(barCode)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        RefresBean refresBean = RefresUtils.toBean(data);
                        //TODO ?????????????????????
                        submitBookMsg(refresBean, data, barCode);
                        ScanCode(refresBean);
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }
                });
    }


    //?????????????????????
    void submitBookMsg(RefresBean refresBean, String str, String barCode) {
        String jsonStr = new Gson().toJson(refresBean);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("model", refresBean.getMS_CODE());
        paramsMap.put("refersBooksContent", str);
        paramsMap.put("refersBooksData", jsonStr);
        paramsMap.put("seriesNumber", refresBean.getSERIAL_NO());//??????
        paramsMap.put("barCode", barCode);
        RetrofitNet.getInstance().getApi().addRefersBook(Utils.getRequestBody(paramsMap))
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
    }


    //????????????????????????
    protected void ScanCode(RefresBean refresBean) {
        String modeName = refresBean.getMS_CODE();//??????
        String calculateNumber = refresBean.getSERIAL_NO();//??????
        String start_no = refresBean.getSTART_NO();//??????
        String linkage = refresBean.getLinkage();
        if (Utils.getGroupId().isEmpty() || Utils.getStationId().isEmpty()) {
            T.show("???????????????????????????~");
            return;
        }
        RetrofitNet.getInstance().getApi().insertStaffSchedule(Utils.getTokenMsg(), modeName, calculateNumber, Utils.getGroupId(), Utils.getStationId(), start_no, linkage)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        CheduleBean cheduleBean = GsonUtil.GsonToBean(data, CheduleBean.class);
                        startTestDemo(cheduleBean, "1");
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }
                });
    }

    //TODO ????????????
    void showErrorDialog() {
        ErrorCodePopup customPopup = new ErrorCodePopup(getActivity());
        customPopup.setOnSubmitListener((data, modeName, calculateNumber, seriesNumber, linkage) -> {
            //TODO ????????????
            CheduleBean cheduleBean = GsonUtil.GsonToBean(data, CheduleBean.class);
            startTestDemo(cheduleBean, "1");
        });
        new XPopup.Builder(getActivity())
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(customPopup)
                .show();
    }


    /**
     * @param cheduleBean
     * @param typeStatus      ??????1   ????????????2
     */
    void startTestDemo(CheduleBean cheduleBean, String typeStatus) {
        dissLoadingDialog();
        Intent intent = new Intent(getActivity(), ExaminationFromActivity.class);
        intent.putExtra("typeStatus", typeStatus);
        intent.putExtra("title", cheduleBean.getTemplateName());
        intent.putExtra("checkContentJson", cheduleBean.getCheckContentJson());
        intent.putExtra("calculateNumber", cheduleBean.getCheckTableProgress().getCalculateNumber());//??????
        intent.putExtra("seriesNumber", cheduleBean.getCheckTableProgress().getSeriesNumber());//??????
        intent.putExtra("modeName", cheduleBean.getCheckTableProgress().getModeName());//??????
        intent.putExtra("id", cheduleBean.getCheckTableProgress().getId() + "");//?????????id
        intent.putExtra("linkage", cheduleBean.getCheckTableProgress().getLinkage() + "");//linkage
        intent.putExtra("projectId", cheduleBean.getProjectId() + "");  //??????id
        intent.putExtra("templateId", cheduleBean.getTemplateId() + ""); //??????id
        intent.putExtra("isLast", cheduleBean.getIsLast());
        intent.putExtra("RefresBean", cheduleBean.getRefersBookManage());
        startActivity(intent);
    }

    //??????????????????????????????
    private void queryWorkerStaff(String id) {
        RetrofitNet.getInstance().getApi().queryWorkerStaff(Utils.getTokenMsg(), "0", id)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        try {
                            String data = response.body().getData();
                            JSONObject jsonObject = new JSONObject(data);
                            String staffName = jsonObject.getString("staffName");
                            String jobNumber = jsonObject.getString("jobNumber");
                            String stationId = jsonObject.getString("stationId");
                            String jobNumber1 = SpUtils.getInstance(getActivity()).getString("jobNumber", "");
                            if (jobNumber1.equals(jobNumber) && Utils.getStationId().equals(stationId)) {
                                //???????????????,????????????
                                checkCurrentItem(id);
                            } else {
                                T.show(staffName + "???????????????");
                            }
                            dissLoadingDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }

                });
    }

    //??????????????????item
    private void checkCurrentItem(String id) {
        RetrofitNet.getInstance().getApi().staffHomeworkExamination(Utils.getTokenMsg(), id, Utils.getGroupId(), Utils.getStationId())
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        String data = response.body().getData();
                        CheduleBean cheduleBean = GsonUtil.GsonToBean(data, CheduleBean.class);
                        startTestDemo(cheduleBean, "2");
                    }

                    @Override
                    public void onFail(String message) {
                        dissLoadingDialog();
                        T.show(message);
                    }
                });
    }


    //??????????????????
    public void showLoadingDialog() {
        popupView = new XPopup.Builder(getActivity())
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //??????????????????
    public void updateRule() {
        RetrofitNet.getInstance().getApi().queryCheckRuleMap(Utils.getTokenMsg(), Utils.getGroupId(), Utils.getStationId())
                .enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();
                SpUtils.getInstance(MyApplication.getInstance()).save("ruleMap", data);
                T.show("????????????");
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });
    }
}