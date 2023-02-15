package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 组立线表
@Entity
public class group {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int group_name;                     //组立线名称
    private int delete_flag;                    //删除，0:正常，1：删除
    private long create_time;                   //创建时间
    private long update_time;                   //更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_name() {
        return group_name;
    }

    public void setGroup_name(int group_name) {
        this.group_name = group_name;
    }

    public int getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
