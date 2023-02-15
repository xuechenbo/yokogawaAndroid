package com.yokogawa.xc.base;

import android.app.Application;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configUnits();
    }

    private void configUnits() {
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setDesignSize(540, 960)
                .setSupportSubunits(Subunits.MM);
    }
}
