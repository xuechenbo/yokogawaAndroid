package com.yokogawa.xc.room.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yokogawa.xc.room.dao.staffDao;

public class YokogawaManager {
    private static YokogawaDatabase mYDB;
    public static YokogawaDatabase getIntance(Context context) {
        if (mYDB == null) {
            synchronized (YokogawaDatabase.class) {
                mYDB = Room.databaseBuilder(context, YokogawaDatabase.class, "YokogawaDB")
                    .fallbackToDestructiveMigration()// 删除数据重建  数据会清空
//                    .addMigrations(MIGRATION_3_4)  //数据库升级
//                    .allowMainThreadQueries()//主线程操作
                        .build();
            }
        }
        return mYDB;
    }

    public static staffDao getStaffDao() {
        return mYDB.getStaffDao();
    }


    //数据库升级  增加一个字段   int boolean 需要一个默认值
    private static Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE AAAA "
                    + " ADD COLUMN isPosition INTEGER NOT NULL DEFAULT 0");
        }
    };
    /**
     * 如果修改一个 字段类型
     * 创建一个新的临时表，
     * 将数据从AAAA表复制到临时表，
     * 删了AAAA表
     * 将临时表重命名为AAAA
     * 4升级5
     */
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 创建临时表
            database.execSQL("CREATE TABLE AAAA_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(id))");
            // 拷贝数据
            database.execSQL("INSERT INTO AAAA_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
            // 删除老的表
            database.execSQL("DROP TABLE AAAA");
            // 改名
            database.execSQL("ALTER TABLE AAAA_new RENAME TO AAAA");
        }
    };

}
