package com.iiestscse.video_processing_app_1;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.io.File.separator;

public class FrameSeparator extends AsyncTask<Void,Void,Void> {

    private CallBackListener callBackListener;
    private MediaMetadataRetriever meg;
    ProgressDialog progressDialog;
    Context context;
    ArrayList<File> filestore;
    SenderThread senderThread;
    ArrayList<File> list;

    FrameSeparator(MediaMetadataRetriever fmeg, Context con)
    {
        meg=fmeg;
        context=con;
        progressDialog=new ProgressDialog(context);
        filestore=new ArrayList<>();

    }
   int c=0;int [] specs=new int[2];
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Please wait");
        progressDialog.show();

         File folder = new File(Environment.getExternalStorageDirectory() + separator +"Video_Processing_App"+separator+ "Bitmaps");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.e("Folder","Created");
        } else {
            Log.e("Folder","Not created");
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String path;
        try {
            path=new Scanner(new File(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+ separator + "path.txt")).useDelimiter("\\Z").next();
            System.out.println(path);
            File src=new File(path);
           copy.copy_cat(src);
            ExtractMpegFramesTest mTest = new ExtractMpegFramesTest();

            try {

                mTest.testExtractMpegFrames();
                c=mTest.framecount;
            } catch (Throwable e1) {
                /* TODO Auto-generated catch block */
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (IOException e) {
            e.printStackTrace();
        }*/
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"source.mp4");
        boolean deleted = file.delete();
        if(deleted==true)
        {
            System.out.println("Temporary Source File deleted");
        }
        else
        {
            System.out.println("Failed to delete Temporary Source File");
        }
        String total_time=meg.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long time=Long.parseLong(total_time);

        Log.e("Time",time+"");
       /* for(long i=1;i<time;i=i+=100) {
            Bitmap bmp = meg.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
            if (bmp == null) {
                Log.e("Bitmap", "null");
                continue;
            }*/




        /*try {

            String path=new Scanner(new File(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+ separator + "path.txt")).useDelimiter("\\Z").next();
            Frame_Generator.Vid2Frames(path);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // convert to  grayscale

        FileInputStream fis= null;
        try {
            fis = new FileInputStream(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+ separator + "frame_count.txt");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        InputStreamReader isr=new InputStreamReader(fis);
        BufferedReader br=new BufferedReader(isr);
        try {
            c=Integer.parseInt(br.readLine());
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/

            //String str=Integer.toString(i);
            //Bitmap bmp= BitmapFactory.decodeFile(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App"+separator+"Frames"+separator+str+".jpeg");

           // bmp=new GrayScale(bmp).toGrayScale();

            // Save Bitmap
           /* FileOutputStream out = null;
            try {
                File file=new File(Environment.getExternalStorageDirectory() +
                        separator +"Video_Processing_App"+separator+ "Bitmaps"+ separator+i+".jpeg");
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
                filestore.add(file);
                c+=100;
                Log.e("Frame","File written");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            bmp.recycle();
        }*/
        try {

            specs=Temporal_Compression.Compress(c);

            System.out.println(specs[1]+" "+specs[0]);

            File temp_fol=new File(Environment.getExternalStorageDirectory()+File.separator+"Video_processing_App"+File.separator+"Selected_Frames");
            if(!temp_fol.exists())
            {
                Log.e("temporal","No folder created");
                //throw FileNotFoundException;
            }
            else
            {
                list=new ArrayList<>();
                File flist[]=temp_fol.listFiles();
                for(File f:flist)
                {
                    list.add(f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(Void aVoid) {
        /*try (FileOutputStream fos = new FileOutputStream(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Bitmaps" + separator  + "frame_count.txt")) {
            OutputStreamWriter osr;
            osr = new OutputStreamWriter(fos);
            BufferedWriter bw;
            bw = new BufferedWriter(osr);
            bw.write(Integer.toString(c - 2));
            bw.close();
            osr.close();
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        meg.release();
        senderThread=new SenderThread(context,list,c);
        senderThread.start();
        //callBackListener.onTaskCompleted();
    }
}
