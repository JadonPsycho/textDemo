package com.psycho.docx;

import android.util.Log;

import java.io.File;

/**
 * Created by mr.psycho on 2017/6/1.
 */

public class Config {
    public static final String Files = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/docxDemo/";

    public static String getFiles() {
        File file = new File(Files);
        if (!file.exists()) {
            boolean a = file.mkdirs();
            if (a) {
                Log.e("files", "havefile");
            } else {
                Log.e("files", "nofile");
            }
        }
        return Files;
    }
}
