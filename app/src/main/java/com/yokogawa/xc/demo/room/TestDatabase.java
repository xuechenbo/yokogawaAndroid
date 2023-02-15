package com.yokogawa.xc.demo.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yokogawa.xc.demo.bean.Book;
import com.yokogawa.xc.demo.bean.ClassB;
import com.yokogawa.xc.demo.bean.Student;

@Database(entities = {Student.class, Book.class, ClassB.class}, version = 8)
public abstract class TestDatabase extends RoomDatabase {
    public abstract ConnectDao getConnectDAO();

    public abstract StudentDao studentDao();

    public abstract BookDao bookDao();

    public abstract ClassBDao getClassDao();

    private static TestDatabase mDb;

    public static TestDatabase getIntance(Context context) {
        if (mDb == null) {
            synchronized (TestDatabase.class) {
                mDb = Room.databaseBuilder(context, TestDatabase.class, "CONNECT")
                        .fallbackToDestructiveMigration()// 删除数据重建  数据会清空
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return mDb;
    }
}
