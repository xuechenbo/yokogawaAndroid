package com.yokogawa.xc.room.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yokogawa.xc.demo.bean.ExamiBean;

import java.util.List;

public class ExamCoverters {

    @TypeConverter
    public List<ExamiBean.Children> stringToObject(String value) {
        return new Gson().fromJson(value, new TypeToken<List<ExamiBean.Children>>() {
        }.getType());
    }

    @TypeConverter
    public String objectToString(List<ExamiBean.Children> list) {
        return new Gson().toJson(list);
    }
}
