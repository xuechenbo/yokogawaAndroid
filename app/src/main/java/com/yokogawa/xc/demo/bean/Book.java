package com.yokogawa.xc.demo.bean;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int bookId;

    @ColumnInfo(name = "bookName")
    public String bookName;

    @ColumnInfo(name = "studentId", index = true)
    public String studentId;

    @Ignore
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
