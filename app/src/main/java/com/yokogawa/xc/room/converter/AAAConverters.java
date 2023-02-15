package com.yokogawa.xc.room.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yokogawa.xc.demo.bean.AAAA;

import java.util.List;

public class AAAConverters {

    @TypeConverter
    public List<AAAA.SecondDataBean> stringToObject(String value) {
        return new Gson().fromJson(value, new TypeToken<List<AAAA.SecondDataBean>>() {
        }.getType());
    }

    @TypeConverter
    public String objectToString(List<AAAA.SecondDataBean> list) {
        return new Gson().toJson(list);
    }
}
