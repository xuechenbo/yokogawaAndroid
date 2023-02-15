package com.yokogawa.xc.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.yokogawa.xc.room.entity.audit;

import java.util.List;


@Dao
public interface auditDao {

    @Query("SELECT * FROM audit")
    List<audit> getAll();
}
