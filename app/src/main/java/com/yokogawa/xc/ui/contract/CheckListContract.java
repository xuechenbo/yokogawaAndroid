package com.yokogawa.xc.ui.contract;


import com.yokogawa.xc.base.BaseView;
import com.yokogawa.xc.bean.CheckItemBean;
import com.yokogawa.xc.bean.LoginBean;

import java.util.HashMap;
import java.util.List;

public interface CheckListContract {

    interface View extends BaseView {
        void success(List<CheckItemBean.ChildLiST> list,int status);
        void fail();
    }

    interface Presenter {
        void getCheckList(int page);
        void refresh(int page);
        void loadMore(int page);
    }

    interface Model {
        String log();
    }
}
