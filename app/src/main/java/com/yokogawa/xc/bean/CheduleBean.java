package com.yokogawa.xc.bean;

import java.io.Serializable;

public class CheduleBean implements Serializable {

    private int id;
    private int templateId;
    private String templateName;
    private int groupId;
    private String projectId;
    private String groupName;
    private String projectName;
    private String checkContentJson;
    private int deleteFlag;
    private String creator;
    private String createTime;
    private Object updater;
    private String updateTime;
    private String checkId;
    private Object lastStaffId;
    private Object lastStaffName;
    private CheckTableProgress checkTableProgress;
    private String isLast;
    private String isConductLast;
    private String isOnePerson;
    private String checkNum;
    //指图书信息
    private RefersBookManage refersBookManage;

    public RefersBookManage getRefersBookManage() {
        return refersBookManage;
    }

    public void setRefersBookManage(RefersBookManage refersBookManage) {
        this.refersBookManage = refersBookManage;
    }


    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }


    public String getIsOnePerson() {
        return isOnePerson;
    }

    public void setIsOnePerson(String isOnePerson) {
        this.isOnePerson = isOnePerson;
    }


    public String getIsConductLast() {
        return isConductLast;
    }

    public void setIsConductLast(String isConductLast) {
        this.isConductLast = isConductLast;
    }


    public String getTemplateHeaderJson() {
        return templateHeaderJson == null ? "" : templateHeaderJson;
    }

    public void setTemplateHeaderJson(String templateHeaderJson) {
        this.templateHeaderJson = templateHeaderJson;
    }

    private String templateHeaderJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public String getCheckContentJson() {
        return checkContentJson;
    }

    public void setCheckContentJson(String checkContentJson) {
        this.checkContentJson = checkContentJson;
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

    public Object getUpdater() {
        return updater;
    }

    public void setUpdater(Object updater) {
        this.updater = updater;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Object getLastStaffId() {
        return lastStaffId;
    }

    public void setLastStaffId(Object lastStaffId) {
        this.lastStaffId = lastStaffId;
    }

    public Object getLastStaffName() {
        return lastStaffName;
    }

    public void setLastStaffName(Object lastStaffName) {
        this.lastStaffName = lastStaffName;
    }

    public CheckTableProgress getCheckTableProgress() {
        return checkTableProgress == null ? new CheckTableProgress() : checkTableProgress;
    }

    public void setCheckTableProgress(CheckTableProgress checkTableProgress) {
        this.checkTableProgress = checkTableProgress;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }


    public static class CheckTableProgress {
        private int id;
        private String checkTableName;
        private int groupId;
        private int lastProjectFlag;
        private int singleProductFlag;
        private String seriesNumber = "";
        private String calculateNumber = "";
        private String modeName = "";
        private Object loginNumber;
        private String progress;
        private int status;
        private int deleteFlag;
        private String creator;
        private String createTime;
        private String updater;
        private String updateTime;
        private int templateId;
        private String linkage = "";

        public String getLinkage() {
            return linkage;
        }

        public void setLinkage(String linkage) {
            this.linkage = linkage;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCheckTableName() {
            return checkTableName;
        }

        public void setCheckTableName(String checkTableName) {
            this.checkTableName = checkTableName;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getLastProjectFlag() {
            return lastProjectFlag;
        }

        public void setLastProjectFlag(int lastProjectFlag) {
            this.lastProjectFlag = lastProjectFlag;
        }

        public int getSingleProductFlag() {
            return singleProductFlag;
        }

        public void setSingleProductFlag(int singleProductFlag) {
            this.singleProductFlag = singleProductFlag;
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

        public Object getLoginNumber() {
            return loginNumber;
        }

        public void setLoginNumber(Object loginNumber) {
            this.loginNumber = loginNumber;
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

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }
    }

}
