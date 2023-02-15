package com.yokogawa.xc.utils;


import android.util.Log;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.bean.NewExamBean;

/**
 * 判断型名头部
 */
public class ModelTypeUtils {
    //TODO 获取型名头部
    public static String getModelHeader(String modelStr) {
        //AXG4A AXGW4A  AXG1A AXG  AXW   DY  DYA
        if (modelStr == null || modelStr.length() < 5) {
            return modelStr;
        }
        String strSecond = modelStr.substring(0, 2);
        String strThird = modelStr.substring(0, 3);
        String strFive = modelStr.substring(0, 5);
        if (strFive.equals("AXG4A") || strFive.equals("AXW4A") || strFive.equals("AXG1A") || strFive.equals("SF14A")) {
            return strFive;
        } else if (strThird.equals("AXG") || strThird.equals("AXW") || strThird.equals("DYA") ||
                strThird.equals("SF0") || strThird.equals("AXR") || strThird.equals("CA0")) {
            if (strThird.equals("SF0")) {
                return strSecond;
            }
            return strThird;
        } else if (strSecond.equals("DY") || "SF".equals(strSecond)) {
            return strSecond;
        } else {
            return "";
        }
    }


    //TODO 根据型名头部  获取型名数组
    public static String[] getModeArray(String modeHeader, String modeName) {
        if (modeName == null) {
            return new String[0];
        }
        if (modeHeader.equals("AXG4A") || modeHeader.equals("AXW4A") || modeHeader.equals("AXG1A") || modeHeader.equals("SF14A")) {
            return ModelParseUtils.getAXG4aStr(modeName);
        } else if (modeHeader.equals("AXG") || modeHeader.equals("AXW") || modeHeader.equals("CA0")) {
            return ModelParseUtils.getAXgStr(modeName);
        } else if (modeHeader.equals("DYA")) {
            return ModelParseUtils.getDYAStr(modeName);
        } else if (modeHeader.equals("AXR")) {
            return ModelParseUtils.getAXRstr(modeName);
        } else if (modeHeader.equals("SF0")) {
            return ModelParseUtils.getSF0Str(modeName);
        } else if (modeHeader.equals("DY")) {
            return ModelParseUtils.getDYStr(modeName);
        } else if (modeHeader.equals("SF")) {
            return ModelParseUtils.getSF0Str(modeName);
        } else {
            return new String[0];
        }
    }


    //TODO 组合特殊 规则查询

    /**
     * @param modeName  型名
     * @param rulNumber eg:3/9   ,    3
     * @return 从型名中取相应位置的内容  组合成  002/BE4
     */
    public static String getCombinationStr(String modeName, String rulNumber) {
        Log.e("getCombinationStr", "型名==" + modeName + " 规则==" + rulNumber);
        StringBuffer stringBuffer = new StringBuffer();
        String modelHeader = ModelTypeUtils.getModelHeader(modeName);
        String[] arrayStrMode = ModelTypeUtils.getModeArray(modelHeader, modeName);
        //型名==DY100-EBLBA1-0D/SCT   规则==3/9
        if (rulNumber.contains("/")) {
            String[] split = rulNumber.split("/");
            for (int i = 0; i < split.length; i++) {
                int mPostion = Integer.parseInt(split[i]);
                String replace = arrayStrMode[mPostion];
                if (i == split.length - 1) {
                    stringBuffer.append(replace);
                } else {
                    stringBuffer.append(replace + "_");
                }
            }
        } else {
            if (Utils.isNumber(rulNumber)) {
                int mPostion = Integer.parseInt(rulNumber);
                String replace = arrayStrMode[mPostion];
                stringBuffer.append(replace);
            }
        }
        return stringBuffer.toString().endsWith("_/") ? stringBuffer.toString().replace("_/", "") : stringBuffer.toString();
    }


    //小口径，组立7 耐水压实验特殊 规则
    public static String getConStr(String modeName, NewExamBean.Project.Check.Children children1, String[] ruleStr) {
        String modelHeader = ModelTypeUtils.getModelHeader(modeName);
        String[] arrayStrMode = ModelTypeUtils.getModeArray(modelHeader, modeName);
        //TODO 耐水压实验 AXG小口径 组立7特殊规则 单独处理
        //17-   !/T01,/TS1,/TS2,/CS  +    6-!A*
        String strings = "";
        if (children1.getRemark().contains("+")) {
            String[] split = children1.getRemark().split("\\+");
            strings = split[0].split("-")[1];
        } else {
            String[] split = children1.getRemark().split("-");
            strings = split[1];
        }
        StringBuffer stringBuffer = new StringBuffer(strings);
        String conStr = ruleStr[1];
        String[] split1 = conStr.split("/");
        for (int sp = 0; sp < split1.length; sp++) {
            if (sp == 0) continue;
            String num = split1[sp];
            stringBuffer.append("_" + arrayStrMode[Integer.parseInt(num)]);
        }
        return stringBuffer.toString();
    }


}
