package com.iiestscse.video_processing_app_1;

/**
 * Created by user on 13-12-2016.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.io.File.separator;

public class ReceiverTransaction extends Thread{

    private static final String SERVER_IP="192.168.43.1";
    private static final int SERVER_PORT=4444;
    Socket sock=null;
    private Context context;
    ArrayList<File> files;

    public ReceiverTransaction(Context c)
    {
        context=c;
        files=new ArrayList<>();
    }
        int [] specs=new int[2];
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            Thread.sleep(15*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ReceiveThread.cancelEngagement();
            return;
        }
        boolean canConnect;
        try {
            canConnect=InetAddress.getByName(SERVER_IP).isReachable(2000);
            if(!canConnect)
            {
                ReceiveThread.cancelEngagement();
                return;
            }
            sock=new Socket(SERVER_IP, SERVER_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ReceiveThread.cancelEngagement();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            ReceiveThread.cancelEngagement();
            return;
        }

		/*
		 * Main Protocol for receiver-side exchange
		 *
		 */
        try
        {
            Log.e("Sender", "active");
            InputStream in=sock.getInputStream();
            OutputStream out=null;
            DataInputStream din=new DataInputStream(in);
            int frm=din.readInt();
            int incoming_size=Integer.parseInt(din.readUTF());
            Log.e("Size",incoming_size+"");
            int i=0;
            File f = new File(Environment.getExternalStorageDirectory()+separator+"Video_Processing_App"+separator+"Selected_Frames");
            if(!f.exists())
                f.mkdir();

            byte[] bytes = new byte[16*1024];

            while(i<incoming_size)
            {
                String filenm=din.readUTF();
                long size=din.readLong();
                out = new FileOutputStream(Environment.getExternalStorageDirectory()+separator+"Video_Processing_App"+separator+"Selected_Frames"+separator+filenm);
                int count;
                while (size>0  && (count = din.read(bytes,0, (int) Math.min(bytes.length,size))) > 0) {
                    out.write(bytes, 0, count);
                    size-=count;
                }
                i++;
                Log.e("Receiver","File received");
                out.flush();
                out.close();
                files.add(new File(Environment.getExternalStorageDirectory()+separator+"Video_Processing_App"+separator+"Selected_Frames"+separator+filenm));
            }
            din.close();
            in.close();
            //out.close();
            sock.close();
            specs=Temporal_Decompression.Decompress();
            Video_Generator.video(specs[0],specs[1],frm);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.e("Receiver Error", "In main transfer protocol");
            ReceiveThread.cancelEngagement();
            return;
        }

        Log.e("Exchange", "Completed without exception");
        ReceiveThread.cancelEngagement();
    }

}
