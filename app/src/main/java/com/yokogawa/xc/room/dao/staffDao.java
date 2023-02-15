package com.yokogawa.xc.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yokogawa.xc.demo.bean.AAAA;
import com.yokogawa.xc.room.entity.staff;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface staffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRx(List<staff> list);

}
