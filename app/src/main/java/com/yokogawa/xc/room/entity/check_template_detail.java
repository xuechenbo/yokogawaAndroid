package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 检查模板明细表
@Entity
public class check_template_detail {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String template_id;                    //模板ID
    private String group_id;                       //立线组ID
    private String project_id;                     //工程ID
    private String check_content_json;          //检查表内容json
    private String delete_flag;                    //删除，0:正常，1：删除
    private String creator;                     //创建者
    private String create_time;                   //创建时间
    private String updater;                     //最后更新者
    private String update_time;                   //更新时间
    private String check_id;                       //检查项目id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
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

    public String getCheck_content_json() {
        return check_content_json;
    }

    public void setCheck_content_json(String check_content_json) {
        this.check_content_json = check_content_json;
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

    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }
}
