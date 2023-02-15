package com.yokogawa.xc.demo.act;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rxjava.rxlife.RxLife;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.demo.bean.AAAA;
import com.yokogawa.xc.demo.bean.Book;
import com.yokogawa.xc.demo.bean.ClassB;
import com.yokogawa.xc.demo.bean.CombinaBean;
import com.yokogawa.xc.demo.bean.Combination;
import com.yokogawa.xc.demo.bean.Student;
import com.yokogawa.xc.demo.room.BookDao;
import com.yokogawa.xc.demo.room.ClassBDao;
import com.yokogawa.xc.demo.room.StudentDao;
import com.yokogawa.xc.demo.room.TestDatabase;
import com.yokogawa.xc.room.dao.check_templateDao;
import com.yokogawa.xc.room.dao.check_template_detailDao;
import com.yokogawa.xc.room.dao.staffDao;
import com.yokogawa.xc.room.database.YokogawaManager;
import com.yokogawa.xc.room.entity.check_template;
import com.yokogawa.xc.room.entity.check_template_detail;
import com.yokogawa.xc.room.entity.staff;
import com.yokogawa.xc.utils.GetJsonDataUtil;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestRoomActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private com.yokogawa.xc.room.dao.staffDao staffDao;
    private check_templateDao checkTemplateDao;
    private check_template_detailDao checkDetailDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_room);
        ButterKnife.bind(this);
        tvTitle.setText("数据库操作");


        staffDao = YokogawaManager.getIntance(this).getStaffDao();
        checkTemplateDao = YokogawaManager.getIntance(this).getCheckTemplateDao();
        checkDetailDao = YokogawaManager.getIntance(this).getCheckDetailDao();
    }

    @OnClick({R.id.tv_back, R.id.room_addMsg, R.id.room_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.room_clear:
                //TODO 添加数据
                String JsonData = new GetJsonDataUtil().getJson(MyApplication.getInstance(), "staff.json");
                List<staff> mList = new Gson().fromJson(JsonData, new TypeToken<List<staff>>() {
                }.getType());

                String ctJson = new GetJsonDataUtil().getJson(MyApplication.getInstance(), "check_template.json");
                List<check_template> check_templates = new Gson().fromJson(ctJson, new TypeToken<List<check_template>>() {
                }.getType());

                String ctdJson = new GetJsonDataUtil().getJson(MyApplication.getInstance(), "check_template_detail.json");
                List<check_template_detail> check_details = new Gson().fromJson(ctdJson, new TypeToken<List<check_template_detail>>() {
                }.getType());

                staffDao.insertRx(mList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxLife.as(TestRoomActivity.this))
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("TAG", "staffDao: 插入成功");
                            }
                        });

                checkTemplateDao.insertRx(check_templates)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxLife.as(TestRoomActivity.this))
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("TAG", "checkTemplateDao: 插入成功");
                            }
                        });
                checkDetailDao.insertRx(check_details)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxLife.as(TestRoomActivity.this))
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("TAG", "checkDetailDao: 插入成功");
                            }
                        });

                break;

            case R.id.room_addMsg:
                //TODO 多表连接查询
                checkDetailDao.getList("28")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxLife.as(this))
                        .subscribe(new Consumer<List<CombinaBean>>() {
                            @Override
                            public void accept(List<CombinaBean> combinaBeans) throws Exception {
                                Log.e("TAG", "连接数据===" + combinaBeans.toString());
                                Log.e("TAG", "连接数据===" + combinaBeans.size());
                            }
                        });
                break;
        }
    }
}