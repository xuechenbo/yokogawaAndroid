package com.yokogawa.xc.ui.contract;

import com.yokogawa.xc.base.BaseView;
import com.yokogawa.xc.bean.ExamDetailsBean;
import com.yokogawa.xc.demo.bean.ExamiBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;

public interface ExaminationContract {

    interface View extends BaseView {
        void queryWorkRecordSucess(ExamDetailsBean examDetailsBean);

        void subMitMsgSuccess();
        void finishProgress_Success();

        void staffScheduleSuccess(String id);
    }

    interface Presenter {
        void queryWorkRecord(String id, String groupId);

        void subMitMsg(RequestBody requestBody, boolean isPause);

        void finishProgress_Second(RequestBody requestBody);

        void staffSchedule(RequestBody requestBody);
    }

    interface Model {
        String log();
    }
}
