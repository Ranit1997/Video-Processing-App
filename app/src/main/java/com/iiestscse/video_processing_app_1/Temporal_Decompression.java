package com.iiestscse.video_processing_app_1;

import android.os.Environment;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.io.File.separator;

public class Temporal_Decompression
{
    public static int[] Decompress()throws IOException
    {
        int [] arr=new int[2];
        File folder = new File(Environment.getExternalStorageDirectory() + separator +"Video_Processing_App"+separator+ "Final_Frames");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.e("Folder","Created");
        } else {
            Log.e("Folder","Not created");
        }

        FileInputStream fis=new FileInputStream(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+separator+"Selected_Frames" + separator + "metadata.txt");
        InputStreamReader isr=new InputStreamReader(fis);
        BufferedReader br=new BufferedReader(isr);
        Mat frame=new Mat();int i=0,j=0;String strr,str;double row,col;int freq,c=0;
        Mat frame_1=new Mat();
        String readline = br.readLine();
        while(readline!=null)
        {
            String[] content =readline.split(" ");
            strr=String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+separator+"Selected_Frames" + separator + content[0] + ".jpeg";
            freq=Integer.parseInt(content[1]);
            frame = Imgcodecs.imread(strr);
            frame_1= frame.clone();
            for(i=1;i<=freq;i++)
            {
                str=Integer.toString(c);
                strr=String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+separator+"Final_Frames" + separator + str + ".jpeg";
                Imgcodecs.imwrite(strr,frame_1);c++;
            }


            readline = br.readLine();
        }
        Size size=frame.size();
        row=size.width;
        col=size.height;
        arr[0]=(int)row;arr[1]=(int)col;
        return  arr;
    }
}