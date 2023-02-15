package com.yokogawa.xc.ui.contract;

import com.yokogawa.xc.base.BaseView;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.bean.GroupBean;
import com.yokogawa.xc.bean.PadBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public interface PadManagerContract {

    interface View extends BaseView {
        void msgInfo(PadBean padBean);

        void showButton(String message);

        void showBottomDialog(List<GroupBean> listArray,String[] arr, String id);

        void addDeviceSuccess();

        void updateDeviceSuccess();
    }

    interface Presenter {
        void getPadMessage(String id);

        void getGroupList();

        void getStationList(String groupId);

        void updatePadMessage(HashMap paramsMap);

        void addPadDevice(HashMap paramsMap);
    }
}
