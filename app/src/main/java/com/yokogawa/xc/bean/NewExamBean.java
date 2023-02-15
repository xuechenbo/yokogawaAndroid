package com.yokogawa.xc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewExamBean implements Serializable {

    private int groupId;
    private String groupName;
    private List<String> columnNameList;
    private List<Boolean> columnIsShow;
    private String createName;
    private String masterUrl;
    private String ngStatus;
    private String createDate;
    private String stationName;
    private String stationId;
    private LastProject lastProject;
    private List<Project> project;


    public String getNgStatus() {
        return ngStatus == null ? "" : ngStatus;
    }

    public void setNgStatus(String ngStatus) {
        this.ngStatus = ngStatus;
    }

    public String getMasterUrl() {
        return masterUrl == null ? "" : masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }

    public List<Boolean> getColumnIsShow() {
        return columnIsShow;
    }

    public void setColumnIsShow(List<Boolean> columnIsShow) {
        this.columnIsShow = columnIsShow;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public LastProject getLastProject() {
        return lastProject;
    }

    public void setLastProject(LastProject lastProject) {
        this.lastProject = lastProject;
    }

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    public static class LastProject {
        private boolean isShow;
        private String value;

        public boolean isIsShow() {
            return isShow;
        }

        public void setIsShow(boolean isShow) {
            this.isShow = isShow;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Project implements Serializable {
        @Override
        public String toString() {
            return "{" +
                    "projectId=" + projectId +
                    ", projectName='" + projectName + '\'' +
                    ", check=" + check +
                    '}';
        }

        public boolean isFinished() {
            boolean isFinish = true;
            List<NewExamBean.Project.Check> check = getCheck();
            for (int i = 0; i < check.size(); i++) {
                isFinish = check.get(i).isFinish();
                if (!isFinish) {
                    isFinish = false;
                    break;
                }
            }
            return isFinish;
        }

        public boolean isNg() {
            boolean isNg = false;
            List<NewExamBean.Project.Check> check = getCheck();
            for (int i = 0; i < check.size(); i++) {
                boolean ng = check.get(i).isNg();
                if (ng) {
                    isNg = true;
                    break;
                }
            }
            return isNg;
        }


        private String projectId;
        private String projectName;
        private List<Check> check;

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public List<Check> getCheck() {
            return check;
        }

        public void setCheck(List<Check> check) {
            this.check = check;
        }

        public static class Check implements Serializable {

            @Override
            public String toString() {
                return "{" +
                        "checkId='" + checkId + '\'' +
                        ", checkName='" + checkName + '\'' +
                        ", children=" + children +
                        '}';
            }

            public boolean isFinish() {
                boolean isFinish = true;
                List<NewExamBean.Project.Check.Children> children = getChildren();
                for (int i = 0; i < children.size(); i++) {
                    isFinish = children.get(i).isFinish();
                    if (!isFinish) {
                        isFinish = false;
                        break;
                    }
                }
                return isFinish;
            }

            public boolean isNg() {
                boolean isNg = false;
                List<NewExamBean.Project.Check.Children> children = getChildren();
                for (int i = 0; i < children.size(); i++) {
                    boolean ng = children.get(i).isNg();
                    if (ng) {
                        isNg = true;
                        break;
                    }
                }
                return isNg;
            }

            private String checkId;
            private String checkName;
            private List<Children> children;

            public String getCheckId() {
                return checkId;
            }

            public void setCheckId(String checkId) {
                this.checkId = checkId;
            }

            public String getCheckName() {
                return checkName;
            }

            public void setCheckName(String checkName) {
                this.checkName = checkName;
            }

            public List<Children> getChildren() {
                return children;
            }

            public void setChildren(List<Children> children) {
                this.children = children;
            }

            public static class Children implements Serializable {
                @Override
                public String toString() {
                    return "{" +
                            "childrenContent='" + childrenContent + '\'' +
                            ", answer='" + answer + '\'' +
                            ", spec='" + spec + '\'' +
                            ", attention='" + attention + '\'' +
                            ", modeNumberContent='" + modeNumberContent + '\'' +
                            ", ruleContent='" + ruleContent + '\'' +
                            ", isNG='" + isNG + '\'' +
                            ", checkType='" + checkType + '\'' +
                            ", modeRuleNumber='" + modeRuleNumber + '\'' +
                            ", remark='" + remark + '\'' +
                            ", showValue='" + showValue + '\'' +
                            '}';
                }

                public boolean isNg() {
                    if (getIsNG().equals("0")) {
                        return true;
                    } else {
                        return false;
                    }
                }

                public boolean isFinish() {
                    boolean isFinish = true;
                    if (getAnswer() == null) {
                        return false;
                    } else if (getAnswer().equals("null")) {

                    } else {
                        String[] split = getAnswer().split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].trim().isEmpty()) {
                                isFinish = false;
                                break;
                            }
                        }
                        if (isFinish == false) {
                            return false;
                        } else {
                            for (int i = 0; i < split.length; i++) {
                                if (split[i].trim().equals("true")) {
                                    isFinish = true;
                                    break;
                                } else if (split[i].trim().equals("false")) {
                                    isFinish = false;
                                } else {
                                    if (split[i].trim().isEmpty()) {
                                        isFinish = false;
                                        break;
                                    }
                                }
                            }
                            return isFinish;
                        }
                    }
                    return isFinish;
                }

                private String childrenContent;
                private String answer;
                private String spec;
                private String attention;
                private String modeNumberContent;  //规则位数
                private String ruleContent;        //规则内容
                private String isNG;        //是否NG    NG====0    通过====1
                private String checkType;    //判断类型
                private String modeRuleNumber;   //型名位数
                private String remark;     //备注
                private String modeHead;     //型名头部

                private String showValue = "";     //回显数据

                public String getShowValue() {
                    return showValue;
                }

                public void setShowValue(String showValue) {
                    this.showValue = showValue;
                }

                private List<String> imageUrl = new ArrayList<>();
                private List<String> attentionUrl = new ArrayList<>();

                public List<String> getAttentionUrl() {
                    return attentionUrl;
                }

                public void setAttentionUrl(List<String> attentionUrl) {
                    this.attentionUrl = attentionUrl;
                }

                public List<String> getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(List<String> imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public String getModeHead() {
                    return modeHead;
                }

                public void setModeHead(String modeHead) {
                    this.modeHead = modeHead;
                }

                public String getCheckType() {
                    return checkType == null ? "" : checkType;
                }

                public void setCheckType(String checkType) {
                    this.checkType = checkType;
                }

                public String getModeRuleNumber() {
                    return modeRuleNumber == null ? "" : modeRuleNumber;
                }

                public void setModeRuleNumber(String modeRuleNumber) {
                    this.modeRuleNumber = modeRuleNumber;
                }

                public String getRemark() {
                    return remark == null ? "" : remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getIsNG() {
                    return isNG == null ? "" : isNG;
                }

                public void setIsNG(String isNG) {
                    this.isNG = isNG;
                }

                public String getModeNumberContent() {
                    return modeNumberContent == null ? "" : modeNumberContent;
                }

                public void setModeNumberContent(String modeNumberContent) {
                    this.modeNumberContent = modeNumberContent;
                }

                public String getRuleContent() {
                    return ruleContent == null ? "" : ruleContent;
                }

                public void setRuleContent(String ruleContent) {
                    this.ruleContent = ruleContent;
                }

                public String getChildrenContent() {
                    return childrenContent;
                }

                public void setChildrenContent(String childrenContent) {
                    this.childrenContent = childrenContent;
                }

                public String getAnswer() {
                    return answer;
                }

                public void setAnswer(String answer) {
                    this.answer = answer;
                }

                public String getSpec() {
                    return spec == null ? "" : spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }

                public String getAttention() {
                    return attention == null ? "" : attention;
                }

                public void setAttention(String attention) {
                    this.attention = attention;
                }
            }
        }
    }


    @Override
    public String toString() {
        return "{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", columnNameList=" + columnNameList +
                ", columnIsShow=" + columnIsShow +
                ", createName='" + createName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", lastProject=" + lastProject +
                ", project=" + project +
                '}';
    }

    public boolean isFinished() {
        boolean isType = true;
        for (int i = 0; i < getProject().size(); i++) {
            isType = getProject().get(i).isFinished();
            if (!isType) {
                isType = false;
                break;
            }
        }
        return isType;
    }

    public String isNg() {
        String isNgType = "OK";
        for (int i = 0; i < getProject().size(); i++) {
            boolean ng = getProject().get(i).isNg();
            if (ng) {
                isNgType = "NG";
                break;
            }
        }
        return isNgType;
    }
}
