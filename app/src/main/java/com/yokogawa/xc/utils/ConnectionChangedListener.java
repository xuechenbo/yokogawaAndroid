package com.yokogawa.xc.utils;

import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;


public class ConnectionChangedListener implements ConnectionClassManager.ConnectionClassStateChangeListener {
    @Override
    public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
        Log.e("onBandwidthStateChange", bandwidthState.toString());
    }
}

