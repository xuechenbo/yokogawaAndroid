package com.yokogawa.xc.utils;

import java.util.regex.Pattern;

public class PatterUtils {
    //13     15   18   17 开头手机号
    public static final String PHONE_NUMBER_REGEX = "^1(3{1}\\d{1}|5{1}[012356789]{1}|8{1}[0123456789]{1}|7{1}[0123678]{1})\\d{8}$";


    public static void showPattern() {
        //  ^ 开头
    }
}
