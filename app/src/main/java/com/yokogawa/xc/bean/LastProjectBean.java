package com.yokogawa.xc.bean;

public class LastProjectBean {


    private int id;
    private String checkName;
    private int progressId;
    private int groupId;
    private String projectId;
    private String groupName;
    private String projectName;
    private int staffId;
    private String staffName;
    private String seriesNumber;
    private String calculateNumber;
    private String modeName;
    private String loginNumber;
    private String result;
    private String startTime;
    private String endTime;
    private int deleteFlag;
    private String creator;
    private String createTime;
    private String updater;
    private String updateTime;
    private int status;
    private String checkContentJson;
    private String totalCheckContentJson;
    private int isLast;
    private String isOnePerson;
    private String templateHeaderJson;

    public String getTemplateHeaderJson() {
        return templateHeaderJson==null?"":templateHeaderJson;
    }

    public void setTemplateHeaderJson(String templateHeaderJson) {
        this.templateHeaderJson = templateHeaderJson;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
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

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getCalculateNumber() {
        return calculateNumber;
    }

    public void setCalculateNumber(String calculateNumber) {
        this.calculateNumber = calculateNumber;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getLoginNumber() {
        return loginNumber;
    }

    public void setLoginNumber(String loginNumber) {
        this.loginNumber = loginNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCheckContentJson() {
        return checkContentJson;
    }

    public void setCheckContentJson(String checkContentJson) {
        this.checkContentJson = checkContentJson;
    }

    public String getTotalCheckContentJson() {
        return totalCheckContentJson;
    }

    public void setTotalCheckContentJson(String totalCheckContentJson) {
        this.totalCheckContentJson = totalCheckContentJson;
    }

    public int getIsLast() {
        return isLast;
    }

    public void setIsLast(int isLast) {
        this.isLast = isLast;
    }

    public String getIsOnePerson() {
        return isOnePerson;
    }

    public void setIsOnePerson(String isOnePerson) {
        this.isOnePerson = isOnePerson;
    }
}
