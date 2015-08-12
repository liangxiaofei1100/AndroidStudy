package com.example.alex.http;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.R;
import com.example.alex.common.FileConstant;
import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;
import com.example.alex.common.ShowResultUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.text.DecimalFormat;

public class HttpDownload2 extends ShowResultActivity {
    String URL = "http://down.cashcashcash.cn/img/V_201507311049141438310954794_.jpg";
    private static AsyncHttpClient sDownloadFileClient;

    private static final int DEFAULT_MAX_RETRIES = 1;
    private static final int DOWNLOAD_MAX_RETRIES = 2;
    // Set both the connection and socket timeouts.
    private static final int DEFAULT_SOCKET_TIMEOUT = 5000;

    Button mDownloadButton;
    Button mDeleteButton;
    File mFileToDownload = new File(FileConstant.DIAR_DOWNLOAD, "TestDownload.jpg");
    File mDownloadingTmpFile = new File(FileConstant.DIAR_DOWNLOAD, "TestDownload.jpg.dl");


    public static final int STATUS_DOWNLOAD_NOT_START = 0;
    public static final int STATUS_DOWNLOAD_ONGOING = 1;
    public static final int STATUS_DOWNLOAD_FINISHED = 2;
    private int mDownloadStatus = STATUS_DOWNLOAD_NOT_START;

    private DecimalFormat mNumberFormat = new DecimalFormat("0.00");

    {
        // 初始化下载文件Client
        sDownloadFileClient = new AsyncHttpClient();
        sDownloadFileClient.setTimeout(DEFAULT_SOCKET_TIMEOUT);
        sDownloadFileClient.setMaxRetriesAndTimeout(DOWNLOAD_MAX_RETRIES,
                DEFAULT_SOCKET_TIMEOUT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_download2);
        mDownloadButton = (Button) findViewById(R.id.btn_download);
        mDeleteButton = (Button) findViewById(R.id.btn_delete);
        mTextView = (TextView) findViewById(R.id.tv_result);
    }

    public void downloadButtonClicked(View view) {
        switch (mDownloadStatus) {
            case STATUS_DOWNLOAD_NOT_START:
                setDownloadStatus(STATUS_DOWNLOAD_ONGOING);
                break;
            case STATUS_DOWNLOAD_ONGOING:
                setDownloadStatus(STATUS_DOWNLOAD_NOT_START);
                break;
            case STATUS_DOWNLOAD_FINISHED:
                setDownloadStatus(STATUS_DOWNLOAD_ONGOING);
                break;
            default:
                break;
        }
    }


    private void setDownloadStatus(int status) {
        mDownloadStatus = status;
        updateDownloadStatusUI();
        setDownload();
    }

    private void setDownload() {
        switch (mDownloadStatus) {
            case STATUS_DOWNLOAD_NOT_START:
                sDownloadFileClient.cancelRequests(mContext, true);
                break;
            case STATUS_DOWNLOAD_ONGOING:
                if (mFileToDownload.exists()) {
                    mFileToDownload.delete();
                }
                sDownloadFileClient.get(mContext, URL, new DownloadResponseHandler(mDownloadingTmpFile));
                break;
            default:
                break;
        }
    }

    private void updateDownloadStatusUI() {
        switch (mDownloadStatus) {
            case STATUS_DOWNLOAD_NOT_START:
                mDownloadButton.setText("Start download");
                break;
            case STATUS_DOWNLOAD_ONGOING:
                mDownloadButton.setText("Pause download");
                break;
            case STATUS_DOWNLOAD_FINISHED:
                mDownloadButton.setText("Restart download");
                break;
            default:

                break;
        }
    }

    public void deleteButtonClicked(View view) {
        if (mFileToDownload.exists()) {
            mFileToDownload.delete();
        }
        setDownloadStatus(STATUS_DOWNLOAD_NOT_START);
    }

    private void showMessage(String message) {
        ShowResultUtil.showInTextView(mTextView, message);
    }

    private class DownloadResponseHandler extends RangeFileAsyncHttpResponseHandler {

        public DownloadResponseHandler(File file) {
            super(file);
        }

        @Override
        public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
            Log.d();
            showMessage("onFailure");
            setDownloadStatus(STATUS_DOWNLOAD_FINISHED);
        }

        @Override
        public void onSuccess(int i, Header[] headers, File file) {
            Log.d();
            showMessage("onSuccess");
            Log.d("mFileToDownload: " + mFileToDownload.getName());
            if (mDownloadingTmpFile.exists() && mDownloadingTmpFile.renameTo(mFileToDownload)) {
                showMessage("Rename sucess: " + mDownloadingTmpFile.getName() + " -> " + mFileToDownload.getName());
            } else  {
                showMessage("Rename fail.");
            }
            setDownloadStatus(STATUS_DOWNLOAD_FINISHED);
        }

        @Override
        public void onProgress(int bytesWritten, int totalSize) {
            super.onProgress(bytesWritten, totalSize);
            long now = System.currentTimeMillis();
            if (now - lastUpdateTime >= 1000) {
                //每一秒更新
                if (lastUpdateTime > 0) {
                    int addedBytes = bytesWritten - lastBytes;
                    showMessage(bytesToMB(bytesWritten) + " / " +
                            bytesToMB(totalSize) + ", speed = " + bytesToKB(addedBytes) + "KB/s");
                }
                lastBytes = bytesWritten;
                lastUpdateTime = now;
            }
        }

        private String bytesToKB(double bytes) {
            return mNumberFormat.format(bytes / 1024);
        }

        private String bytesToMB(double bytes) {
            return mNumberFormat.format(bytes / 1024 / 1024);
        }

        int lastBytes;
        long lastUpdateTime;

        @Override
        public void onCancel() {
            super.onCancel();

        }
    }
}
