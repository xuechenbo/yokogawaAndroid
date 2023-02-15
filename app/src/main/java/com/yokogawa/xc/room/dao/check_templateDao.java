package com.yokogawa.xc.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.yokogawa.xc.room.entity.check_template;
import java.util.List;
import io.reactivex.Completable;

@Dao
public interface check_templateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRx(List<check_template> list);

}
