package com.example.alex.ftp;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

/**
 * Created by LiangXiaofei on 2015/9/8.
 */
public class FTPTestActivity extends ShowResultActivity {

    @Override
    protected void onButtonClick() {
        Log.d();

        Thread thread = new Thread() {
            @Override
            public void run() {
                Log.d();
                FTP ftp = new FTP();
                String[] folders = ftp.getAllFolderNames();
                if (folders != null) {
                    for (String folder : folders) {
                        Log.d("fodlers: " + folder);
                    }
                } else {
                    Log.d("folders is null.");
                }
            }
        };
        thread.start();
    }
}
