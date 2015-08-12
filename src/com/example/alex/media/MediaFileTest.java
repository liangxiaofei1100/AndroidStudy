package com.example.alex.media;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by LiangXiaofei on 2015/7/7.
 */
public class MediaFileTest extends ShowResultActivity {

    @Override
    protected void onButtonClick() {
        File file = new File(Environment.getExternalStorageDirectory() + "/test.mp4");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verifyVideoFile(file.getAbsolutePath(), new VideoVerifyListener() {
            @Override
            public void onVerifySuccess() {
                Log.d();
            }

            @Override
            public void onVerifyFail() {
                Log.e();
            }
        });
    }

    public void verifyVideoFile(String videoPath, final VideoVerifyListener listener) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    listener.onVerifySuccess();
                    mediaPlayer.release();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    listener.onVerifyFail();
                    mediaPlayer.release();
                    return false;
                }
            });
        } catch (IOException e) {
            Log.e(e.toString());
            listener.onVerifyFail();
            mediaPlayer.release();
        }
    }

    public interface VideoVerifyListener {
        void onVerifySuccess();

        void onVerifyFail();
    }
}
