package com.yokogawa.xc.bean;

public class RuleBean {

    private String ruleContent;
    private String modeRuleNumber;
    private String modeNumberContent;
    private String modeHead;

    public String getRuleContent() {
        return ruleContent == null ? "无" : ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getModeRuleNumber() {
        return modeRuleNumber;
    }

    public void setModeRuleNumber(String modeRuleNumber) {
        this.modeRuleNumber = modeRuleNumber;
    }

    public String getModeNumberContent() {
        return modeNumberContent;
    }

    public void setModeNumberContent(String modeNumberContent) {
        this.modeNumberContent = modeNumberContent;
    }

    public String getModeHead() {
        return modeHead;
    }

    public void setModeHead(String modeHead) {
        this.modeHead = modeHead;
    }
}
