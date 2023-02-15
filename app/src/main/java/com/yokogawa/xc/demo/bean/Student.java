package com.yokogawa.xc.demo.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    public int sId;
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "age")
    public int age;

    public int getId() {
        return sId;
    }

    public void setId(int sId) {
        this.sId = sId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "{" +
                "sId=" + sId +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
