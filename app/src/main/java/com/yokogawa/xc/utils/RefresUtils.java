package com.yokogawa.xc.utils;

import android.util.Log;

import com.yokogawa.xc.bean.RefresBean;

import java.util.regex.Pattern;

public class RefresUtils {
    /**
     * 变换器是单品或组合品的字段$ORD_INST_CONTECT1_Y65
     * 若读取值为数字，可以判定为组合品
     * 若读取值是数字以外，可以判定为单品
     */
    public static RefresBean toBean(String strContent) {
        RefresBean refresBean = new RefresBean();
        if (strContent == null || strContent.isEmpty() || strContent.equals("null")) {
            return refresBean;
        }
        String[] split = strContent.split("\\$");
        for (int i = 0; i < split.length; i++) {
            String mPostStr = "$" + split[i];
            Log.e("TAG", "mPostStr====" + mPostStr);
            if (mPostStr.startsWith("$MS_CODE") && refresBean.getMS_CODE().isEmpty()) {
                refresBean.setMS_CODE(getReplString(mPostStr, "$MS_CODE"));
            } else if (mPostStr.startsWith("$TEST_CERT_SIGN") && refresBean.getTEST_CERT_SIGN().isEmpty()) {
                refresBean.setTEST_CERT_SIGN(getReplString(mPostStr, "$TEST_CERT_SIGN"));
            } else if (mPostStr.startsWith("$DOC_LANG_TYPE") && refresBean.getDOC_LANG_TYPE().isEmpty()) {
                refresBean.setDOC_LANG_TYPE(getReplString(mPostStr, "$DOC_LANG_TYPE"));
            } else if (mPostStr.startsWith("$TAG_NO_525") && refresBean.getTAG_NO_525().isEmpty()) {
                refresBean.setTAG_NO_525(getReplString(mPostStr, "$TAG_NO_525"));
            } else if (mPostStr.startsWith("$ORD_INST_CONTECT1_A02") && refresBean.getORD_INST_CONTECT1_A02().isEmpty()) {
                refresBean.setORD_INST_CONTECT1_A02(getReplString(mPostStr, "$ORD_INST_CONTECT1_A02"));
            } else if (mPostStr.startsWith("$ORD_INST_CONTECT1_Y65") && refresBean.getORD_INST_CONTECT1_Y65().isEmpty()) {
                refresBean.setORD_INST_CONTECT1_Y65(getReplString(mPostStr, "$ORD_INST_CONTECT1_Y65"));
            } else if (mPostStr.startsWith("$SERIAL_NO") && refresBean.getSERIAL_NO().isEmpty()) {
                refresBean.setSERIAL_NO(getReplString(mPostStr, "$SERIAL_NO"));
            } else if (mPostStr.startsWith("$MODEL") && refresBean.getMODEL().isEmpty()) {
                refresBean.setMODEL(getReplString(mPostStr, "$MODEL"));
            } else if (mPostStr.startsWith("$START_NO") && refresBean.getSTART_NO().isEmpty()) {
                refresBean.setSTART_NO(getReplString(mPostStr, "$START_NO"));
            } else if (mPostStr.startsWith("$ORD_INST_CONTECT1_A01") && refresBean.getORD_INST_CONTECT1_A01().isEmpty()) {
                refresBean.setORD_INST_CONTECT1_A01(getReplString(mPostStr, "$ORD_INST_CONTECT1_A01"));
            } else if (mPostStr.startsWith("$ORDER_NO") && refresBean.getORDER_NO().isEmpty()) {
                refresBean.setORDER_NO(getReplString(mPostStr, "$ORDER_NO"));
            } else if (mPostStr.startsWith("$ITEM_NOTE") && refresBean.getITEM_NOTE().isEmpty()) {
                refresBean.setITEM_NOTE(getReplString(mPostStr, "$ITEM_NOTE"));
            } else if (mPostStr.startsWith("$ITEM_NO") && refresBean.getITEM_NO().isEmpty()) {
                refresBean.setITEM_NO(getReplString(mPostStr, "$ITEM_NO"));
            } else if (mPostStr.startsWith("$COMP_NO") && refresBean.getCOMP_NO().isEmpty()) {
                refresBean.setCOMP_NO(getReplString(mPostStr, "$COMP_NO"));
            }
        }
        Log.e("TAG", "toBean====" + refresBean.toString());
        return refresBean;
    }


    public static String getReplString(String str, String reStr) {
        return str.replace(reStr, "");
    }


    //指图书是否显示
    public static boolean getIsVisible(RefresBean refresBean, String key) {
        if (refresBean == null) {
            return false;
        }
        String[] str_Array = ExamUtils.scopeArray_(key);
        switch (str_Array[0]) {
            case "ORD_INST_CONTECT1_Y65":
                String y65 = refresBean.getORD_INST_CONTECT1_Y65();
                //-分割
                if (str_Array[1].equals("!0")) {
                    //单品显示
                    return Utils.isSingleProduct(y65);
                } else {
                    //其他
                    return !Utils.isSingleProduct(y65);
                }
            case "ORD_INST_CONTECT1_A02":
                String ord_inst_contect1_a02 = refresBean.getORD_INST_CONTECT1_A02();
                if (ord_inst_contect1_a02.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            case "ORD_INST_CONTECT1_A01":
                String ord_inst_contect1_a01 = refresBean.getORD_INST_CONTECT1_A01();
                if (ord_inst_contect1_a01.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            case "TAG_NO_525":
                String tag_no_525 = refresBean.getTAG_NO_525();
                if (str_Array.length >= 2) {
                    if ("\"\"".equals(str_Array[1])) {
                        if (tag_no_525.equals("BLANK") || tag_no_525.isEmpty()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if ("!\"\"".equals(str_Array[1])) {
                        if (!tag_no_525.isEmpty() && !tag_no_525.equals("BLANK")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    if (tag_no_525.isEmpty() || tag_no_525.equals("BLANK")) {
                        return false;
                    } else {
                        return true;
                    }
                }
            case "TEST_CERT_SIGN":
                String test_cert_sign = refresBean.getTEST_CERT_SIGN();
                String newTestSing = ExamUtils.replaceExcl(str_Array[1]);
                boolean hasExcl = ExamUtils.isHasExcl(str_Array[1]);
                if (newTestSing.isEmpty() && test_cert_sign.equals("\"\"")) {
                    return true;
                } else if (newTestSing.equals(test_cert_sign)) {
                    return hasExcl ? false : true;
                } else {
                    return hasExcl ? true : false;
                }
            default:
                return false;
        }
    }

    //指图书多选一
    public static int getCheckPosition(RefresBean refresBean, String key) {
        if (refresBean == null) {
            return -1;
        }
        String[] str_Array = ExamUtils.scopeArray_(key);
        switch (str_Array[0]) {
            case "ORD_INST_CONTECT1_A02":
                String ord_inst_contect1_a02 = refresBean.getORD_INST_CONTECT1_A02();
                if (ord_inst_contect1_a02.isEmpty()) {
                    return -1;
                } else if (ord_inst_contect1_a02.equals("HORIZONTAL") || ord_inst_contect1_a02.equals("スイヘイ") || ord_inst_contect1_a02.equals("ｽｲﾍｲ")) {
                    return 0;
                } else {
                    return 1;
                }
            case "ORD_INST_CONTECT1_A01":
                String ord_inst_contect1_a01 = refresBean.getORD_INST_CONTECT1_A01();
                if (ord_inst_contect1_a01.isEmpty()) {
                    return -1;
                } else if (ord_inst_contect1_a01.contains("+180")) {
                    return 1;
                } else if (ord_inst_contect1_a01.contains("+90")) {
                    return 0;
                } else if (ord_inst_contect1_a01.contains("-90")) {
                    return 2;
                }
            case "TAG_NO_525":
                String tag_no_525 = refresBean.getTAG_NO_525();
                if (tag_no_525.isEmpty() || tag_no_525.equals("BLANK") || tag_no_525.equals("null")) {
                    //无
                    return 0;
                } else {
                    //有
                    return 1;
                }
            default:
                return -1;
        }
    }

    //获取指图书内容
    public static String getRefresContent(RefresBean refresBean, String key) {
        if (refresBean == null) {
            return "";
        }
        String[] str_Array = ExamUtils.scopeArray_(key);
        switch (str_Array[0]) {
            case "TAG_NO_525":
                return refresBean.getTAG_NO_525();
            case "START_NO":
                return refresBean.getSTART_NO();
            case "SERIAL_NO":
                return refresBean.getSERIAL_NO();
            default:
                return "";
        }
    }
}
