package com.iiestscse.video_processing_app_1;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by user on 30-12-2016.
 */

public class copy {
    static File dst= new File(Environment.getExternalStorageDirectory()+File.separator+"source.mp4");

    public static void copy_cat(File src) throws IOException {
        InputStream in = new FileInputStream(src);

        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {

            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
