package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 检查项目库表
@Entity
public class check_project_master {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int group_id;            //组立线id
    private int project_id;          //工程ID
    private String spec;             //规格
    private String check_content;    //检查内容/确认内容
    private String attention;        //注意点/确认基准
    private String mode_rule;        //型名规则
    private int status;              //状态：0：正常，1待审核
    private int delete_flag;         //删除，0:正常，1：删除
    private String creator;          //创建者
    private long create_time;        //创建时间
    private String updater;          //最后更新者
    private long update_time;        //更新时间
    private String check_name;       //检查项目名称
    private String rule_content;     //规则内容

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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getCheck_content() {
        return check_content;
    }

    public void setCheck_content(String check_content) {
        this.check_content = check_content;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getMode_rule() {
        return mode_rule;
    }

    public void setMode_rule(String mode_rule) {
        this.mode_rule = mode_rule;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getRule_content() {
        return rule_content;
    }

    public void setRule_content(String rule_content) {
        this.rule_content = rule_content;
    }
}
