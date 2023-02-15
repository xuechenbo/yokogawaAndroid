package com.yokogawa.xc.utils;


import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.bean.RuleBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonUtils {
    /**
     * @param modeName    型名
     * @param modelHeader 型名头
     * @param rulNumber   位置
     * @return
     */
    public static String getRuleContentStr(String modeName, String modelHeader, String rulNumber) {
        String ruleMap = SpUtils.getInstance(MyApplication.getInstance()).getString("ruleMap", "");
        if (!ruleMap.isEmpty()) {
            Map<String, Object> xMap = JsonUtils.parseJSONstr2Map(ruleMap);
            String combinationStr = ModelTypeUtils.getCombinationStr(modeName, rulNumber);
            String ruleContent = JsonUtils.getRuleContent(xMap, modelHeader, Utils.getStationId(), rulNumber, combinationStr);
            Log.e("getRuleContentStr", "查询到的值===" + ruleContent);
            return ruleContent;
        } else {
            return "无";
        }
    }


    //小口径组立7耐水压实验  特殊组合
    public static String getRuleContentStr7(String modelHeader, String rulNumber, String combinationStr) {
        String ruleMap = SpUtils.getInstance(MyApplication.getInstance()).getString("ruleMap", "");
        if (!ruleMap.isEmpty()) {
            Map<String, Object> xMap = JsonUtils.parseJSONstr2Map(ruleMap);
            String ruleContent = JsonUtils.getRuleContent(xMap, modelHeader, Utils.getStationId(), rulNumber, combinationStr);
            Log.e("getRuleContentStr7", "查询到的值===" + ruleContent);
            return ruleContent;
        } else {
            return "无";
        }
    }


    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是json数组继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<>();
                Iterator<Object> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = (JSONObject) it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof JSONObject) {
                // 如果内层是json对象继续解析
                map.put(k.toString(), parseJSON2Map((JSONObject) v));
            } else {
                // 如果内层是普通对象直接放入map中
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 将json字符串转换为Map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSONstr2Map(String jsonStr) {
        JSONObject json = (JSONObject) JSONObject.parse(jsonStr);
        Map<String, Object> map = parseJSON2Map(json);
        return map;
    }


    /**
     * @param xMap     当前pad所属组立线工位下的所有规则map
     * @param axg1     型名头部
     * @param project  工程id
     * @param position 位数    需要查询的规则
     * @return
     */
    public static String getRuleContent(Map<String, Object> xMap, String axg1, String project, String position, String ruleContent) {
        Log.e("TAG", "型名头部==" + axg1 + "   工位id==" + project + "  规则key==" + position + "   匹配值==" + ruleContent);

        String axg = axg1.replace("、", "/");
        String content = "无";
        for (Object map : xMap.entrySet()) {
            Log.e("xMap值:::", "key==" + ((Map.Entry) map).getKey() + "  value==" + ((Map.Entry) map).getValue());
        }
        if (!xMap.containsKey(axg)) {
            return "无";
        }
        Map projectMap = (Map) xMap.get(axg);
        if (!projectMap.containsKey(project)) {
            return "无";
        }

        Map loactMap = (Map) projectMap.get(project);
        if (!loactMap.containsKey(position)) {
            return "无";
        }
        List o1 = (List) loactMap.get(position);
        String json = JSON.toJSONString(o1);
        List<RuleBean> users = JSON.parseArray(json, RuleBean.class);

        for (int i = 0; i < users.size(); i++) {
            RuleBean ruleBean = users.get(i);
            Log.e("规则判等==", "modeNumberContent===" + ruleBean.getModeNumberContent());
            String modeNumberContent = ruleBean.getModeNumberContent();
            if (modeNumberContent.contains("*") && modeNumberContent.contains("_")) {
                //TODO  这种特殊规则 modeNumberContent = 040_*G1      ruleContent==050_BA1
                if (isEqual(modeNumberContent, ruleContent)) {
                    content = ruleBean.getRuleContent();
                    break;
                }
            } else if (ruleBean.getModeNumberContent().equals(ruleContent)) {
                //可以判断第一位是否相等 省
                content = ruleBean.getRuleContent();
                break;
            }
        }


        //TODO 针对涡流组立   /判断数量     DY和DYA    两位规则
        if ("无".equals(content) && ruleContent.contains("/") && ruleContent.contains("_") && ("DY".equals(axg) || "DYA".equals(axg))) {
//            content = isHasContent(users, ruleContent);
            //修改规则后使用
            content = isNewContent(users, ruleContent);
            if ("无".equals(content)) {
                String nKey = ruleContent.split("_")[0];
                for (int i = 0; i < users.size(); i++) {
                    RuleBean ruleBean = users.get(i);
                    if (ruleBean.getModeNumberContent().equals(nKey)) {
                        Log.e("TAG", "DY/DYA整体组立--第一位匹配" + nKey);
                        content = ruleBean.getRuleContent();
                        break;
                    }
                }
            }
        }
        //TODO  最后一位  全等没有，就判断是否包含     eg: /GRP/CH
        if (content.equals("无") && ruleContent.contains("/") && !ruleContent.contains("_")) {
            for (int i = 0; i < users.size(); i++) {
                RuleBean ruleBean = users.get(i);
                Log.e("规则包含==", "modeNumberContent===" + ruleBean.getModeNumberContent());
                if (ruleContent.contains(ruleBean.getModeNumberContent())) {
                    content = ruleBean.getRuleContent();
                    break;
                }
            }
        }
        return content;
    }


    //小口径，返回key
    public static String getRuleContentX(Map<String, Object> xMap, String axg1, String project, String position, String ruleContent) {
        String axg = axg1.replace("、", "/");
        String content = "无";
        if (!xMap.containsKey(axg)) {
            return "无";
        }
        Map projectMap = (Map) xMap.get(axg);
        if (!projectMap.containsKey(project)) {
            return "无";
        }
        Map loactMap = (Map) projectMap.get(project);
        if (!loactMap.containsKey(position)) {
            return "无";
        }
        List o1 = (List) loactMap.get(position);
        String json = JSON.toJSONString(o1);
        List<RuleBean> users = JSON.parseArray(json, RuleBean.class);

        for (int i = 0; i < users.size(); i++) {
            RuleBean ruleBean = users.get(i);
            if (ruleBean.getModeNumberContent().equals(ruleContent)) {
                content = ruleBean.getModeNumberContent();
                break;
            }
        }

        //TODO 针对涡流组立   /判断数量
        if ("无".equals(content) && ruleContent.contains("/") && ruleContent.contains("_")) {
            //修改规则后使用
            content = isNewContent(users, ruleContent);
        }

        //TODO  最后一位  全等没有，就判断是否包含     eg: /GRP/CH
        if (content.equals("无") && ruleContent.contains("/") && !ruleContent.contains("_")) {
            for (int i = 0; i < users.size(); i++) {
                RuleBean ruleBean = users.get(i);
                Log.e("规则包含==", "modeNumberContent===" + ruleBean.getModeNumberContent());
                if (ruleContent.contains(ruleBean.getModeNumberContent())) {
                    content = ruleBean.getModeNumberContent();
                    break;
                }
            }
        }
        return content;
    }

    //TODO 针对涡流组立  顺序匹配
    public static String isNewContent(List<RuleBean> users, String ruleContent) {
        boolean isFlag = false;
        String content = "无";
        String recordStr = "";
        String[] ruleArray = ruleContent.split("_");
//        String[] endArray = ruleArray[1].split("/");
        //ruleConent===F_/X1/X2/X3
        List<String> sortStr = isSortStr(ruleArray[1].substring(1));
        String newConstr = ruleArray[0] + "_";
        for (int j = 0; j < sortStr.size(); j++) {
            if ("无".equals(content)) {
                newConstr = conStr(ruleArray[0] + "_", sortStr.get(j));
            } else if (isFlag) {
                isFlag = false;
                newConstr = conStr(newConstr, sortStr.get(j));
            } else {
                newConstr = conStr(recordStr, sortStr.get(j));
            }
            for (int i = 0; i < users.size(); i++) {
                String modeNumberContent = users.get(i).getModeNumberContent();
                //第一位相等
                if (modeNumberContent.split("_")[0].equals(ruleArray[0])) {
                    Log.e("isHasContent", "拼接字符===" + newConstr);
                    if (newConstr.equals(modeNumberContent)) {
                        isFlag = true;
                        recordStr = newConstr;
                        content = users.get(i).getRuleContent();
                        Log.e("isHasContent", "赋值过程===" + content);
                    }
                }
            }
        }
        Log.e("isHasContent", "最终结果===" + content);
        return content;
    }

    public static List<String> isSortStr(String str) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(str.split("/")));
        Collections.sort(list, Comparator.comparing(l -> l.substring(0, 1)));
//        Collections.sort(list, (l1, l2) -> l1.substring(0, 1).compareTo(l2.substring(0, 1)));
        return list;
    }


    public static String conStr(String start, String endStr) {
        return start + "/" + endStr;
    }


    /**
     * @param modeNumberContent
     * @param ruleContent
     * @return 这种特殊规则 modeNumberContent = 040_*G1      ruleContent==050_BA1
     */
    public static boolean isEqual(String modeNumberContent, String ruleContent) {
        boolean isTr = false;
        String[] newStr = modeNumberContent.split("_");
        String[] rulecon = ruleContent.split("_");
        if (newStr.length != rulecon.length) {
            return false;
        }
        for (int j = 0; j < newStr.length; j++) {
            if (j == newStr.length - 1) {
                String endStr = newStr[j].replace("*", "");
                if (rulecon[j].endsWith(endStr)) {
                    isTr = true;
                }
            }
            if (!newStr[j].equals(rulecon[j])) {
                break;
            }
        }
        return isTr;
    }
}

