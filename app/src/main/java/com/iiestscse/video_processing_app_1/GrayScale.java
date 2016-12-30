package com.iiestscse.video_processing_app_1;

/**
 * Created by user on 13-12-2016.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

//Converting to grayscale

public class GrayScale {

    private Bitmap bitmap,bmp;

    public GrayScale(Bitmap bit)
    {
        bmp=bit;
        bitmap=Bitmap.createBitmap(bit.getWidth(),bit.getHeight(), Bitmap.Config.RGB_565);
    }

    public Bitmap toGrayScale()
    {
        int row_selec=0;
        int col_selec=0;
        for(int i=0;i<bitmap.getHeight();i++)
        {
            for(int j=0;j<bitmap.getWidth();j++)
            {
                int col=bmp.getPixel(j,i);
                int new_col;
                switch(col_selec)
                {
                    case 0 : new_col=Color.rgb(Color.red(col),Color.red(col),Color.red(col));
                        break;
                    case 1 : new_col=Color.rgb(Color.green(col),Color.green(col),Color.green(col));
                        break;
                    case 2 : new_col=Color.rgb(Color.blue(col),Color.blue(col),Color.blue(col));
                        break;
                    default : new_col=Color.BLACK;
                }
                bitmap.setPixel(j,i,new_col);
                col_selec=(col_selec+1)%3;
            }
            row_selec=(row_selec+1)%3;
            col_selec=row_selec;
        }
        return bitmap;
    }

}

