package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 员工与组立线工程名关系表
@Entity
public class staff_group_project {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int staff_id;                   //员工id
    private int group_id;                   //组立线id
    private int project_id;                 //工程ID
    private int delete_flag;                //删除，0:正常，1：删除
    private long create_time;               //创建时间
    private long update_time;               //更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
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
