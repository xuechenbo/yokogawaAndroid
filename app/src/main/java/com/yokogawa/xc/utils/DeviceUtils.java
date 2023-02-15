package com.yokogawa.xc.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.yokogawa.xc.MyApplication;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//设备
public class DeviceUtils {
    /**
     * @param context
     * @return 恢复出厂或者刷机后会被重置
     * 部分厂商定制系统中，可能为空，也可能是不同设备中会产生相同的值
     * 对于CDMA设备汇总，AndroidId和DeviceId会返回相同的值
     */
    public static String getAndroidId(Context context) {
        if (context == null) {
            return "";
        }
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return (TextUtils.isEmpty(androidId) ? "" : androidId);
    }



    //78 d6 6c 73 b2 f4                           2   4A:0C:FB:E4:B0:C2
    //04 8c 9a d7 0d 27                           2 5g=D2:1B:3A:A7:14:C5             1= 04:8C:9A:D7:0D:27
    //50 8e 49 52 74 0c
    public static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                Log.e("TAG", "getMacFromHardware===" + nif.getName());
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    Log.e("TAG", "macBytes" + b);
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "未获取到设备Mac地址";
    }


}
