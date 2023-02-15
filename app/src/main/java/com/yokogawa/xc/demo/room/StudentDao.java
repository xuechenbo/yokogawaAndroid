package com.yokogawa.xc.demo.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yokogawa.xc.demo.bean.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Query("SELECT * FROM Student WHERE sId IN (:userIds)")
    List<Student> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Student WHERE age=:age")
    List<Student> getStudentByAge(int age);

    @Query("SELECT * FROM Student WHERE age=:age LIMIT :max")
    List<Student> getStudentByAge(int max, int... age);

    @Query("SELECT * FROM Student WHERE sId =:sId")
    Student getStudent(int sId);

    @Insert
    void insertAll(Student... users);

    @Update
    void updateStudent(Student... users);

    @Delete
    void delete(Student user);
}
