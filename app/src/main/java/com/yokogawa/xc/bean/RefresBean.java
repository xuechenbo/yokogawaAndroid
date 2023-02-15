package com.yokogawa.xc.bean;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class RefresBean implements Serializable {
    private String MS_CODE = "";    //型名
    private String TEST_CERT_SIGN = "";  //成绩表，  0不需要成绩表   其他代表有成绩表   “”
    private String DOC_LANG_TYPE = "";   //说明书，  0不需要说明书   其他代表有说明书
    private String TAG_NO_525 = "";      //TAG No.字段  “” 或“BLANK”表示空白，其他任何字符均代表它的内容
    private String ORD_INST_CONTECT1_A02 = "";   //显示器方向  HORIZONTAL,スイヘイ  ｽｲﾍｲ （水平）VERTICAL，スイチョク（垂直）
    private String ORD_INST_CONTECT1_Y65 = "";   //若读取值为数字，可以判定为组合品   若读取值是数字以外，可以判定为单品
    private String MODEL = "";   //型名前缀
    private String SERIAL_NO = "";   //计番 唯一
    private String START_NO = "";   //连番
    private String ORD_INST_CONTECT1_A01 = "";      //指定角度  90  180

    //拼接字段 LINKAGE
    private String ORDER_NO = "";
    private String ITEM_NO = "";
    private String COMP_NO = "";

    private String ITEM_NOTE = "";

    public String getITEM_NOTE() {
        return ITEM_NOTE;
    }

    public void setITEM_NOTE(String ITEM_NOTE) {
        this.ITEM_NOTE = ITEM_NOTE;
    }

    public String getLinkage() {
        return ORDER_NO + "-" + ITEM_NO + "-" + COMP_NO;
    }

    public String getORDER_NO() {
        return ORDER_NO == null ? "" : ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getITEM_NO() {
        return ITEM_NO == null ? "" : ITEM_NO;
    }

    public void setITEM_NO(String ITEM_NO) {
        this.ITEM_NO = ITEM_NO;
    }

    public String getCOMP_NO() {
        return COMP_NO == null ? "" : COMP_NO;
    }

    public void setCOMP_NO(String COMP_NO) {
        this.COMP_NO = COMP_NO;
    }


    public String getORD_INST_CONTECT1_A01() {
        return ORD_INST_CONTECT1_A01 == null ? "" : ORD_INST_CONTECT1_A01;
    }

    public void setORD_INST_CONTECT1_A01(String ORD_INST_CONTECT1_A01) {
        this.ORD_INST_CONTECT1_A01 = ORD_INST_CONTECT1_A01;
    }


    public String getSTART_NO() {
        return START_NO == null ? "" : START_NO;
    }

    public void setSTART_NO(String START_NO) {
        this.START_NO = START_NO;
    }


    public String getMODEL() {
        return MODEL == null ? "" : MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getSERIAL_NO() {
        return SERIAL_NO == null ? "" : SERIAL_NO;
    }

    public void setSERIAL_NO(String SERIAL_NO) {
        this.SERIAL_NO = SERIAL_NO;
    }


    public String getMS_CODE() {
        return MS_CODE == null ? "" : MS_CODE;
    }

    public void setMS_CODE(String MS_CODE) {
        this.MS_CODE = MS_CODE;
    }

    public String getTEST_CERT_SIGN() {
        return TEST_CERT_SIGN == null ? "" : TEST_CERT_SIGN;
    }

    public void setTEST_CERT_SIGN(String TEST_CERT_SIGN) {
        this.TEST_CERT_SIGN = TEST_CERT_SIGN;
    }

    public String getDOC_LANG_TYPE() {
        return DOC_LANG_TYPE == null ? "" : DOC_LANG_TYPE;
    }

    public void setDOC_LANG_TYPE(String DOC_LANG_TYPE) {
        this.DOC_LANG_TYPE = DOC_LANG_TYPE;
    }

    public String getTAG_NO_525() {
        return TAG_NO_525 == null ? "" : TAG_NO_525;
    }

    public void setTAG_NO_525(String TAG_NO_525) {
        this.TAG_NO_525 = TAG_NO_525;
    }

    public String getORD_INST_CONTECT1_A02() {
        return ORD_INST_CONTECT1_A02 == null ? "" : ORD_INST_CONTECT1_A02;
    }

    public void setORD_INST_CONTECT1_A02(String ORD_INST_CONTECT1_A02) {
        this.ORD_INST_CONTECT1_A02 = ORD_INST_CONTECT1_A02;
    }

    public String getORD_INST_CONTECT1_Y65() {
        return ORD_INST_CONTECT1_Y65 == null ? "" : ORD_INST_CONTECT1_Y65;
    }

    public void setORD_INST_CONTECT1_Y65(String ORD_INST_CONTECT1_Y65) {
        this.ORD_INST_CONTECT1_Y65 = ORD_INST_CONTECT1_Y65;
    }

    @Override
    public String toString() {
        return "{" +
                "MS_CODE='" + MS_CODE + '\'' +
                ", TEST_CERT_SIGN='" + TEST_CERT_SIGN + '\'' +
                ", DOC_LANG_TYPE='" + DOC_LANG_TYPE + '\'' +
                ", TAG_NO_525='" + TAG_NO_525 + '\'' +
                ", ORD_INST_CONTECT1_A02='" + ORD_INST_CONTECT1_A02 + '\'' +
                ", ORD_INST_CONTECT1_Y65='" + ORD_INST_CONTECT1_Y65 + '\'' +
                ", MODEL='" + MODEL + '\'' +
                ", SERIAL_NO='" + SERIAL_NO + '\'' +
                ", START_NO='" + START_NO + '\'' +
                ", ORD_INST_CONTECT1_A01='" + ORD_INST_CONTECT1_A01 + '\'' +
                ", ORDER_NO='" + ORDER_NO + '\'' +
                ", ITEM_NO='" + ITEM_NO + '\'' +
                ", COMP_NO='" + COMP_NO + '\'' +
                '}';
    }


    public void setHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
    }

}
