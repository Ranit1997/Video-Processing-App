package com.iiestscse.video_processing_app_1;


        import android.os.Environment;
        import android.util.Log;

        import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStreamWriter;

        import static java.io.File.separator;
        import static java.lang.Math.abs;

public class Temporal_Compression
{
    public static int[] Compress(int framecount)throws IOException {
        File folder = new File(Environment.getExternalStorageDirectory() + separator +"Video_Processing_App"+separator+ "Selected_Frames");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.e("Folder","Created");
        } else {
            Log.e("Folder","Not created");
        }

        Mat frame = new Mat();int [] arr=new int[2];
        int i = 0, j = 0, c = 0;
        String strr, str, strrr;
        double row, col;
        int[] freq = new int[framecount];
        int[] frameid = new int[framecount];
        int k = 0;
        double r, g, b = 0;
        Mat frame_1 = new Mat();
        for (c = 0; c < framecount; c ++) {
            str = Integer.toString(c);

            strr = String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+separator+ "Bitmaps" + separator + str + ".jpeg";
            frame = Imgcodecs.imread(strr);


            Size size = frame.size();
            if (c == 0) {
                frame_1 = frame.clone();

                row = size.height;
                col = size.width;
                arr[0]=(int)row;
                arr[1]=(int)col;

               /* for (i = 0; i < row; i++) {
                    for (j = 0; j < col; j++) {
                        double[] temp = frame.get(i, j);
                        //System.out.println("Row=" + (j + 1) + " Column=" + (i + 1) + " R=" + data[2] + " G=" + data[1] + " B=" + data[0]);
                        frame_1.put(i, j, temp);
                    }
                }*/
                strrr = String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+separator+ "Selected_Frames" + separator + str + ".jpeg";
                Imgcodecs.imwrite(strrr, frame_1);
                frameid[k] = c;
                freq[k]++;


            } else {
                boolean flag=true;
                row = size.height;
                col = size.width;
                for (i = 0; i < row; i+=2)//Accessing image
                {
                    for (j = 0; j < col; j+=2) {
                        double[] data = frame.get(i, j);
                        double[] temp = frame_1.get(i, j);
                        r = data[2] - temp[2];
                        g = data[1] - temp[1];
                        b = data[0] - temp[0];
                        if ((abs(r) <= 10) && (abs(g) <= 10) && (abs(b) <= 10)) {
                            continue;
                        } else {
                            flag=false;break;
                        }
                    }
                }
                            //System.out.println("Row=" + (j + 1) + " Column=" + (i + 1) + " R=" + data[2] + " G=" + data[1] + " B=" + data[0]);
                            if(flag==false)
                            {
                            frame_1 = frame.clone();
                            strrr = String.valueOf(Environment.getExternalStorageDirectory()) + separator +"Video_Processing_App"+separator+ "Selected_Frames" + separator + str + ".jpeg";
                            Imgcodecs.imwrite(strrr, frame_1);
                            k++;
                            freq[k]++;
                            frameid[k] = c;
                        }
                        else
                            {
                                freq[k]++;
                            }

                    }
                }

        k++;
        frameid[k] = frameid[k - 1] + freq[k - 1];
        freq[k] = c - frameid[k - 1];
        FileOutputStream fos = new FileOutputStream(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+separator+"Selected_Frames" + separator + "metadata.txt");
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        for (i = 0; i < k; i++) {
            System.out.println(frameid[i] + " " + freq[i]);
            bw.write(frameid[i] + " " + freq[i]);
            bw.newLine();

        }
        bw.flush();
        bw.close();
        osw.close();
        fos.close();
        return arr;
    }
}
