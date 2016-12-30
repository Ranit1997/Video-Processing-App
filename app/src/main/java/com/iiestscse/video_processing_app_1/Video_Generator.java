package com.iiestscse.video_processing_app_1;

import android.os.Environment;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoWriter;

import java.io.IOException;

import static java.io.File.separator;

/**
 * Created by user on 22-12-2016.
 */

public class Video_Generator {
    public static void video(int w, int h,int framecount) throws IOException {
        boolean stat;
        VideoWriter videoWriter = new VideoWriter(String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+ separator+ "final_video.avi", VideoWriter.fourcc('M', 'J', 'P', 'G'), 30, new Size(w, h));
        videoWriter.open(String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+separator+ "final_video.avi", VideoWriter.fourcc('M', 'J', 'P', 'G'), 30, new Size(w, h));
        stat= videoWriter.isOpened();
        Mat frame=new Mat();int c=0;String str,strr;
        for(c=0;c<framecount;c++)
        {
            str=Integer.toString(c);
            strr=String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+separator+ "Final_Frames" + separator + str + ".jpeg";
            frame= Imgcodecs.imread(strr);
            videoWriter.write(frame);
            frame.release();
        }
        System.out.println(stat);
        videoWriter.release();
    }
}