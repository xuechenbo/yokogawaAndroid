package com.yokogawa.xc.bean;

public class TokenBean {

    private int id;
    private String jobNumber;
    private String staffName;
    private String groupIds;
    private int projectId;
    private String groupName;
    private String projectName;
    private String password;
    private int level;
    private String autographUrl;
    private int status;
    private int deleteFlag;
    private String creator;
    private String createTime;
    private String updater;
    private String updateTime;

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public String getAutographUrl() {
        return autographUrl == null ? "" : autographUrl;
    }

    public void setAutographUrl(String autographUrl) {
        this.autographUrl = autographUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", jobNumber='" + jobNumber + '\'' +
                ", staffName='" + staffName + '\'' +
                ", projectId=" + projectId +
                ", groupName='" + groupName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", password='" + password + '\'' +
                ", level=" + level +
                ", autographUrl='" + autographUrl + '\'' +
                ", status=" + status +
                ", deleteFlag=" + deleteFlag +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updater='" + updater + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
