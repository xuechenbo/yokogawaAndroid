package com.yokogawa.xc.demo.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.demo.bean.RadioBean;
import com.yokogawa.xc.demo.bean.AAAA;
import com.yokogawa.xc.room.database.AAAManager;
import com.yokogawa.xc.demo.room.AAADao;
import com.yokogawa.xc.utils.GetJsonDataUtil;
import com.yokogawa.xc.utils.T;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

//最初demo 数据库添加删除
public class RoomActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rectangles)
    RecyclerView rectangles;
    private AAADao userDao;
    private ArrayList<RadioBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);


        tvTitle.setText("数据库操作");
        userDao = AAAManager.getIntance(MyApplication.getInstance()).getUserDao();
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            RadioBean radioBean = new RadioBean();
            RadioBean.LastProject lastProject = new RadioBean.LastProject();
            lastProject.setValue("");
            radioBean.setLastProject(lastProject);
            list.add(radioBean);
        }
        MyAdapter myAdapter = new MyAdapter(R.layout.item_radiobutton, list);
        myAdapter.bindToRecyclerView(rectangles);

    }

    @OnClick({R.id.tv_back, R.id.room_addMsg, R.id.room_clear})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.room_addMsg:
                //获取assets目录下的json文件数据
                String JsonData = new GetJsonDataUtil().getJson(MyApplication.getInstance(), "province.json");
                List<AAAA> TomList = new Gson().fromJson(JsonData, new TypeToken<List<AAAA>>() {
                }.getType());
                userDao.insertRx(TomList).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                T.show( "添加成功");
                            }
                        });
                break;
            case R.id.room_clear:
                userDao.deleteAllMsg().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.e("TAG", "onSubscribe: ......onsubscribe");
                            }

                            @Override
                            public void onComplete() {
                                T.show( "删除成功");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                break;
        }
    }

    class MyAdapter extends BaseQuickAdapter<RadioBean, BaseViewHolder> {

        public MyAdapter(int layoutResId, @Nullable List<RadioBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RadioBean item) {
            RadioGroup radioGroup = helper.getView(R.id.rg);

            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.clearCheck();
            switch (item.getLastProject().getValue()) {
                case "":
                    break;
                case "1":
                    radioGroup.check(R.id.rb1);
                    break;
                case "2":
                    radioGroup.check(R.id.rb2);
                    break;
                default:
                    radioGroup.clearCheck();
                    break;
            }
            radioGroup.setId(helper.getAdapterPosition());
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.rb1:
                            item.getLastProject().setValue("1");
                            break;
                        case R.id.rb2:
                            item.getLastProject().setValue("2");
                            break;
                        default:
                            break;
                    }

                }
            });

        }
    }
}