package com.iiestscse.video_processing_app_1;

/**
 * Created by user on 13-12-2016.
 */

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ReceiveThread extends Thread{

    private Context context;
    private volatile boolean stopThread=true;
    private static volatile boolean engage=true;
    ArrayList<String> blacklist=null;

    public void stopReceiverThread()
    {
        stopThread=false;
        interrupt();
    }

    public ReceiveThread(Context c) {
        // TODO Auto-generated constructor stub
        context=c;
        stopThread=true;
        engage=true;
        WManager manage=new WManager();
        if(!manage.isWifiOn(context))
        {
            manage.enableWifi(context);
        }
        blacklist= new ArrayList<>();
    }

    public static void cancelEngagement()
    {
        engage=true;
    }

    private boolean connectToServer(Context context)
    {
        WifiConfiguration conf = new WifiConfiguration();
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.disconnect();
        List<ScanResult> list =  wifiManager.getScanResults();
        if(list==null)
        {
            //cancelEngagement();
            return false;
        }
        for( ScanResult i : list ) {
            Log.e("SSID", i.SSID);
            if(i.SSID != null && (i.SSID.startsWith("SMSKCM877-") && i.SSID.endsWith("-MSDBP2016"))) {
                Log.e("SSID Connected", i.SSID);
                conf.SSID="\""+i.SSID+"\"";
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiManager.addNetwork(conf);
                int networkID=wifiManager.addNetwork(conf);
                wifiManager.disconnect();
                wifiManager.enableNetwork(networkID, true);
                wifiManager.reconnect();
                ReceiverTransaction rec=new ReceiverTransaction(context);
                rec.start();
                //blacklist.add(i.SSID);
                return true;
            }
        }
        //cancelEngagement();
        return false;
    }

    @Override
    public void run() {
        super.run();
        boolean look=true;
        while(look)
        {

            boolean connected=connectToServer(context);
            Log.e("Receiver", "Connected "+connected);
            look=(!connected);

        }
     /*   WManager manage=new WManager();
        if(manage.isWifiOn(context))
        {
        	manage.disableWifi(context);
        }

        cancelEngagement();
        */
    }
}
