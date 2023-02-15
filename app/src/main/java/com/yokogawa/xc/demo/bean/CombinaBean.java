package com.yokogawa.xc.demo.bean;

import androidx.room.Ignore;

public class CombinaBean {
    private String check_name;
    private String group_id;
    private String template_id;
    private String project_id;
    private String check_content_json;

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCheck_content_json() {
        return check_content_json;
    }

    public void setCheck_content_json(String check_content_json) {
        this.check_content_json = check_content_json;
    }

    @Override
    public String toString() {
        return "{" +
                "check_name='" + check_name + '\'' +
                ", group_id='" + group_id + '\'' +
                ", template_id='" + template_id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", check_content_json='" + check_content_json + '\'' +
                '}';
    }
}
