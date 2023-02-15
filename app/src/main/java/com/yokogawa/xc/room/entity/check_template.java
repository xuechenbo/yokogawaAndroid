package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 检查表模板表
@Entity
public class check_template {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String check_name;            //检查表名称
    private String group_id;                       //立线组ID
    private String project_id;                     //工程ID
    private String last_project_flag;              //后工程flag，1：是，0：否
    private String single_product_flag;            //是否是单品，1：是，0：否
    private String status;                         //状态：0：正常，1待审核
    private String delete_flag;                    //删除，0:正常，1：删除
    private String creator;                     //创建者
    private String create_time;                   //创建时间
    private String updater;                     //最后更新者
    private String update_time;                   //更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getLast_project_flag() {
        return last_project_flag;
    }

    public void setLast_project_flag(String last_project_flag) {
        this.last_project_flag = last_project_flag;
    }

    public String getSingle_product_flag() {
        return single_product_flag;
    }

    public void setSingle_product_flag(String single_product_flag) {
        this.single_product_flag = single_product_flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
