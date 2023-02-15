package com.yokogawa.xc.demo.bean;

public class Combination {

    public String name;
    public int age;

    public int bookId;

    public String bookName;

    public String studentId;

    public String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
                "name='" + name + '\'' +
                ", age=" + age +
                ", bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
