package com.yokogawa.xc.bean;

import java.io.Serializable;

public class RefersBookManage implements Serializable {

    private String createTime;
    private String id;
    private String model;
    private String refersBooksContent;
    private String refersBooksData;
    private String seriesNumber;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRefersBooksContent() {
        return refersBooksContent;
    }

    public void setRefersBooksContent(String refersBooksContent) {
        this.refersBooksContent = refersBooksContent;
    }

    public String getRefersBooksData() {
        return refersBooksData;
    }

    public void setRefersBooksData(String refersBooksData) {
        this.refersBooksData = refersBooksData;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }
}
