package com.yokogawa.xc.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 员工作业的进度管理表
@Entity
public class check_table_progress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String check_table_name;            //检查表名称
    private int group_id;                       //立线组ID
    private int last_project_flag;              //后工程flag，1：是，0：否
    private int single_product_flag;            //是否是单品，1：是，0：否
    private String series_number;               //连番
    private String calculate_number;            //计番
    private String mode_name;                   //型名
    private String login_number;                //登录No.
    private String progress;                    //进度
    private int status;                         //状态：0：作业中，1：待作业，2：完结
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

    public String getCheck_table_name() {
        return check_table_name;
    }

    public void setCheck_table_name(String check_table_name) {
        this.check_table_name = check_table_name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getLast_project_flag() {
        return last_project_flag;
    }

    public void setLast_project_flag(int last_project_flag) {
        this.last_project_flag = last_project_flag;
    }

    public int getSingle_product_flag() {
        return single_product_flag;
    }

    public void setSingle_product_flag(int single_product_flag) {
        this.single_product_flag = single_product_flag;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getCalculate_number() {
        return calculate_number;
    }

    public void setCalculate_number(String calculate_number) {
        this.calculate_number = calculate_number;
    }

    public String getMode_name() {
        return mode_name;
    }

    public void setMode_name(String mode_name) {
        this.mode_name = mode_name;
    }

    public String getLogin_number() {
        return login_number;
    }

    public void setLogin_number(String login_number) {
        this.login_number = login_number;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
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
}
