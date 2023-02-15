package com.yokogawa.xc.demo.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ClassB {
    @PrimaryKey(autoGenerate = true)
    public int classId;
    public String className;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
