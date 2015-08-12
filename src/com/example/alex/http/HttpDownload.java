package com.example.alex.http;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.example.alex.R;
import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownload extends ShowResultActivity {
    private String URL = "http://down.cashcashcash.cn/MP4/X37.mp4";
    private String LOCAL_FILE = Environment.getExternalStorageDirectory() + "/Test.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            CreateFileThread thread = new CreateFileThread(URL);
            thread.start();
        } catch (MalformedURLException e) {
            Log.e(e);
        }
    }

    @Override
    protected void onButtonClick() {
        super.onButtonClick();
    }

    class CreateFileThread extends Thread {
        private URL mUrl;

        public CreateFileThread(String url) throws MalformedURLException {
            mUrl = new URL(url);
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            try {
                // 获取文件总大小
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int fileSize = connection.getContentLength();
                int responseCode = connection.getResponseCode();
                Log.d("fileSize = " + fileSize + ", responseCode = " + responseCode);
                // 创建本地文件
                File file = new File(LOCAL_FILE);
                if (!file.exists()) {
                    boolean createNewFileSuccess = file.createNewFile();
                    if (!createNewFileSuccess) {
                        throw new IOException("Create file fail");
                    }
                }
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                accessFile.setLength(fileSize);
                accessFile.close();
                // 开始下载
                DownloadThread downloadThread = new DownloadThread(mUrl, LOCAL_FILE, 0, fileSize);
                downloadThread.start();
            } catch (IOException e) {
                Log.e(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    class DownloadThread extends Thread {
        private URL mUrl;
        private int mCompleteSize;
        private int mTotalSize;
        private String mLocalFile;

        public DownloadThread(URL url, String localFile, int completeSize, int totalSize) throws MalformedURLException {
            mUrl = url;
            mLocalFile = localFile;
            mCompleteSize = completeSize;
            mTotalSize = totalSize;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            int compeleteSize = mCompleteSize;
            try {
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                // 设置范围，格式为Range：bytes=x-y;
                connection.setRequestProperty("Range", "bytes=" + mCompleteSize + "-" + mTotalSize);

                randomAccessFile = new RandomAccessFile(mLocalFile, "rwd");
                randomAccessFile.seek(mCompleteSize);
                Log.d("start connecting: " + connection);
                // 将要下载的文件写到保存在保存路径下的文件中
                is = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    compeleteSize += length;
                    // 更新数据库中的下载信息
                    Log.d("compeleteSize = " + compeleteSize);
                    // 用消息将下载信息传给进度条，对进度条进行更新
                    if (compeleteSize == mTotalSize) {
                        Log.d("Download complete!");
                    }
                }
            } catch (IOException e) {
                Log.e(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
