package com.yokogawa.xc.bean;

import java.util.List;

public class ShowResumBean {


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
        private int templateId;
        private String templateName;
        private String modifyContent;
        private int deleteFlag;
        private String creator;
        private long createTimestampTime;
        private String updater;
        private long updateTimestampTime;
        private int status;

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

        public String getModifyContent() {
            return modifyContent;
        }

        public void setModifyContent(String modifyContent) {
            this.modifyContent = modifyContent;
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

        public long getCreateTime() {
            return createTimestampTime;
        }

        public void setCreateTime(long createTime) {
            this.createTimestampTime = createTime;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public long getUpdateTime() {
            return updateTimestampTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTimestampTime = updateTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
