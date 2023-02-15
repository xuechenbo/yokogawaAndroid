package com.yokogawa.xc.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;

public class T {
    private static Toast toast = null;


    public static void show(String text, int duration) {
        show(MyApplication.getInstance(), text, duration);
    }

    //TODO
    public static void show(String text) {
        show(MyApplication.getInstance(), text);
    }

    public static void show(int id, int duration) {
        show(MyApplication.getInstance(), id, duration);
    }

    public static void show(int id) {
        show(MyApplication.getInstance(), id);
    }

    public static void show(Context context, String text, int duration) {
        if (null == context)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (null == toast) {
//                toast = makeToast(MyApplication.getInstance(), text, duration);
                toast = Toast.makeText(MyApplication.getInstance(), text, duration);
            } else {
                toast.setText(text);
                toast.setDuration(duration);
            }
            toast.show();
        } else {
            if (null != toast) {
                toast.cancel();
                toast = null;
            }
//            toast = makeToast(MyApplication.getInstance(), text, duration);
            toast = Toast.makeText(MyApplication.getInstance(), text, duration);
            toast.show();
        }
    }

    public static Toast makeToast(Context context, String text, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_toast_layout, null);
        TextView chapterNameTV = (TextView) view.findViewById(R.id.chapterName);
        chapterNameTV.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(duration);
        toast.setView(view);
        return toast;
    }

    //TODO ISSUCCESS
    public static void show(Context context, String text) {
        if (null == context)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (null == toast) {
                toast = makeToast(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
//                toast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
            } else {
                toast = makeToast(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
//                toast.setText(text);
//                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.show();
        } else {
            if (null != toast) {
                toast.cancel();
                toast = null;
            }
            toast = makeToast(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
//            toast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public static void show(Context context, int id, int duration) {
        if (null == context)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (null == toast) {
                toast = Toast.makeText(MyApplication.getInstance(), id, duration);
            } else {
                toast.setText(id);
                toast.setDuration(duration);
            }
            toast.show();
        } else {
            if (null != toast) {
                toast.cancel();
                toast = null;
            }
            toast = Toast.makeText(MyApplication.getInstance(), id, duration);
            toast.show();
        }

    }

    public static void show(Context context, int id) {
        if (null == context)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (null == toast) {
                toast = Toast.makeText(MyApplication.getInstance(), id, Toast.LENGTH_LONG);
            } else {
                toast.setText(id);
                toast.setDuration(Toast.LENGTH_LONG);
            }
            toast.show();
        } else {
            if (null != toast) {
                toast.cancel();
                toast = null;
            }
            toast = Toast.makeText(MyApplication.getInstance(), id, Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
