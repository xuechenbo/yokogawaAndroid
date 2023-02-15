package com.yokogawa.xc.utils;

import android.util.Log;

/**
 * 型名解析
 */
public class ModelParseUtils {
    /*****************************变换器****************************/

    //TODO 1=====AXG4AG000101JA12/CH 或者 AXW4A-G000101JA12/CH 或者  AXG1AG000101JA12/CH
    public static String[] getAXG4aStr(String modelStr) {
        StringBuffer stringBuffer = new StringBuffer("1-");
        String strContent = ExamUtils.getStrContent(modelStr);
        if (strContent.length() <= 15) {
            Log.e("TAG", "getAXG4aStr: 拉了");
            return new String[0];
        }
        try {
            stringBuffer.append(strContent.substring(0, 5) + "-");
            stringBuffer.append(strContent.substring(5, 6) + "-");
            stringBuffer.append(strContent.substring(6, 9) + "-");
            stringBuffer.append(strContent.substring(9, 10) + "-");
            stringBuffer.append(strContent.substring(10, 11) + "-");
            stringBuffer.append(strContent.substring(11, 12) + "-");
            stringBuffer.append(strContent.substring(12, 14) + "-");
            stringBuffer.append(strContent.substring(14, 15) + "-");
            stringBuffer.append(strContent.substring(15, 16) + "-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strContent.substring(16).isEmpty()) {
            stringBuffer.append("/");
        } else {
            stringBuffer.append(strContent.substring(16));
        }
        return stringBuffer.toString().split("-");
    }


    //TODO SF0 100 GA000AA1BL110A1JA12/CH
    public static String[] getSF0Str(String modelStr) {
        String substring = "";
        StringBuffer stringBuffer = new StringBuffer("1-");
        String newStr = ExamUtils.getStrContent(modelStr);
        try {
            stringBuffer.append(newStr.substring(0, 3) + "-");
            stringBuffer.append(newStr.substring(3, 6) + "-");
            stringBuffer.append(newStr.substring(6, 7) + "-");
            stringBuffer.append(newStr.substring(7, 8) + "-");
            stringBuffer.append(newStr.substring(8, 11) + "-");
            //TODO 如果第五位是Z取一位
            if (newStr.substring(11, 12).equals("Z")) {
                stringBuffer.append(newStr.substring(11, 12) + "-");
                substring = newStr.substring(12);
            } else {
                stringBuffer.append(newStr.substring(11, 14) + "-");
                substring = newStr.substring(14);
            }
            if (substring.length() <= 10) {
                Log.e("TAG", "getAXG4aStr: 拉了");
                return new String[0];
            }
            stringBuffer.append(substring.substring(0, 1) + "-");
            stringBuffer.append(substring.substring(1, 2) + "-");
            stringBuffer.append(substring.substring(2, 3) + "-");
            stringBuffer.append(substring.substring(3, 4) + "-");
            stringBuffer.append(substring.substring(4, 5) + "-");
            stringBuffer.append(substring.substring(5, 6) + "-");
            stringBuffer.append(substring.substring(6, 7) + "-");
            stringBuffer.append(substring.substring(7, 9) + "-");
            stringBuffer.append(substring.substring(9, 10) + "-");
            stringBuffer.append(substring.substring(10, 11) + "-");
            if (substring.substring(11).isEmpty()) {
                stringBuffer.append("/");
            } else {
                stringBuffer.append(substring.substring(11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString().split("-");
    }
    /*****************************小口径****************************/

    /**
     * 第五位为Z
     *
     * @param modelStr TODO 2======AXG002-GA000BE4AL114B-2JA12/CH
     */
    public static String[] getAXgStr(String modelStr) {
        StringBuffer stringBuffer = new StringBuffer("1-");
        String substring = "";
        String newStr = ExamUtils.getStrContent(modelStr);

        try {
            stringBuffer.append(newStr.substring(0, 3) + "-");
            stringBuffer.append(newStr.substring(3, 6) + "-");
            stringBuffer.append(newStr.substring(6, 7) + "-");
            stringBuffer.append(newStr.substring(7, 8) + "-");
            stringBuffer.append(newStr.substring(8, 11) + "-");
            //TODO 如果第五位是Z取一位
            if (newStr.substring(11, 12).equals("Z")) {
                stringBuffer.append(newStr.substring(11, 12) + "-");
                substring = newStr.substring(12);
            } else {
                stringBuffer.append(newStr.substring(11, 14) + "-");
                substring = newStr.substring(14);
            }
            if (substring.length() <= 10) {
                Log.e("TAG", "getAXG4aStr: 拉了");
                return new String[0];
            }
            stringBuffer.append(substring.substring(0, 1) + "-");
            stringBuffer.append(substring.substring(1, 2) + "-");
            stringBuffer.append(substring.substring(2, 3) + "-");
            stringBuffer.append(substring.substring(3, 4) + "-");
            stringBuffer.append(substring.substring(4, 5) + "-");
            stringBuffer.append(substring.substring(5, 6) + "-");
            stringBuffer.append(substring.substring(6, 7) + "-");
            stringBuffer.append(substring.substring(7, 9) + "-");
            stringBuffer.append(substring.substring(9, 10) + "-");
            stringBuffer.append(substring.substring(10, 11) + "-");
            if (substring.substring(11).isEmpty()) {
                stringBuffer.append("/");
            } else {
                stringBuffer.append(substring.substring(11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("TAG", "getAXgStr: ===" + stringBuffer.toString());
        return stringBuffer.toString().split("-");
    }


    /*****************************涡流****************************/

    /**
     * @param modelStr DY型名解析
     * @return
     */
    public static String[] getDYStr(String modelStr) {
        StringBuffer stringBuffer = new StringBuffer("1-");
        String newStr = ExamUtils.getStrContent(modelStr);
        try {
            stringBuffer.append(newStr.substring(0, 2) + "-");
            String strContent = newStr.substring(2);
            if (strContent.length() <= 10) {
                return new String[0];
            }
            stringBuffer.append(strContent.substring(0, 3) + "-");
            stringBuffer.append(strContent.substring(3, 4) + "-");
            stringBuffer.append(strContent.substring(4, 5) + "-");
            stringBuffer.append(strContent.substring(5, 6) + "-");
            stringBuffer.append(strContent.substring(6, 9) + "-");
            stringBuffer.append(strContent.substring(9, 10) + "-");
            stringBuffer.append(strContent.substring(10, 11) + "-");
            if (strContent.substring(11).isEmpty()) {
                stringBuffer.append("/");
            } else {
                stringBuffer.append(strContent.substring(11));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuffer.toString().split("-");
    }

    /**
     * @param modelStr DYA型名解析
     * @return
     */
    public static String[] getDYAStr(String modelStr) {
        StringBuffer stringBuffer = new StringBuffer("1-");
        String newStr = ExamUtils.getStrContent(modelStr);
        try {
            stringBuffer.append(newStr.substring(0, 3) + "-");
            String strContent = newStr.substring(3);
            if (strContent.length() <= 2) {
                return new String[0];
            }
            stringBuffer.append(strContent.substring(0, 1) + "-");
            stringBuffer.append(strContent.substring(1, 2) + "-");
            stringBuffer.append(strContent.substring(2, 3) + "-");
            if (strContent.substring(3).isEmpty()) {
                stringBuffer.append("/");
            } else {
                stringBuffer.append(strContent.substring(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString().split("-");
    }


    //小口径 AXR050G-J1AL1L-BA11-21B/CH
    public static String[] getAXRstr(String modelStr) {
        StringBuffer stringBuffer = new StringBuffer("1-");
        String substring = "";
        String newStr = ExamUtils.getStrContent(modelStr);
        try {
            stringBuffer.append(newStr.substring(0, 3) + "-");
            stringBuffer.append(newStr.substring(3, 6) + "-");
            stringBuffer.append(newStr.substring(6, 7) + "-");
            stringBuffer.append(newStr.substring(7, 8) + "-");
            stringBuffer.append(newStr.substring(8, 9) + "-");
            stringBuffer.append(newStr.substring(9, 10) + "-");
            stringBuffer.append(newStr.substring(10, 11) + "-");
            stringBuffer.append(newStr.substring(11, 12) + "-");
            stringBuffer.append(newStr.substring(12, 13) + "-");
            if (newStr.substring(13, 14).equals("Z")) {
                stringBuffer.append(newStr.substring(13, 14) + "-");
                substring = newStr.substring(14);
            } else {
                stringBuffer.append(newStr.substring(13, 16) + "-");
                substring = newStr.substring(16);
            }
            stringBuffer.append(substring.substring(0, 1) + "-");
            stringBuffer.append(substring.substring(1, 2) + "-");
            stringBuffer.append(substring.substring(2, 3) + "-");
            stringBuffer.append(substring.substring(3, 4) + "-");
            if (substring.substring(4).isEmpty()) {
                stringBuffer.append("/");
            } else {
                stringBuffer.append(substring.substring(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString().split("-");
    }


}
