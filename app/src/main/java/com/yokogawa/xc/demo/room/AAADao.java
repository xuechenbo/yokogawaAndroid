package com.yokogawa.xc.demo.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.yokogawa.xc.demo.bean.AAAA;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface AAADao {
    //查询指定id
    @Query("SELECT * FROM AAAA WHERE tabId=:id")
    List<AAAA> getIdItem(String id);

    @Query("SELECT * FROM AAAA")
    List<AAAA> getAll();

    @Insert
    void insertALL(List<AAAA> list);

    //更新数据
    @Update
    int updataItem(List<AAAA> list);


    @Delete
    void delete(List<AAAA> list);

    //删全部
    @Query("DELETE FROM aaaa")
    void deleteAll();


    //Rx 查询
    @Query("SELECT * FROM AAAA WHERE tabId=:id")
    Flowable<List<AAAA>> getAll1(String id);

    //Rx 插入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRx(List<AAAA> list);

    //Rx 更新
    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updataItemRx(List<AAAA> list);

    /**
     * 清空所有数据
     */
    @Query("DELETE FROM AAAA")
    Completable deleteAllMsg();
    //Maybe, Single 和 Flowable
}
