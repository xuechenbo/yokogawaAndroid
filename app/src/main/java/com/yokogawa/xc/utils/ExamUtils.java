package com.yokogawa.xc.utils;

import android.util.Log;

import com.yokogawa.xc.bean.RefresBean;

import java.text.BreakIterator;


public class ExamUtils {


    /**
     * @param modeRuleNumber 规则位数
     * @param remark         备注
     * @param modeName       型名
     * @return true 显示 false 不显示
     */
    public static boolean isShow(String modeRuleNumber, String remark, String modeName, RefresBean refresBean) {
        String modelHeader = ModelTypeUtils.getModelHeader(modeName);
        String[] arrayStrMode = ModelTypeUtils.getModeArray(modelHeader, modeName);
        Log.e("ExamUtils", "位置===" + modeRuleNumber + "   规则备注===" + remark);
        if (remark.contains("+")) {
            String[] ruleNum = modeRuleNumber.split("\\+");
            String[] ruleSplit = remark.split("\\+");
            boolean isShow = mListItem(ruleNum, ruleSplit, arrayStrMode, refresBean);
            Log.e("ExamUtils", "是否显示===" + isShow);
            return isShow;
        } else {
            if ("0".equals(modeRuleNumber)) {
                return RefresUtils.getIsVisible(refresBean, remark);
            } else {
                boolean singleItem = isSingleItem(modeRuleNumber, remark, arrayStrMode, refresBean);
                Log.e("ExamUtils", "是否显示===" + singleItem);
                return singleItem;
            }
        }
    }

    /**
     * @param ruleNum      位置数组
     * @param ruleSplit    备注数组
     * @param arrayStrMode 型名数组
     * @return
     */
    public static boolean mListItem(String[] ruleNum, String[] ruleSplit, String[] arrayStrMode, RefresBean refresBean) {
        boolean isAdd = true;
        boolean singleItem = true;
        for (int y = 0; y < ruleSplit.length; y++) {
            if (ruleNum.length != ruleSplit.length) {
                continue;
            }
            //指图书：
            if ("0".equals(ruleNum[y])) {
                singleItem = RefresUtils.getIsVisible(refresBean, ruleSplit[y]);
            } else {
                singleItem = isSingleItem(ruleNum[y], ruleSplit[y], arrayStrMode, refresBean);
            }
            if (!singleItem) {
                isAdd = false;
            }
        }
        return isAdd;
    }

    //7-DP～DT,JP～JT&JA&G0

    //TODO 最小单位    3-!000

    /**
     * @param modeRuleNumber
     * @param remark
     * @param array          型名数组
     * @return
     */
    public static boolean isSingleItem(String modeRuleNumber, String remark, String[] array, RefresBean refresBean) {
        String[] remarkArray = remark.split("-");
        if (remarkArray.length != 2) {
            return true;
        }
        try {
            int mPostion = Integer.parseInt(remarkArray[0].trim());
            if (array.length <= mPostion) {
                Log.e("ERROR", "error====型名规则超出位数，数组越界。型名大小=" + array.length + "   取值大小==" + mPostion);
                return true;
            }
            if (remarkArray.length == 2) {
                if (scopeSlash(remarkArray[1])) {
                    //TODO 特殊最后一位
                    return isEndTrueFlag(remarkArray[1], array[mPostion]);

                } else if (isScope(remarkArray[1])) {
                    //TODO 是否包含某个 规则串  ~       加上判断！*****
                    return isScopeFlag(remarkArray[1], array[mPostion]);

                } else if (remarkArray[1].contains("*")) {
                    //TODO 是否是 以某个字符开头     !****   !C*,A*,D*    结尾呢？
                    return isAsteriskFlag(remarkArray[1], array[mPostion]);

                } else if (isHasExcl(remarkArray[1])) {
                    //TODO 非 ！  nStr ====  a1,a2,a3
                    String nStr = replaceExcl(remarkArray[1]);
                    if (isHasComma(nStr)) {
                        //非  判断
                        return !getSingleType(array[mPostion], nStr);
                    } else {
                        return (remarkArray[0].equals(modeRuleNumber) && !array[mPostion].equals(nStr));
                    }
                } else if (isHasComma(remarkArray[1])) {
                    return getSingleType(array[mPostion], remarkArray[1]);
                } else if (remarkArray[0].equals(modeRuleNumber) && array[mPostion].equals(remarkArray[1])) {
                    //TODO 显示   3-000   10-/MC
                    return (remarkArray[0].equals(modeRuleNumber) && array[mPostion].equals(remarkArray[1]));
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        //TODO ***********
        return false;
    }

    //规则串  *
    public static boolean isAsteriskFlag(String str0, String strComp) {
        boolean isAdd = false;
        //去掉！
        String nStr = replaceExcl(str0);
        String[] arrayStr = getArrayStr(nStr);
        for (int i = 0; i < arrayStr.length; i++) {
            //开头是*   以某个字符串结尾    *AB
            boolean isStart = getNoEmptStr(arrayStr[i]).startsWith("*");
            String newStr = arrayStr[i].replace("*", "");
            if (isStart && getNoEmptStr(strComp).endsWith(newStr)) {
                isAdd = true;
                break;
            } else if (strComp.startsWith(newStr)) {
                isAdd = true;
                break;
            }
        }
        if (isHasExcl(str0)) {
            return !isAdd;
        } else {
            return isAdd;
        }
    }

    //规则串  ~
    private static boolean isScopeFlag(String str0, String strComp) {
        //是否是多个范围 ，分割
        if (isHasComma(str0)) {
            boolean isAdd = false;
            String[] arrayStr = getArrayStr(str0);
            for (int i = 0; i < arrayStr.length; i++) {
                //是否再范围
                if (getIsExist(strComp, arrayStr[i])) {
                    isAdd = true;
                    break;
                }
            }
            return isAdd;
        } else {
            return getIsExist(strComp, str0);
        }
    }

    // 规则串 /
    private static boolean isEndTrueFlag(String str0, String strComp) {
        if (isHasExcl(str0)) {
            // 第一位 是 !
            if (startWith(str0)) {
                //第一位是！     10-!/VE,/VR,/CH,/EC,/U*
                String nStr = replaceExcl(str0);
                Log.e("TAG", "isSingleItem====" + str0);
                return !getSingleTypeSlash(strComp, nStr);
            } else {
                //中间有！  10-/VE,/VR,!/CH,/EC
                String[] hasExclArray = hasExclArray(str0);
                boolean singleTypeSlash = getSingleTypeSlash(strComp, hasExclArray[0]);
                //不包含
                boolean singleTypeSlash1 = getSingleTypeSlash(strComp, hasExclArray[1]);

                if (singleTypeSlash && !singleTypeSlash1) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return getSingleTypeSlash(strComp, str0);
        }
    }

    /**
     * 判断A1 是否在M2到M7之间，或等于M0
     * 肯定有~
     * <p>
     * 7-DP～DT,JP～JT&JA&G0
     */
    public static boolean getIsExist(String str, String totalStr) {
        boolean isScope = false;
        if (totalStr.contains("&")) {
            //有单个  判断,
            String[] djSplite = totalStr.split("&");
            for (int i = 0; i < djSplite.length; i++) {
                if (isScope(djSplite[i])) {
                    if (getScopeType(str, djSplite[0])) {
                        isScope = true;
                        break;
                    }
                } else {
                    if (str.equals(djSplite[i])) {
                        isScope = true;
                        break;
                    }
                }
            }
            return isScope;
        } else {
            //TODO
            if (isScope(totalStr)) {
                return getScopeType(str, totalStr);
            } else {
                return str.equals(totalStr);
            }
        }
    }


    //TODO 判断str 是否在totalStr里
    public static boolean getSingleType(String str, String totalStr) {
        boolean singType = false;
        String[] arrayStr = getArrayStr(getNoEmptStr(totalStr));
        for (int i = 0; i < arrayStr.length; i++) {
            if (arrayStr[i].equals(str)) {
                singType = true;
                break;
            }
        }
        return singType;
    }


    /**
     * 型名内容中是否包含备注规则内容  备注内容，分割
     * eg:   是否显示：   15-/KF2/X1,/SF2,/GF1,/NF2,/PF2/KF8    15位包含其中任何一个
     * 型名 15= /X1/KF2     return true;
     *
     * @param str      型名内容
     * @param totalStr 备注规则内容
     * @return true
     */
    public static boolean getSingleTypeSlash(String str, String totalStr) {
        Log.e("ExamUtils", "型名内容==" + str + "     备注规则内容==" + totalStr);
        boolean singType = false;
        if (isHasComma(totalStr)) {
            String[] arrayStr = getArrayStr(totalStr);
            for (int i = 0; i < arrayStr.length; i++) {
                if (isContains(str, arrayStr[i])) {
                    singType = true;
                    break;
                }
            }
            return singType;
        } else {
            return isContains(str, totalStr);
        }
    }


    /**
     * 第二层过滤
     * eg:   是否显示：   totalStr==/KF2/X1
     * 型名 str=/X1/KF2
     *
     * @param str      型名内容
     * @param totalStr 规则内容
     * @return
     */
    public static boolean isContains(String str, String totalStr) {
        boolean singType = true;
        if ("/".equals(totalStr)) {
            return "/".equals(str);
        }
        String[] splitRemark = totalStr.split("/");
        if ("/".equals(str.trim())) {
            if (str.equals(totalStr)) {
                return true;
            } else {
                return false;
            }
        }
        for (int i = 0; i < splitRemark.length; i++) {
            if (splitRemark[i].isEmpty()) {
                continue;
            }
            if (!isSmallContains(str, splitRemark[i])) {
                singType = false;
                break;
            }
        }
        return singType;
    }


    /**
     * 最小包含
     * 规则内容  totalStr = X1
     * 型名内容  str= /X1/KF2
     *
     * @param str      型名内容
     * @param totalStr 规则内容     A || A*
     * @return
     */
    public static boolean isSmallContains(String str, String totalStr) {
        Log.e("ExamUtils", "型名内容==" + str + "     备注规则内容 最小单位==" + totalStr);
        boolean singType = false;
        String[] arrayStr = str.split("/");
        for (int i = 0; i < arrayStr.length; i++) {
            if (i == 0 && arrayStr[0].isEmpty()) {
                continue;
            }
            if (totalStr.contains("*")) {
                String newStr = totalStr.replace("*", "");
                if (totalStr.startsWith("*") && arrayStr[i].endsWith(newStr)) {
                    singType = true;
                    break;
                } else if (arrayStr[i].startsWith(newStr)) {
                    singType = true;
                    break;
                }
            } else {
                if (arrayStr[i].equals(totalStr)) {
                    singType = true;
                    break;
                }
            }
        }
        return singType;
    }


    //TODO 是否在一个范围  如果有多位数 要修改！！！！！！！！！！！！  eg: A05>A11

    /**
     * @param str      AA
     * @param totalStr AA~AJ
     * @return
     */
    public static boolean getScopeType(String str, String totalStr) {
        Log.e("TAG", "getScopeType: ===" + str + "     " + totalStr);
        String[] numSplite = scopeArray(totalStr);
        if (numSplite.length != 2) {
            return true;
        }
        if (str.trim().compareTo(numSplite[0]) >= 0 && str.trim().compareTo(numSplite[1]) <= 0) {
            return true;
        } else {
            return false;
        }
    }


    //TODO 判断str1在str的位置    return 0  有问题！！改

    /**
     * @param str  remark              b*+c*,a*             ！A,A
     * @param str1 型名内容             ba1
     * @return
     */
    public static int getLoaction(String str, String str1) throws Exception {
        if (str1.contains("/")) {
            return CheckEnd_(str, str1);
        } else {
            return CheckPostion(str, str1);
        }
    }


    public static int CheckEnd_(String str, String str1) {
        Log.e("TAG", "remarkEnd===" + str + "   strEnd==" + str1);
        String[] arrayStr;
        int num = -1;
        if (str.contains("-")) {
            String[] split = str.split("-");
            arrayStr = getArrayStr(split[1]);
        } else {
            arrayStr = getArrayStr(str);
        }
        for (int i = 0; i < arrayStr.length; i++) {
            if (isC(arrayStr[i].replace("/", ""), str1)) {
                num = i;
                break;
            }
        }
        return num;
    }

    public static boolean isC(String str, String content) {
        Log.e("TAG", "isC: ===" + str + "       " + content);
        boolean is = false;
        if ("/".equals(content) && str.isEmpty()) {
            return true;
        }
        String[] split = content.split("/");
        if (split.length == 0) {
            //没有最后一位
            if (isHasExcl(str)) {
                return true;
            } else {
                return false;
            }
        }
        for (int i = 0; i < split.length; i++) {
            if (split[i].isEmpty()) {
                continue;
            }
            if (isHasExcl(str)) {
                if (!replaceExcl(str).equals(split[i].trim())) {
                    is = true;
                    break;
                }
            } else {
                if (str.equals(split[i].trim())) {
                    is = true;
                    break;
                }
            }
        }
        return is;
    }


    public static int CheckPostion(String str, String str1) {
        Log.e("TAG", "remark===" + str + "   str==" + str1);
        String[] arrayStr;
        int num = -1;
        if (str.contains("-")) {
            String[] split = str.split("-");
            arrayStr = getArrayStr(split[1]);
        } else {
            arrayStr = getArrayStr(str);
        }
        for (int i = 0; i < arrayStr.length; i++) {
            if (arrayStr[i].contains("*")) {
                //TODO 特殊情况  以B，C开头， A 开头的
                String replace = arrayStr[i].replace("*", "");
                String[] arrayAddStr = getArrayAddStr(replace);
                for (int add = 0; add < arrayAddStr.length; add++) {
                    if (str1.startsWith(arrayAddStr[add])) {
                        num = i;
                        break;
                    }
                }
            } else {
                if (isHasExcl(arrayStr[i])) {
                    String newStr = replaceExcl(arrayStr[i]);
                    if (!newStr.equals(str1)) {
                        Log.e("TAG!", "CheckPostion: ====" + i);
                        num = i;
                        break;
                    }
                } else {
                    if (arrayStr[i].equals(str1)) {
                        Log.e("TAG", "CheckPostion: ====" + i);
                        num = i;
                        break;
                    }
                }
            }
        }
        if (num == -1) {
            for (int i = 0; i < arrayStr.length; i++) {
                if (str1.contains(arrayStr[i])) {
                    num = i;
                    break;
                }
            }
        }
        return num;
    }


    public static String[] getArrayStr(String checkType) {
        String[] split;
        if (checkType.contains(",")) {
            split = checkType.split(",");
        } else {
            split = checkType.split("，");
        }
        return split;
    }

    //按+分割
    public static String[] getArrayAddStr(String addStr) {
        String[] split = addStr.split("\\+");
        return split;
    }

    //是否包含，,
    public static boolean isHasComma(String str) {
        return str.contains("，") || str.contains(",");
    }


    //是否包含~   ～
    public static boolean isScope(String str) {
        return str.contains("~") || str.contains("～");
    }

    //是否包含/
    public static boolean scopeSlash(String str) {
        return str.contains("/");
    }

    // ~   ～ 分割数组
    public static String[] scopeArray(String str) {
        String[] split;
        if (str.contains("~")) {
            split = str.split("~");
        } else {
            split = str.split("～");
        }
        return split;
    }

    // ！ ! 分割数组
    public static String[] hasExclArray(String str) {
        String[] split;
        if (str.contains("！")) {
            split = str.split("！");
        } else {
            split = str.split("!");
        }
        return split;
    }

    //是否包含！ !
    public static boolean isHasExcl(String str) {
        return str.contains("！") || str.contains("!");
    }

    public static String replaceExcl(String str) {
        if (str.contains("!")) {
            return str.replace("!", "").trim();
        } else {
            return str.replace("！", "").trim();
        }
    }

    public static boolean startWith(String str) {
        if (str.startsWith("!") || str.startsWith("！")) {
            return true;
        } else {
            return false;
        }
    }


    public static String[] scopeArray_(String str) {
        String[] split;
        if (str.contains("-")) {
            split = str.split("-");
        } else {
            split = str.split("—");
        }
        return split;
    }
    public static String[] brackArray_(String str) {
        String[] split;
        if (str.contains("(")) {
            split = str.split("\\(");
        } else {
            split = str.split("（");
        }
        return split;
    }


    //— -
    public static String getStrContent(String string) {
        String replace;
        if (string.contains("-")) {
            replace = string.replace("-", "");
        } else {
            replace = string.replace("—", "");
        }
        String empStr = replace.replace(" ", "");
        return empStr;
    }


    //去掉所有空格
    public static String getNoEmptStr(String modeName) {
        return modeName.replace(" ", "").trim();
    }

    //切割字符串  是否包含Z
    public static boolean ishasZshow(String remark, String modeName) {
        boolean isZ = false;
        String[] arrayStr = getArrayStr(remark);
        String[] arrayStrMode = ModelTypeUtils.getModeArray(ModelTypeUtils.getModelHeader(modeName), modeName);
        for (int i = 0; i < arrayStrMode.length; i++) {
            if ((i < Integer.parseInt(arrayStr[0]))) {
                continue;
            }
            if (arrayStrMode[i].contains("Z")) {
                isZ = true;
                break;
            }
        }
        Log.e("TAG", "ishasZshow===" + isZ);
        return isZ;
    }


    //是否包含某个字符串  去掉全部空格

    /**
     * sourceStr 是否包含 CompareStr
     *
     * @param sourceStr  源数据
     * @param compareStr 要包含的数据
     * @return
     */
    public static boolean isContainStr(String sourceStr, String compareStr) {
        return getNoEmptStr(sourceStr).contains(getNoEmptStr(compareStr));
    }


    //过滤空格   双空格"  " 和 三空格"   "  替换为 " "
    public static String getoneSpaceStr(String str) {
        return str.trim().replace("    ", " ").replace("   ", " ").replace("  ", " ");
    }


    //规则判断是否包含
    public static boolean isExamContaninFlag1(String sourceStr, String compareStr) {
        String[] sourceArray = getoneSpaceStr(sourceStr).split(" ");
        String[] comparArray = getoneSpaceStr(compareStr).split(" ");
        boolean isNgFlag = true;
        if (sourceArray.length != comparArray.length) {
            return false;
        }
        for (int i = 0; i < sourceArray.length; i++) {
            if (!isContainStr(sourceArray[i], comparArray[i])) {
                isNgFlag = false;
                break;
            }
        }
        return isNgFlag;
    }


    /**
     * DN15 PN40 F304 EN 123455    DN PN40 F304(SUFS304) EN
     *
     * @param sourceStr  输入内容
     * @param compareStr 被比较内容
     * @return
     */
    public static boolean isExamContaninFlag(String sourceStr, String compareStr) {
        if (isContainStr(sourceStr, compareStr)) {
            return true;
        }
        String[] sourceArray = getoneSpaceStr(sourceStr).split(" ");
        String[] comparArray = getoneSpaceStr(compareStr).split(" ");
        int isConstNum = 0;
        if (sourceArray.length < comparArray.length) {
            return false;
        }
        for (int j = 0; j < comparArray.length; j++) {
            for (int i = 0; i < sourceArray.length; i++) {
                if (isConBrack(comparArray[j])) {
                    boolean splitCon = isSplitCon(sourceArray[i], comparArray[j]);
                    if (splitCon) {
                        isConstNum++;
                        break;
                    }

                } else if (sourceArray[i].contains(comparArray[j])) {
                    isConstNum++;
                    break;
                }
            }
        }
        Log.e("TAG", "isExamContaninFlag: ====" + isConstNum);
        return isConstNum == comparArray.length;
    }


    public static boolean isSplitCon(String sourceStr, String comparStr) {
        boolean isFlag = false;
        String[] split = brackArray_(delBrackets(comparStr));
        for (int k = 0; k < split.length; k++) {
            if (sourceStr.contains(split[k])) {
                isFlag = true;
                break;
            }
        }
        return isFlag;
    }

    public static String delBrackets(String str) {
        return str.replace(")", "").replace("）", "");
    }


    public static boolean isConBrack(String str) {
        return (str.contains("(") && str.contains(")")) || (str.contains("（") && str.contains("）"));
    }


}
