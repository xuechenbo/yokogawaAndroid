package com.yokogawa.xc.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yokogawa.xc.demo.bean.AAAA;
import com.yokogawa.xc.demo.bean.ExamiBean;
import com.yokogawa.xc.demo.room.ExamiDAO;
import com.yokogawa.xc.demo.room.AAADao;

@Database(entities = {AAAA.class, ExamiBean.class}, version = 6)
public abstract class AAADatabase extends RoomDatabase {
    public abstract AAADao getUserDao();
    public abstract ExamiDAO getexamDao();

    private static AAADatabase mDb;

    public static AAADatabase getIntance(Context context) {
        if (mDb == null) {
            synchronized (AAADatabase.class) {
                mDb = Room.databaseBuilder(context, AAADatabase.class, "testAAA")
                        .fallbackToDestructiveMigration()// 删除数据重建  数据会清空
//                    .addMigrations(MIGRATION_3_4)  //数据库升级
//                    .allowMainThreadQueries()//主线程操作
                        .build();
            }
        }
        return mDb;
    }

}

