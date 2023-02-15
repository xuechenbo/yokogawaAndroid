package com.yokogawa.xc.demo.bean;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yokogawa.xc.room.converter.AAAConverters;
import com.yokogawa.xc.utils.Utils;

import java.util.List;

@Entity
@TypeConverters({AAAConverters.class})
public class AAAA implements MultiItemEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "tabId")
    public String tabId;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "spName")
    private String spName;
    @ColumnInfo(name = "secondData")
    private List<SecondDataBean> secondData;
    @ColumnInfo(name = "project")
    private String project;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpName() {
        return spName == null ? "null" : spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public List<SecondDataBean> getSecondData() {
        return secondData;
    }

    public void setSecondData(List<SecondDataBean> secondData) {
        this.secondData = secondData;
    }

    @Override
    public int getItemType() {
        return Integer.parseInt(type);
    }

    public static class SecondDataBean implements MultiItemEntity {

        //TODO ***
        public boolean isFinished(Context context) {
            boolean isFinish = true;
            if (getMsgAnswer() == null) {
                return false;
            } else {
                String[] split = getMsgAnswer().split(",");
                for (int i = 0; i < split.length; i++) {
                    switch (getType()) {
                        case "1":
                            //选择
                            if (split[i].equals("false")) {
                                isFinish = false;
                            }
                            break;
                        case "2":
                            //填空
                            if (split[i].trim().isEmpty()) {
                                isFinish = false;
                            }
                            break;
                        case "3":
                            if (split[i].equals("false")) {
                                isFinish = false;
                            } else if (split[i].trim().isEmpty()) {
                                isFinish = false;
                            }
                            break;
                    }
                }
            }
            return isFinish;
        }

        @Override
        public String toString() {
            return "{" +
                    "mid=" + mid +
                    ", type='" + type + '\'' +
                    ", msg='" + msg + '\'' +
                    ", msgAnswer='" + msgAnswer + '\'' +
                    ", topTitle='" + topTitle + '\'' +
                    ", thridMsg=" + thridMsg +
                    ", examination='" + examination + '\'' +
                    '}';
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        @PrimaryKey(autoGenerate = true)
        public int mid;

        private String type;
        private String msg;
        //msg 结果
        private String msgAnswer;
        //规则
        private String rule;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        private String topTitle;
        private List<ThridMsgBean> thridMsg;
        private String examination;

        public String getTopTitle() {
            return topTitle == null ? "" : topTitle;
        }

        public void setTopTitle(String topTitle) {
            this.topTitle = topTitle;
        }

        public String getMsgAnswer() {
            return msgAnswer;
        }

        public void setMsgAnswer(String msgAnswer) {
            this.msgAnswer = msgAnswer;
        }

        public String getExamination() {
            return examination == null ? "测试数据" : examination;
        }

        public void setExamination(String examination) {
            this.examination = examination;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public int getItemType() {
            return Integer.parseInt(type);
        }

        public List<ThridMsgBean> getThridMsg() {
            return thridMsg;
        }

        public void setThridMsg(List<ThridMsgBean> thridMsg) {
            this.thridMsg = thridMsg;
        }

        public static class ThridMsgBean implements MultiItemEntity {

            @Override
            public String toString() {
                return "{" +
                        "type='" + type + '\'' +
                        ", msg='" + msg + '\'' +
                        ", TmsgAnswer='" + TmsgAnswer + '\'' +
                        '}';
            }

            private String type;
            private String msg;
            private String TmsgAnswer;

            public String getTmsgAnswer() {
                return TmsgAnswer;
            }

            public void setTmsgAnswer(String tmsgAnswer) {
                TmsgAnswer = tmsgAnswer;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            @Override
            public int getItemType() {
                return Integer.parseInt(type);
            }

            //TODO **
            public boolean isFinished() {
                return true;
            }
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", tabId='" + tabId + '\'' +
                ", type='" + type + '\'' +
                ", spName='" + spName + '\'' +
                ", secondData=" + secondData +
                ", project='" + project + '\'' +
                '}';
    }

    public boolean isFinished(Context context) {
        boolean isType = false;
        for (int i = 0; i < getSecondData().size(); i++) {
            if (getSecondData().get(i).isFinished(context)) {
                isType = true;
            } else {
                isType = false;
                break;
            }
        }
        return isType;
    }
}
