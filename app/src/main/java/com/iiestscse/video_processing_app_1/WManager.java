package com.iiestscse.video_processing_app_1;

/**
 * Created by user on 13-12-2016.
 */

import android.content.Context;
import android.net.wifi.WifiManager;

public class WManager {

    public boolean isWifiOn(Context context)
    {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifimanager.isWifiEnabled()) {
            return true;
        }
        else
            return false;
    }

    public void enableWifi(Context context)
    {
        ApManager ap=new ApManager();
        ap.ApStateOff(context);
        if(isWifiOn(context))
            return;
        else
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }
    }

    public void disableWifi(Context context)
    {
        if(isWifiOn(context))
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }
    }

}
