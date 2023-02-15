package com.yokogawa.xc.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.GroupBean;
import com.yokogawa.xc.bean.NewExamBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by cpn on 2017/3/6.
 * 验证
 */
public class Utils {

    public void isRemarkValue(String str) {
        Pattern.matches("(\\d-.*)", str);
    }

    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null ||
                mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    //去掉数组第一个元素
    //是否显示，数据显示,abc
    public static String RemoveFirstEle(String str) {
        StringBuffer stBuffer = new StringBuffer();
        String[] arrayStr = ExamUtils.getArrayStr(str);
        for (int i = 0; i < arrayStr.length; i++) {
            if (i == 0) continue;
            if (i == arrayStr.length - 1) {
                stBuffer.append(arrayStr[i]);
            } else {
                stBuffer.append(arrayStr[i] + ",");
            }
        }
        Log.e("TAG", "RemoveFirstEle===" + stBuffer.toString());
        return stBuffer.toString();
    }

    public static String RemoveFirstEle_0(String str) {
        StringBuffer stBuffer = new StringBuffer();
        String[] arrayStr = str.split("\\+");
        for (int i = 0; i < arrayStr.length; i++) {
            if (i == 0) continue;
            if (i == arrayStr.length - 1) {
                stBuffer.append(arrayStr[i]);
            } else {
                stBuffer.append(arrayStr[i] + "+");
            }
        }
        Log.e("TAG", "RemoveFirstEle_0===" + stBuffer.toString());
        return stBuffer.toString();
    }


    //判断是否是单品  如果是数字=组合品,其他单品
    public static boolean isSingleProduct(String number) {
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Matcher isNum = pattern.matcher(number);
        if (isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isNumber(String number) {
        if (number.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Matcher isNum = pattern.matcher(number);
        if (!isNum.matches()) {
            return false;
        } else {
            //数字
            return true;
        }
    }


    public static boolean getStrIs(String content) {
        if (content.equals("[]") || content.isEmpty() || content == null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTypeName(int status, String result) {
        if (result.equals("NG")) {
            return "NG";
        }
        switch (status) {
            case 0:
                return "作业中";
            case 1:
                return "待作业";
            case 2:
                return "完成";
            default:
                return "";
        }
    }

    public static String getNgMsg(NewExamBean newExamBean) {
        boolean ngMsg = true;
        String ngType = "OK";
        List<NewExamBean.Project> data = newExamBean.getProject();
        for (int i = 0; i < data.size(); i++) {
            ngMsg = data.get(i).isNg();
            if (ngMsg) {
                ngType = "NG";
                break;
            }
        }
        return ngType;
    }

    //提交判空
    public static boolean isFinish(NewExamBean newExamBean) {
        boolean isFInish = true;
        List<NewExamBean.Project> data = newExamBean.getProject();
        for (int i = 0; i < data.size(); i++) {
            isFInish = data.get(i).isFinished();
            if (!isFInish) {
                break;
            }
        }
        return isFInish;
    }

    public static String getTokenMsg() {
        String token = SpUtils.getInstance(MyApplication.getInstance()).getString("token", "");
        String tokenHead = SpUtils.getInstance(MyApplication.getInstance()).getString("tokenHead", "");
        return tokenHead + token;
    }

    //pad绑定的组立线id
    public static String getGroupId() {
        return SpUtils.getInstance(MyApplication.getInstance()).getString("groupId", "");
    }

    //pad绑定的工位id
    public static String getStationId() {
        return SpUtils.getInstance(MyApplication.getInstance()).getString("stationId", "");
    }

    public static RequestBody getRequestBody(HashMap paramsMap) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(paramsMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        return requestBody;
    }

    public static RequestBody getRequestBody(List paramsMap) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(paramsMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        return requestBody;
    }


    public static List<String> getResult(String msgAnswer, String msg, String type) {
        ArrayList<String> list = new ArrayList<>();
        int strNum = Utils.getStrNum(msg, type);
//        Log.e("TAG", "答案==" + msgAnswer + "  问题==" + msg + "  分隔符==" + type + "   分隔符数量==" + strNum);
        if (msgAnswer == null) {
            for (int i = 0; i < strNum; i++) {
                list.add(type.equals("<fill>") ? "" : "false");
            }
        } else {
            String[] split = msgAnswer.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(split[i]);
            }
        }
        return list;
    }

    public static String FortListToString(List<String> result) {
        String content = "";
        for (int i = 0; i < result.size(); i++) {
            if (i == result.size() - 1) {
                content += result.get(i);
            } else {
                content += result.get(i) + ",";
            }
        }
        return content;
    }

    public static int getStrNum(String str1, String str2) {
        int count = 0;
        int i = 0;
        while (str1.indexOf(str2, i) >= 0) {
            count++;
            i = str1.indexOf(str2, i) + str2.length();
        }
        return count;
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        String PATTERN = "^[0-9\\-]{8,18}";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(phone);
        Log.d("phoneMatch", matcher.matches() + "");
        return matcher.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean notNullOrEmpty(String string) {
        return !("".equals(string) || string == null || string == "null");
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    // TODO:2016
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 设置顏色
     *
     * @param mContext
     * @param color
     * @return
     */
    public static int getColor(Context mContext, int color) {
        return ContextCompat.getColor(mContext, color);
    }

    /**
     * 验证手机号 是否为空 是否合法
     *
     * @param mobile
     * @return
     */
    public static boolean notNullMobile(String mobile) {

        if (!TextUtils.isEmpty(mobile) && isPhoneNum(mobile)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否合法
     *
     * @param mobile
     * @param code
     * @return
     */
    public static boolean isLegal(String mobile, String code) {
        if (notNullMobile(mobile) && code.length() == 4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 邮箱重置密码 验证输入的参数是否正确
     *
     * @param email
     * @param code
     * @param pwd
     * @param newPWd
     * @return
     */
    public static boolean isEmailLegal(String email, String code, String pwd, String newPWd) {
        if (isEmail(email) && code.length() == 4 && notNullOrEmpty(pwd) &&
                notNullOrEmpty(newPWd) && pwd.equals(newPWd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手机号重置密码 验证输入的参数是否正确
     *
     * @param mobile
     * @param code
     * @param pwd
     * @param newpwd
     * @return
     */
    public static boolean isMobileLegal(String mobile, String code, String pwd, String newpwd) {
        if (isPhoneNum(mobile) && code.length() == 4 && notNullOrEmpty(pwd) &&
                notNullOrEmpty(newpwd) && pwd.equals(newpwd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 邮箱重置
     *
     * @param mobile
     * @param code
     * @param email
     * @return
     */
    public static boolean isResetEmailLegal(String mobile, String code, String email) {
        if (isPhoneNum(mobile) && code.length() == 4 && isEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前程序的版本号
     *
     * @param context
     * @return
     * @throws Exception
     */

    public static String getVersionName(Context context) throws Exception {
        //获取packageManager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * 加密手机号
     *
     * @param mobile
     * @return
     */
    public static String encryptPhone(String mobile) {
        String temp1 = mobile.substring(0, 3);
        String temp2 = mobile.substring(9, 11);
        return temp1 + "******" + temp2;
    }

    /**
     * 判断超出一定字数字符串截取
     */
    public static String subString(int num, String str, String end) {
        if (str.length() >= num) {
            return str.substring(0, num).concat(end);
        }
        return str;
    }

    /**
     * 检测是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        String token = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).getString("token", "");
        return notNullOrEmpty(token);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String getFormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 动态设置在ScrollView中的ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        // 获取ListView对应的Adapter
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            // pre -condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = listAdapter.getGroupView(i, true, null, listView);
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem.getMeasuredHeight(); // 统计所有子项的总高度
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                View listchildItem = listAdapter.getChildView(i, j, false, null, listView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 获取在ScrollView中的ListView的高度
     *
     * @param listView
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        return params.height;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        return time + "";
    }

    /**
     * 从Sh
     *
     * @param context
     * @param SPName
     * @param key
     * @param value
     */
    public static void saveSP(Context context, String SPName, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        if (value instanceof String) {
            sp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String changeDateFormat(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || "null".equals(seconds)) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy.MM.dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String changeDateFormat(Long seconds, String format) {
        if (seconds == null || seconds <= 0) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy.MM.dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    //静态方法，便于作为工具类
    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将ms单位转换成HH:mm:ss
     */
    public static String changeMS2S(int old) {
        old = old / 1000;
        int h = old / 3600;
        int m = old / 60 - h * 60;
        int s = old - m * 60 - h * 3600;
        String hs = "";
        String ms = "";
        String ss = "";
        if (h < 10) {
            hs = "0" + h;
        }
        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }
        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = s + "";
        }
        if (h <= 0) {
            return ms + ":" + ss;
        }
        return hs + ":" + ms + ":" + ss;
    }

    /**
     * 将s单位转换成HH:mm:ss
     */
    public static String changeS(long old) {
        long h = old / 3600;
        long m = old / 60 - h * 60;
        long s = old - m * 60 - h * 3600;
        String hs = "";
        String ms = "";
        String ss = "";
        if (h < 10) {
            hs = "0" + h;
        }
        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }
        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = s + "";
        }
        if (h <= 0) {
            return ms + ":" + ss;
        }
        return hs + ":" + ms + ":" + ss;
    }


    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String saveDigit(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(1, RoundingMode.UP);
        //保留2位小数
        double f1 = bg.doubleValue();
        return f1 + "";
    }
}
