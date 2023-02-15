package com.yokogawa.xc.utils;

public class ClickUtils {
    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick1() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= 1000) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     *  if (Utils.isFastClick()) {
     *      // 进行点击事件后的逻辑操作
     *  }
     */
}
