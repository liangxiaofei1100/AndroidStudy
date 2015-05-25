package com.example.alex.common;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * This class is used for write log to file. It is useful for debug. Log file is
 * located at /data/data/com.dreamlink.communication/files/</br>
 * <p/>
 * Usage:</br>
 * <p/>
 * 1. {@link #open()}, Before write, we need to open the file to prepare
 * writing.</br>
 * <p/>
 * 2. {@link #writeLog(String)}, Write log to file.</br>
 * <p/>
 * 3. {@link #close()}, After finish all writing, do not forget to close the
 * file.;
 */
public class LogFile {
    private static final String TAG = "LogFile";
    private File mFile;
    private FileWriter mWriter;
    public static final String LOG_FOLDER_NAME = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "AlexLog";

    /**
     * Create log file use the file name.
     *
     * @param fileName
     */
    public LogFile(String fileName) {
        createFile(fileName);
    }

    private void createFile(String fileName) {
        String path = LOG_FOLDER_NAME + File.separator + TimeUtil.getDate();
        String filePath = path + File.separator + fileName;
        mFile = new File(filePath);
        if (!mFile.exists()) {
            try {
                mFile.getParentFile().mkdirs();
                mFile.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Create file error. File is " + filePath + ". " + e);
            }
        }
    }

    /**
     * Before write, we need to open the file to prepare writing.
     */
    public boolean open() {
        if (mFile != null && !mFile.exists()) {
            try {
                mFile.getParentFile().mkdirs();
                mFile.createNewFile();
            } catch (IOException e) {
                Log.e(TAG,
                        "Creat file error. File is " + mFile.getAbsolutePath()
                                + ". " + e);
                return false;
            }
        }
        try {
            mWriter = new FileWriter(mFile);
        } catch (IOException e) {
            Log.e(TAG, "Open file error. " + e);
        }
        return mWriter != null;
    }

    /**
     * Write log to file. </br>
     * <p/>
     * Tips: add \n to make log more readable.
     *
     * @param log
     */
    public void writeLog(String log) {
        if (mWriter == null) {
            if (!open()) {
                Log.e(TAG, "writeLog error. open() file error.");
                return;
            }
        }

        try {
            mWriter.write(log);
            mWriter.flush();
        } catch (IOException e) {
            Log.e(TAG, "writeLog write error." + e);
        }
    }

    public void writeLog(byte[] logs) {
        if (mWriter == null) {
            if (!open()) {
                Log.e(TAG, "writeLog error. open() file error.");
                return;
            }
        }
        char[] log = getChars(logs);
        try {
            mWriter.write(log);
            mWriter.flush();
        } catch (IOException e) {
            Log.e(TAG, "writeLog write error." + e);
        }
    }

    /**
     * bytes tp chars
     *
     * @param bytes
     * @return
     */
    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    /**
     * After finish all writing, do not forget to close the file.
     */
    public void close() {
        if (mWriter == null) {
            return;
        }
        try {
            mWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "close error." + e);
        }
    }
}
