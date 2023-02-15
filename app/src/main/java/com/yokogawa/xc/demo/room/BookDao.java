package com.yokogawa.xc.demo.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yokogawa.xc.demo.bean.Book;
import com.yokogawa.xc.demo.bean.Combination;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM Book")
    List<Book> getAll();

    @Query("SELECT * FROM Book WHERE studentId IN (:bookId)")
    List<Book> loadAllByIds(int[] bookId);

    @Query("SELECT * FROM Book WHERE studentId =:studentId")
    Book getBook(String studentId);

    @Insert
    void insert(Book... users);

    @Update
    void update(Book... users);

    @Delete
    void delete(Book user);

    //使用内连接查询
    @Query("SELECT studentId,name,bookName,bookId,age,className from Student INNER JOIN Book ON Student.sId = Book.studentId INNER JOIN ClassB ON ClassB.classId = Book.bookId")
    List<Combination> getFromCompany();

    //删除 更新 不能

    @Query("UPDATE Book SET bookName='测试' WHERE bookName = :users")
    int querUpdata(String users);


    @Query("DELETE FROM Book WHERE bookName=:bookname")
    int deleteByBookname(String bookname);


    //DROP TABLE 语句用于删除表。
    //DROP INDEX 语句 用于删除表中的索引

}
