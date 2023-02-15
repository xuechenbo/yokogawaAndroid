package com.yokogawa.xc.demo.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.yokogawa.xc.demo.bean.ExamiBean;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ExamiDAO {

    @Query("SELECT * FROM ExamiBean")
    Flowable<List<ExamiBean>> getTestAll();

    //Rx 插入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTestRx(List<ExamiBean> list);

    //Rx 更新
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updataItemTestRx(List<ExamiBean> list);

}
