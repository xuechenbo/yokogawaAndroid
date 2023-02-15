package com.yokogawa.xc.bean;

import java.util.List;

public class CheckItemBean {


    private int pageNum;
    private int pageSize;
    private int totalPage;
    private int total;
    private List<ChildLiST> list;

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

    public List<ChildLiST> getList() {
        return list;
    }

    public void setList(List<ChildLiST> list) {
        this.list = list;
    }

    public static class ChildLiST {
        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", checkName='" + checkName + '\'' +
                    ", progressId=" + progressId +
                    ", groupId=" + groupId +
                    ", projectId=" + projectId +
                    ", staffId=" + staffId +
                    ", staffName='" + staffName + '\'' +
                    ", seriesNumber='" + seriesNumber + '\'' +
                    ", calculateNumber='" + calculateNumber + '\'' +
                    ", modeName='" + modeName + '\'' +
                    ", loginNumber='" + loginNumber + '\'' +
                    ", result='" + result + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", deleteFlag=" + deleteFlag +
                    ", creator='" + creator + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updater='" + updater + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", checkContentJson='" + checkContentJson + '\'' +
                    '}';
        }

        private int id;
        private String checkName;
        private int progressId;
        private int groupId;
        private int projectId;
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
        private String checkContentJson;
        private String stationName;
        private String groupName;

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getStationName() {
            return stationName == null ? "" : stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getTemplateHeaderJson() {
            return templateHeaderJson;
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

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
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

        public String getCheckContentJson() {
            return checkContentJson;
        }

        public void setCheckContentJson(String checkContentJson) {
            this.checkContentJson = checkContentJson;
        }
    }
}
