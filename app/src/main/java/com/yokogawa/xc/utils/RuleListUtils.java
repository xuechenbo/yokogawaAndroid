package com.yokogawa.xc.utils;

import android.util.Log;

import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.bean.RefresBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleListUtils {
    public static ArrayList<NewExamBean.Project> getRuleMsg(ArrayList<NewExamBean.Project> list, RefresBean refresBean, String modeName) {
        ArrayList<NewExamBean.Project> mList = new ArrayList<>();
        //TODO 已经获取到这个组立线工位的所有规则
        String ruleMap = SpUtils.getInstance(MyApplication.getInstance()).getString("ruleMap", "");
        for (int k = 0; k < list.size(); k++) {
            NewExamBean.Project project = list.get(k);
            List<NewExamBean.Project.Check> check = project.getCheck();
            List<NewExamBean.Project.Check> newCheck = new ArrayList<>();
            for (int i = 0; i < check.size(); i++) {
                List<NewExamBean.Project.Check.Children> children = check.get(i).getChildren();
                List<NewExamBean.Project.Check.Children> newchildren = new ArrayList<>();
                for (int j = 0; j < children.size(); j++) {
                    NewExamBean.Project.Check.Children children1 = children.get(j);
                    String modeRuleNumber = children1.getModeRuleNumber();
                    String checkType = children1.getCheckType().trim();
                    //TODO 单个指图书   是否显示,     多选一     导入指图书
                    if ("0".equals(children1.getModeRuleNumber())) {
                        String remark = children1.getRemark();
                        if ("导入指图书".equals(checkType)) {
                            String refresContent = RefresUtils.getRefresContent(refresBean, remark);
                            children1.setRemark(refresContent);
                            children1.setShowValue(refresContent);
                            newchildren.add(children1);
                        } else if (checkType.equals("是否显示")) {
                            if (RefresUtils.getIsVisible(refresBean, remark)) {
                                newchildren.add(children1);
                            }
                        } else if (checkType.equals("多选一")) {
                            //二选一
                            children1.setRemark(RefresUtils.getCheckPosition(refresBean, remark) + "");
                            children1.setShowValue(RefresUtils.getCheckPosition(refresBean, remark) + "");
                            newchildren.add(children1);
                        } else {
                            newchildren.add(children1);
                        }
                    } else if ("多选一".equals(checkType) || checkType.isEmpty() || modeRuleNumber.isEmpty()) {
                        newchildren.add(children1);
                    } else if ("是否显示".equals(checkType)) {
                        if (isShowVi(children1, refresBean, modeName)) {
                            newchildren.add(children1);
                        }
                    } else if (checkType.contains("是否显示")) {
                        if (ExamUtils.isHasComma(modeRuleNumber)) {
                            String[] ruleStr = ExamUtils.getArrayStr(modeRuleNumber);//规则位置
                            String[] splitRemark = children1.getRemark().split("&");
                            if (ruleStr.length >= 2 && "0".equals(ruleStr[0]) && "0".equals(ruleStr[1])) {
                                if (RefresUtils.getIsVisible(refresBean, children1.getRemark())) {
                                    String refresContent = RefresUtils.getRefresContent(refresBean, children1.getRemark());
                                    children1.setRemark(refresContent);
                                    children1.setShowValue(refresContent);
                                    children1.setModeRuleNumber(Utils.RemoveFirstEle_0(modeRuleNumber));
                                    children1.setCheckType(Utils.RemoveFirstEle(checkType));
                                    newchildren.add(children1);
                                }
                            } else {
                                if (ExamUtils.isShow(ruleStr[0], splitRemark[0], modeName, refresBean)) {
                                    newchildren.add(isShowCon(children1, modeName, refresBean, ruleMap, check.get(i).getCheckName()));
                                }
                            }
                        } else {
                            String remark = children1.getRemark();
                            String[] ruleStr = ExamUtils.getArrayAddStr(modeRuleNumber);
                            String[] remarkStr = ExamUtils.getArrayAddStr(remark);
                            if (ExamUtils.isShow(ruleStr[0], remarkStr[0], modeName, refresBean)) {
                                if (refresBean != null && checkType.contains("多选一") && ruleStr[1].equals("0")) {
                                    //指图书  显示器 水平垂直 2选1
                                    int beanValue = RefresUtils.getCheckPosition(refresBean, remarkStr[1]);
                                    //二选一
                                    children1.setRemark(beanValue + "");
                                    children1.setShowValue(beanValue + "");
                                    String newCheckType = Utils.RemoveFirstEle(checkType);
                                    String newModeRule = Utils.RemoveFirstEle_0(modeRuleNumber);
                                    children1.setCheckType(newCheckType);
                                    children1.setModeRuleNumber(newModeRule);
                                }
                                newchildren.add(children1);
                            }
                        }
                    } else {
                        //显示型名  数据比较  数据显示   数据比较A  导入指图书
                        NewExamBean.Project.Check.Children showEl = isShowEl(children1, ruleMap, modeName);
                        newchildren.add(showEl);
                    }
                }
                //项目判断完成  增加新的
                if (newchildren.size() != 0) {
                    check.get(i).setChildren(newchildren);
                    newCheck.add(check.get(i));
                }
            }
            if (newCheck.size() != 0) {
                project.setCheck(newCheck);
                mList.add(project);
            }
        }
        return mList;
    }

    //TODO 是否显示
    public static boolean isShowVi(NewExamBean.Project.Check.Children children1, RefresBean refresBean, String modeName) {
        String remark = children1.getRemark();
        String modeRuleNumber = children1.getModeRuleNumber();
        if (modeRuleNumber.contains("|") || remark.contains("|")) {
            //  modeRuleNumber= 4+5|4+17    remark=4-!A+5-!JF5|4-A+17-!WM    规则或      modeRuleNumber=4+5    remark=4-!A+5-!JF5|4-A+5-WM
            // 指图书规则
            boolean isShow = false;
            String[] modeArray = modeRuleNumber.split("\\|");
            String[] remarkArray = remark.split("\\|");
            boolean contains = modeRuleNumber.contains("|");
            //如果都包含| 大小!=  规则报错--->显示
            if (contains && modeArray.length != remarkArray.length) {
                return true;
            }
            for (int i = 0; i < remarkArray.length; i++) {
                String newModeRule = contains ? modeArray[i] : modeRuleNumber;
                if (ExamUtils.isShow(newModeRule, remarkArray[i], modeName, refresBean)) {
                    isShow = true;
                    break;
                }
            }
            Log.e("TAG", "isShowVi1: ---" + isShow);
            return isShow;
        } else if (modeRuleNumber.contains("Z")) {
            if (ExamUtils.isHasExcl(modeRuleNumber) && remark.isEmpty() && !modeName.contains("Z")) {
                return true;
            } else if (remark.isEmpty() && modeName.contains("Z")) {
                return true;
            } else if (!remark.isEmpty()) {
                return ExamUtils.ishasZshow(remark, modeName);
            } else {
                return false;
            }
        } else if (ExamUtils.getArrayAddStr(modeRuleNumber)[0].equals("0")) {
            //TODO 包含指图书的规则   0+9
            String[] remarkArray = ExamUtils.getArrayAddStr(remark);
            if (refresBean != null) {
                boolean beanValue = RefresUtils.getIsVisible(refresBean, remarkArray[0]);
                String newRemark = Utils.RemoveFirstEle_0(remark);
                String newModeRule = Utils.RemoveFirstEle_0(modeRuleNumber);
                boolean show = ExamUtils.isShow(newModeRule, newRemark, modeName, refresBean);
                return (show && beanValue);
            } else {
                return false;
            }
        } else {
            return ExamUtils.isShow(modeRuleNumber, remark, modeName, refresBean);
        }
    }

    //19177
    //TODO 多个规则  eg:是否显示,多选一    是否显示,导入指图书        是否显示,显示型名     是否显示,数据比较     是否显示,数据显示    是否显示，数据比较A   是否显示，数据比较，前后联动
    public static NewExamBean.Project.Check.Children isShowCon(NewExamBean.Project.Check.Children children1, String modeName, RefresBean refresBean, String ruleMap, String checkName) {
        String modeRuleNumber = children1.getModeRuleNumber();
        String checkType = children1.getCheckType();
        String remark = children1.getRemark();
        String[] remarkStr = remark.split("&");
        //是否显示,显示型名     是否显示,数据比较
        String[] ruleStr = ExamUtils.getArrayStr(modeRuleNumber);//规则位置

        String newCheckType = Utils.RemoveFirstEle(checkType);
        String newModeRule = Utils.RemoveFirstEle(modeRuleNumber);
        //去掉是否显示 重新赋值
        children1.setCheckType(newCheckType);
        children1.setModeRuleNumber(newModeRule);
        if ("0".equals(newModeRule)) {
            //指图书多选一    填充
            if (ruleStr.length >= 2) {
                if (refresBean != null) {
                    if (newCheckType.contains("导入指图书")) {
                        //TODO 0,0    是否显示,导入指图书
                        String refresContent = RefresUtils.getRefresContent(refresBean, remarkStr[1]);
                        children1.setRemark(refresContent);
                        children1.setShowValue(refresContent);
                    } else if (newCheckType.contains("多选一")) {
                        int checkPosition = RefresUtils.getCheckPosition(refresBean, remarkStr[1]);
                        children1.setRemark(String.valueOf(checkPosition));
                        children1.setShowValue(String.valueOf(checkPosition));
                    }
                } else {
                    children1.setRemark("");
                }
            }
        } else if (children1.getCheckType().equals("显示型名")) {
            //最多两个
            String[] arrayStrMode = ModelTypeUtils.getModeArray(ModelTypeUtils.getModelHeader(modeName), modeName);
            if (Utils.isNumber(children1.getModeRuleNumber())) {
                int mPostNum = Integer.parseInt(children1.getModeRuleNumber());
                //TODO 小口径 第17位 显示型名
                if (arrayStrMode[mPostNum].contains("/")) {
                    //TODO 限制很大，如果最后一位没有规则，那就gg
                    if (!ruleMap.isEmpty()) {
                        Map<String, Object> xMap = JsonUtils.parseJSONstr2Map(ruleMap);
                        String combinationStr = ModelTypeUtils.getCombinationStr(modeName, children1.getModeRuleNumber());
                        String axg = JsonUtils.getRuleContentX(xMap, children1.getModeHead(), Utils.getStationId(), children1.getModeRuleNumber(), combinationStr);
                        if ("无".equals(axg)) {
                            children1.setRemark(arrayStrMode[mPostNum]);
                            children1.setShowValue(arrayStrMode[mPostNum]);
                        } else {
                            children1.setRemark(axg);
                            children1.setShowValue(axg);
                        }
                    } else {
                        children1.setRemark(arrayStrMode[mPostNum]);
                        children1.setShowValue(arrayStrMode[mPostNum]);
                    }
                } else {
                    children1.setRemark(arrayStrMode[mPostNum]);
                    children1.setShowValue(arrayStrMode[mPostNum]);
                }
            } else {
                children1.setRemark("");
            }
        } else if (children1.getCheckType().equals("数据比较A")) {
            //TODO  小口径特殊规则获取比较的值      是要remark的值+其他位数的值   用不到！
            String conStr1 = ModelTypeUtils.getConStr(modeName, children1, ruleStr);
            String ruleContentStr7 = JsonUtils.getRuleContentStr7(children1.getModeHead(), children1.getModeRuleNumber(), conStr1);
            children1.setRemark(ruleContentStr7);
            children1.setShowValue(ruleContentStr7);
        } else if (children1.getRemark().contains("&")) {
            //TODO 是否显示,多选一   是否显示，导入指图书
            //指图书的多选一
            if ("0".equals(ruleStr[1])) {
                int beanValue = RefresUtils.getCheckPosition(refresBean, remarkStr[1]);
                //二选一
                children1.setRemark(String.valueOf(beanValue));
                children1.setShowValue(String.valueOf(beanValue));
            } else {
                children1.setRemark(remarkStr[1]);
                children1.setShowValue(remarkStr[1]);
            }
        } else if ("耐水压试验".equals(checkName.trim()) && checkType.contains("数据比较")) {
            //TODO 获取特殊 规则内容  ***************************************   小口径耐水压 AXG/W   2,2/10	是否显示,数据比较	2-040,050,065,080,100
            if (remark.contains("/")) {
                String ruleContentStr7 = JsonUtils.getRuleContentStr7(children1.getModeHead(), children1.getModeRuleNumber(), ModelTypeUtils.getConStr(modeName, children1, ruleStr));
                children1.setRemark(ruleContentStr7);
                children1.setShowValue(ruleContentStr7);
            } else {
                String ruleContentStr = JsonUtils.getRuleContentStr(modeName, children1.getModeHead(), children1.getModeRuleNumber());
                children1.setRemark(ruleContentStr);
                children1.setShowValue(ruleContentStr);
            }
        } else if (children1.getCheckType().contains("是否包含") || children1.getCheckType().contains("前后联动")) {
            String[] arrayRuleStr = ExamUtils.getArrayStr(newModeRule);
            if (!ruleMap.isEmpty()) {
                String remarkStr1 = getRemarkStr(children1.getModeHead(), ruleMap, modeName, arrayRuleStr[0]);
                children1.setRemark(remarkStr1);
                children1.setShowValue(remarkStr1);
            }
        } else {
            String ruleContentStr = JsonUtils.getRuleContentStr(modeName, children1.getModeHead(), children1.getModeRuleNumber());
            children1.setRemark(ruleContentStr);
            children1.setShowValue(ruleContentStr);
        }
        return children1;
    }

    //TODO else
    public static NewExamBean.Project.Check.Children isShowEl(NewExamBean.Project.Check.Children children1, String ruleMap, String modeName) {
        String checkType = children1.getCheckType();
        String modelHeader = ModelTypeUtils.getModelHeader(modeName);
        String modeRuleNumber = children1.getModeRuleNumber();
        String[] arrayStrMode = ModelTypeUtils.getModeArray(modelHeader, modeName);
        if (checkType.equals("显示型名")) {
            if (Utils.isNumber(children1.getModeRuleNumber())) {
                int i1 = Integer.parseInt(children1.getModeRuleNumber());
                children1.setRemark(arrayStrMode[i1]);
                children1.setShowValue(arrayStrMode[i1]);
            } else {
                children1.setRemark("");
            }
        } else {
            String modStr = "";
            if (checkType.contains("前后联动") && (checkType.contains("是否包含") || checkType.contains("数据比较"))) {
                String[] arrayRuleStr = ExamUtils.getArrayStr(modeRuleNumber);
                modStr = arrayRuleStr[0];
            } else {
                modStr = modeRuleNumber;
            }
            //TODO 整体判断
            if (!ruleMap.isEmpty()) {
                children1.setRemark(getRemarkStr(children1.getModeHead(), ruleMap, modeName, modStr));
                children1.setShowValue(getRemarkStr(children1.getModeHead(), ruleMap, modeName, modStr));
            } else {
                children1.setRemark("");
            }
        }
        return children1;
    }


    public static String getRemarkStr(String modeHead, String ruleMap, String modeName, String modStr) {
        Log.e("TAG", "getRemarkStr: ===" + ruleMap);
        Map<String, Object> xMap = JsonUtils.parseJSONstr2Map(ruleMap);
        String combinationStr = ModelTypeUtils.getCombinationStr(modeName, modStr);
        Log.e("TAG", "组合串====" + combinationStr);
        String axg = JsonUtils.getRuleContent(xMap, modeHead, Utils.getStationId(), modStr, combinationStr);
        Log.e("回显结果:", "查询到的值====" + axg);
        return axg;
    }
}
