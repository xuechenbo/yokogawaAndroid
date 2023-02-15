package com.yokogawa.xc.bean;

import java.io.Serializable;
import java.util.List;

public class ExamDetailsBean implements Serializable {

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

    public static class ChildList implements Serializable{
        private int id;
        private int templateId;
        private String templateName;
        private int groupId;
        private int projectId;
        private String groupName;
        private String projectName;
        private String checkContentJson;
        private int deleteFlag;
        private String creator;
        private String createTime;
        private Object updater;
        private String updateTime;
        private String checkId;
        private String seriesNumber;
        private String calculateNumber;

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

        private String modeName;

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
    }
}
