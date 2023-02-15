package com.yokogawa.xc.bean;

import java.util.List;

public class CurrListBean {

    private int pageNum;
    private int pageSize;
    private int totalPage;
    private int total;
    private List<ChildList> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ChildList> getList() {
        return list;
    }

    public void setList(List<ChildList> list) {
        this.list = list;
    }

    public static class ChildList {
        private int id;
        private String checkTableName;
        private Object checkTableId;
        private int groupId;
        private String groupName;
        private int lastProjectFlag;
        private int singleProductFlag;
        private String seriesNumber;
        private String calculateNumber;
        private String modeName;
        private Object loginNumber;
        private String progress;
        private int status;
        private int deleteFlag;
        private String creator;
        private String createTime;
        private Object updater;
        private String updateTime;
        private Object projectId;
        private Object checkContentJson;
        private String result;
        private Object startTime;
        private Object endTime;
        private int templateId;

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

        public Object getCheckTableId() {
            return checkTableId;
        }

        public void setCheckTableId(Object checkTableId) {
            this.checkTableId = checkTableId;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
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

        public Object getProjectId() {
            return projectId;
        }

        public void setProjectId(Object projectId) {
            this.projectId = projectId;
        }

        public Object getCheckContentJson() {
            return checkContentJson;
        }

        public void setCheckContentJson(Object checkContentJson) {
            this.checkContentJson = checkContentJson;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Object getStartTime() {
            return startTime;
        }

        public void setStartTime(Object startTime) {
            this.startTime = startTime;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }
    }
}
