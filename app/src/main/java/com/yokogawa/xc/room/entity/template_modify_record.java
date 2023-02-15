package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 检查表的修改履历表
@Entity
public class template_modify_record {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int template_id;                    //模板ID
    private String modify_content;              //修正内容
    private int delete_flag;                    //删除，0:正常，1：删除
    private String creator;                     //创建者
    private long create_time;                   //创建时间
    private String updater;                     //最后更新者
    private long update_time;                   //更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public String getModify_content() {
        return modify_content;
    }

    public void setModify_content(String modify_content) {
        this.modify_content = modify_content;
    }

    public int getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
