package com.yokogawa.xc.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//TODO 员工表
@Entity
public class staff {
    @PrimaryKey(autoGenerate = false)
    private int id;
    @ColumnInfo(name = "job_number")
    private String job_number;                  //工号，账号
    @ColumnInfo(name = "staff_name")
    private String staff_name;                  //员工姓名
    @ColumnInfo(name = "password")
    private String password;                    //密码
    @ColumnInfo(name = "level")
    private int level;                          //级别
    @ColumnInfo(name = "autograph_url")
    private String autograph_url;               //员工签名URL
    @ColumnInfo(name = "status")
    private int status;                         //状态：0：正常，1待审核
    @ColumnInfo(name = "delete_flag")
    private int delete_flag;                    //删除，0:正常，1：删除
    @ColumnInfo(name = "creator")
    private String creator;                     //创建者
    @ColumnInfo(name = "create_time")
    private String create_time;                   //创建时间
    @ColumnInfo(name = "updater")
    private String updater;                     //最后更新者
    @ColumnInfo(name = "update_time")
    private String update_time;                   //更新时间

    //表中没有的字段
    @Ignore
    private int groupId;
    @Ignore
    private int projectId;
    @Ignore
    private String groupName;
    @Ignore
    private String projectName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAutograph_url() {
        return autograph_url;
    }

    public void setAutograph_url(String autograph_url) {
        this.autograph_url = autograph_url;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
