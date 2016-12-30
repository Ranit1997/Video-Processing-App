package com.iiestscse.video_processing_app_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.opencv.android.OpenCVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static java.io.File.separator;


public class MainActivity extends AppCompatActivity implements CallBackListener {

    MediaMetadataRetriever med;

    private static final int FILE_REQUEST = 1;
    RelativeLayout firstactivity;
    boolean permission;


    private static final String TAG = "MainActivity";
    static
    {
        if(!OpenCVLoader.initDebug())
        {
            Log.d(TAG,"Opencv not loaded");
        }
        else
        {
            Log.d(TAG,"Opencv loaded");
        }
    }




    public void send(View v) {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("file/*"), FILE_REQUEST);
    }

    public void receive(View v) {
        ReceiveThread receiveThread = new ReceiveThread(MainActivity.this);
        receiveThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstactivity = (RelativeLayout) findViewById(R.id.main_screen);
        med = new MediaMetadataRetriever();
        permission= isStoragePermissionGranted();

    }
    public  boolean isStoragePermissionGranted() {


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            File folder = new File(Environment.getExternalStorageDirectory() + separator + "Video_Processing_App");
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
    }


    void openFile(Uri uri) {
        med.setDataSource(MainActivity.this, uri);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(permission==true)
        {
            File folder = new File(Environment.getExternalStorageDirectory() + separator + "Video_Processing_App");
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
        String path = null;
        if (requestCode == FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Snackbar.make(firstactivity, "File open successful", Snackbar.LENGTH_LONG).show();
                    Uri uri = data.getData();

                    path=uri.getPath();

                    try {
                        FileOutputStream fos = new FileOutputStream(String.valueOf(Environment.getExternalStorageDirectory()) + separator + "Video_Processing_App" + separator + "path.txt");

                        OutputStreamWriter osr = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osr);
                        bw.write(path);
                        bw.close();
                        osr.close();
                        fos.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    openFile(uri);
                    FrameSeparator frameSeparator = new FrameSeparator(med, MainActivity.this);
                    frameSeparator.execute();
                }
            } else {
                Snackbar.make(firstactivity, "File open was unsuccessful", Snackbar.LENGTH_LONG).show();
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onTaskCompleted() {

    }


    }


