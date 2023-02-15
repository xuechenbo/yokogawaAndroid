package com.yokogawa.xc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;


public class SpUtils {
    private static SharedPreferences sp;
    private static SpUtils instance;
    private SpUtils() {

    }

    //SP 数据导入MMKV
    //MMKV preferences = MMKV.mmkvWithID("UserInfo");
    //SharedPreferences old_man = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    //preferences.importFromSharedPreferences(old_man);
    //old_man.edit().clear().commit();
    //Log.e("TAG", "initView=="+MMKV.mmkvWithID("UserInfo").getString("groupName",""));



    public static SpUtils getInstance(Context context) {
        if (instance == null) {
            sp = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            instance = new SpUtils();
        }
        return instance;
    }

    public void save(String key, Object value) {
        if (value instanceof String) {
            sp.edit().putString(key, (String)value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }
    }
    /**
     * 读取
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        Boolean value = false;
        try {
            value = sp.getBoolean(key,defValue);
        } catch (Exception e){
            value = defValue;
        }finally {
            return value;
        }
    }
    public int getInt(String key, int defValue) {
        int value = 0;
        try {
            value = sp.getInt(key,defValue);
        }catch (Exception e){
            value = defValue;
        }finally {
            return value;
        }
    }

    /**
     * 根据key移除
     * @param key
     */
    public void remove(String key) {
        sp.edit().remove(key).commit();
    }

    public void clear(){
        sp.edit().clear().commit();
    }



    /**
     * 存储List集合
     *
     * @param key     存储的键
     * @param list    存储的集合
     */
    public static void putList( String key, List<? extends Serializable> list) {
        try {
            putObject(  key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取List集合
     *
     * @param key     键
     * @param <E>     指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(  String key) {
        try {
            return (List<E>) getObject( key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 存储对象
     */
    public static void putObject( String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        sp.edit().putString(key, objectStr).commit();

    }



    /**
     * 获取对象
     */
    public static Object getObject( String key)
            throws IOException, ClassNotFoundException {
        String wordBase64 = sp.getString( key,"");
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
}
