package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 工程名
@Entity
public class project {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int group_id;                       //立线组ID
    private String name;                        //工程名称
    private int delete_flag;                    //删除，0:正常，1：删除
    private long create_time;                   //创建时间
    private long update_time;                   //更新时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
