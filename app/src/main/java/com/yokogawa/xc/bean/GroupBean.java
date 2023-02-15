package com.yokogawa.xc.bean;

public class GroupBean {

    private String id;      //组立线id  或 工位id
    private String groupName;  //组立线name
    private String name;  //工位name


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
