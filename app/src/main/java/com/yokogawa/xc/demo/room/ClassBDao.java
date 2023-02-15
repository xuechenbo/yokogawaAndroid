package com.yokogawa.xc.demo.room;

import androidx.room.Dao;
import androidx.room.Insert;
import com.yokogawa.xc.demo.bean.ClassB;

@Dao
public interface ClassBDao {

    @Insert
    void insert(ClassB... mclass);
}
