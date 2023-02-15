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
        tvTitle.setText("工作台");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("扫码异常");
        tv_workRecord.setVisibility(View.VISIBLE);
        tv_workRecord.setText("指图书");
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
                if (autographUrl.equals("默认") || autographUrl.isEmpty()) {
                    T.show("请先签名");
                    startActivity(new Intent(getActivity(), DrawNameActivity.class));
                    return;
                }
                int status = bean.getStatus();
                if (result.equals("NG")) {
                    T.show("此检查表已NG,无法进行操作！");
                    return;
                }
                if (status == 1) {
                    //待作业   员工检查item
                    showLoadingDialog();
                    checkCurrentItem(String.valueOf(bean.getId()));
                } else if (status == 0) {
                    //作业中   去查谁在作业中
                    showLoadingDialog();
                    queryWorkerStaff(String.valueOf(bean.getId()));
                }
            }
        });
        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
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
                //扫码异常
                showErrorDialog();
            case R.id.tv_updateRule:
                if (ClickUtils.isFastClick()) {
                    updateRule();
                }
                break;
        }
    }


    //获取当天生成的检查表
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


    //输入指图书条码  获取指图书信息
    void showErrorDialog1() {
        ErrorCodePopup1 customPopup = new ErrorCodePopup1(getActivity());
        customPopup.setOnSubmitListener((data, barCode) -> {
            RefresBean refresBean = RefresUtils.toBean(data);
            //TODO 提交指图书信息
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
     * @param barCode 条码
     */
    public void getGroupList(String barCode) {
        String autographUrl = SpUtils.getInstance(getActivity()).getString("autographUrl", "");
        if (autographUrl.equals("默认") || autographUrl.isEmpty()) {
            T.show("请先签名");
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
                        //TODO 提交指图书信息
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


    //提交指图书信息
    void submitBookMsg(RefresBean refresBean, String str, String barCode) {
        String jsonStr = new Gson().toJson(refresBean);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("model", refresBean.getMS_CODE());
        paramsMap.put("refersBooksContent", str);
        paramsMap.put("refersBooksData", jsonStr);
        paramsMap.put("seriesNumber", refresBean.getSERIAL_NO());//计番
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


    //获取员工工作列表
    protected void ScanCode(RefresBean refresBean) {
        String modeName = refresBean.getMS_CODE();//型名
        String calculateNumber = refresBean.getSERIAL_NO();//计番
        String start_no = refresBean.getSTART_NO();//连番
        String linkage = refresBean.getLinkage();
        if (Utils.getGroupId().isEmpty() || Utils.getStationId().isEmpty()) {
            T.show("请检查是否绑定工位~");
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

    //TODO 扫码异常
    void showErrorDialog() {
        ErrorCodePopup customPopup = new ErrorCodePopup(getActivity());
        customPopup.setOnSubmitListener((data, modeName, calculateNumber, seriesNumber, linkage) -> {
            //TODO 保存型名
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
     * @param typeStatus      扫码1   列表点击2
     */
    void startTestDemo(CheduleBean cheduleBean, String typeStatus) {
        dissLoadingDialog();
        Intent intent = new Intent(getActivity(), ExaminationFromActivity.class);
        intent.putExtra("typeStatus", typeStatus);
        intent.putExtra("title", cheduleBean.getTemplateName());
        intent.putExtra("checkContentJson", cheduleBean.getCheckContentJson());
        intent.putExtra("calculateNumber", cheduleBean.getCheckTableProgress().getCalculateNumber());//计番
        intent.putExtra("seriesNumber", cheduleBean.getCheckTableProgress().getSeriesNumber());//连番
        intent.putExtra("modeName", cheduleBean.getCheckTableProgress().getModeName());//型名
        intent.putExtra("id", cheduleBean.getCheckTableProgress().getId() + "");//检查表id
        intent.putExtra("linkage", cheduleBean.getCheckTableProgress().getLinkage() + "");//linkage
        intent.putExtra("projectId", cheduleBean.getProjectId() + "");  //项目id
        intent.putExtra("templateId", cheduleBean.getTemplateId() + ""); //模板id
        intent.putExtra("isLast", cheduleBean.getIsLast());
        intent.putExtra("RefresBean", cheduleBean.getRefersBookManage());
        startActivity(intent);
    }

    //查询正在作业中的员工
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
                                //本人作业中,继续工作
                                checkCurrentItem(id);
                            } else {
                                T.show(staffName + "正在作业中");
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

    //员工项目检查item
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


    //提交加载进度
    public void showLoadingDialog() {
        popupView = new XPopup.Builder(getActivity())
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asLoading("正在加载中")
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

    //更新工位规则
    public void updateRule() {
        RetrofitNet.getInstance().getApi().queryCheckRuleMap(Utils.getTokenMsg(), Utils.getGroupId(), Utils.getStationId())
                .enqueue(new ResultCallback<HttpResult<String>>() {
            @Override
            public void onSuccess(Response<HttpResult<String>> response) {
                String data = response.body().getData();
                SpUtils.getInstance(MyApplication.getInstance()).save("ruleMap", data);
                T.show("更新成功");
            }

            @Override
            public void onFail(String message) {
                T.show(message);
            }
        });
    }
}