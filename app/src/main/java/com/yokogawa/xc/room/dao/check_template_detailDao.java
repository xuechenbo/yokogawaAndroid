package com.yokogawa.xc.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yokogawa.xc.demo.bean.CombinaBean;
import com.yokogawa.xc.room.entity.check_template_detail;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface check_template_detailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRx(List<check_template_detail> list);


    //查询 模板表和模板详情连接
    @Query("SELECT check_name,ct.project_id,ct.group_id,template_id,check_content_json from check_template_detail as ct  " +
            "INNER JOIN check_template  as ct1 ON ct1.id = ct.template_id " +
            "where ct.template_id = :tempId")
    Flowable<List<CombinaBean>> getList(String tempId);
}
