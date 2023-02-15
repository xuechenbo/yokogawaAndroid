package com.yokogawa.xc.room.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

//TODO 审核表
@Entity
public class audit {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String source;                      //审核来源
    private int source_id;                      //来源ID
    private int source_type;                    //0:检查项目库，1：检查项目规则库，2：模板修改，等等
    private String modify_description;          //修改说明
    private int delete_flag;                    //删除，0:正常，1：删除
    private String creator;                     //创建者
    private long create_time;                   //创建时间
    private String updater;                     //最后更新者
    private long update_time;                   //更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getSource_type() {
        return source_type;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public String getModify_description() {
        return modify_description;
    }

    public void setModify_description(String modify_description) {
        this.modify_description = modify_description;
    }

    public int getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", source_id=" + source_id +
                ", source_type=" + source_type +
                ", modify_description='" + modify_description + '\'' +
                ", delete_flag=" + delete_flag +
                ", creator='" + creator + '\'' +
                ", create_time=" + create_time +
                ", updater='" + updater + '\'' +
                ", update_time=" + update_time +
                '}';
    }
}
