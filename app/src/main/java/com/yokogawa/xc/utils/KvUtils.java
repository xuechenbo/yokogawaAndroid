package com.yokogawa.xc.utils;

import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.Set;

public class KvUtils {
    private static KvUtils mInstance;
    private static MMKV mv;

    private KvUtils() {
        mv = MMKV.defaultMMKV();
    }

    public static KvUtils getInstance() {
        if (mInstance == null) {
            synchronized (SpUtils.class) {
                if (mInstance == null) {
                    mInstance = new KvUtils();
                }
            }
        }
        return mInstance;
    }

    public static void encode(String key, Object object) {
        if (object instanceof String) {
            mv.encode(key, (String) object);
        } else if (object instanceof Integer) {
            mv.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mv.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            mv.encode(key, (Float) object);
        } else if (object instanceof Long) {
            mv.encode(key, (Long) object);
        } else if (object instanceof Double) {
            mv.encode(key, (Double) object);
        } else if (object instanceof byte[]) {
            mv.encode(key, (byte[]) object);
        } else {
            mv.encode(key, object.toString());
        }
    }

    public static void encodeSet(String key, Set<String> sets) {
        mv.encode(key, sets);
    }

    public static void encodeParcelable(String key, Parcelable obj) {
        mv.encode(key, obj);
    }

    public static Integer getInt(String key) {
        return mv.decodeInt(key, 0);
    }

    public static Double getDouble(String key) {
        return mv.decodeDouble(key, 0.00);
    }

    public static Long getLong(String key) {
        return mv.decodeLong(key, 0L);
    }

    public static Boolean getBoolean(String key) {
        return mv.decodeBool(key, false);
    }

    public static Float getFloat(String key) {
        return mv.decodeFloat(key, 0F);
    }

    public static byte[] getBytes(String key) {
        return mv.decodeBytes(key);
    }

    public static String getString(String key) {
        return mv.decodeString(key, "");
    }

    public static Set<String> getStringSet(String key) {
        return mv.decodeStringSet(key, Collections.<String>emptySet());
    }

    public static Parcelable getParcelable(String key) {
        return mv.decodeParcelable(key, null);
    }

    public static void removeKey(String key) {
        mv.removeValueForKey(key);
    }

    public static void clearAll() {
        mv.clearAll();
    }

}
