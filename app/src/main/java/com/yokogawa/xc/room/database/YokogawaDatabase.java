package com.yokogawa.xc.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.yokogawa.xc.room.dao.auditDao;
import com.yokogawa.xc.room.dao.check_templateDao;
import com.yokogawa.xc.room.dao.check_template_detailDao;
import com.yokogawa.xc.room.dao.staffDao;
import com.yokogawa.xc.room.entity.audit;
import com.yokogawa.xc.room.entity.check_project_master;
import com.yokogawa.xc.room.entity.check_table_progress;
import com.yokogawa.xc.room.entity.check_template;
import com.yokogawa.xc.room.entity.check_template_detail;
import com.yokogawa.xc.room.entity.group;
import com.yokogawa.xc.room.entity.project;
import com.yokogawa.xc.room.entity.staff;
import com.yokogawa.xc.room.entity.staff_group_project;
import com.yokogawa.xc.room.entity.template_modify_record;

@Database(entities = {
        audit.class,
        check_project_master.class,
        check_table_progress.class,
        check_template.class,
        check_template_detail.class,
        group.class,
        project.class,
        staff.class,
        staff_group_project.class,
        template_modify_record.class
}, version = 3)
public abstract class YokogawaDatabase extends RoomDatabase {
    public abstract auditDao getAuditDao();

    public abstract staffDao getStaffDao();
    public abstract check_templateDao getCheckTemplateDao();
    public abstract check_template_detailDao getCheckDetailDao();
}
