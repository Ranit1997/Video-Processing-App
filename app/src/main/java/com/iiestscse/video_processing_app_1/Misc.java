package com.iiestscse.video_processing_app_1;

/**
 * Created by user on 13-12-2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;

public class Misc {

    public static String getHotName()
    {
        String name="SMSKCM877-"+((int)(Math.random() * ((99999) + 1)))+"-MSDBP2016";
        return name;
    }

    public static String getTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date dt = new Date();
        String time = sdf.format(dt);
        return time;
    }
}
