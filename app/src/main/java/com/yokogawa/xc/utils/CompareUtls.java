package com.yokogawa.xc.utils;

import android.util.Log;

public class CompareUtls {
    public static boolean isNgFlag(String remark, String trNum, String checkType) {
        if (checkType.contains("是否包含")) {
            return ExamUtils.isExamContaninFlag(trNum, remark);
        } else if (ExamUtils.isScope(remark)) {
            //包含~判断范围
            String[] strings = ExamUtils.scopeArray(remark);
            String startNum = strings[0];
            String endNum = strings[1];
            if (Utils.isNumber(trNum) && Utils.isNumber(startNum.trim()) && Utils.isNumber(endNum.trim())) {
                Log.e("TAG", "包含~判断范围" + startNum + "~" + endNum);
                double v = Double.parseDouble(trNum);
                double n_startNum = Double.parseDouble(startNum);
                double n_endNum = Double.parseDouble(endNum);
                if (v >= n_startNum && v <= n_endNum) {
                    return true;
                } else {
                    return false;
                }
            } else {
                T.show("请输入数字");
                return false;
            }
        } else if (remark.contains("以上")) {
            String newStr = remark.replace("以上", "");
            if (Utils.isNumber(trNum) && Utils.isNumber(newStr.trim())) {
                double v = Double.parseDouble(trNum);
                double n_startNum = Double.parseDouble(newStr);
                if (v >= n_startNum) {
                    return true;
                } else {
                    return false;
                }
            } else {
                T.show("请输入数字");
                return false;
            }
        } else if (remark.contains("大于")) {
            String newStr = remark.replace("大于", "");
            if (Utils.isNumber(trNum) && Utils.isNumber(newStr.trim())) {
                double v = Double.parseDouble(trNum);
                double n_startNum = Double.parseDouble(newStr);
                if (v > n_startNum) {
                    return true;
                } else {
                    return false;
                }
            } else {
                T.show("请输入数字");
                return false;
            }

        } else {
            //值比较
            if (remark.trim().equals(trNum.trim())) {
                Log.e("", "else值比较" + remark.equals(trNum));
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 数据比较
     * @param resultNum
     * @param startNum
     * @param endNum
     * @return
     */
    public static boolean compareTO(String resultNum, String startNum, String endNum) {

        return true;
    }

}
