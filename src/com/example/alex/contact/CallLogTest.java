package com.example.alex.contact;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;

import com.example.alex.common.Log;
import com.example.alex.common.ShowResultActivity;

public class CallLogTest extends ShowResultActivity {
    private static final String TAG = CallLogTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView.setText("call count: "
                + getInOutCallCount(getApplicationContext()));
    }

    /**
     * 获取一周之内呼入和呼出的电话总量
     * 
     * @param context
     * @return
     */
    public static int getInOutCallCount(Context context) {
        int callNum = 0;
        long weekInMillis = 1000 * 60 * 60 * 24 * 7;
        long aWeekAgo = System.currentTimeMillis() - weekInMillis;
        String selection = Calls.DATE + ">?";
        String[] selectionArgs = new String[] { String.valueOf(aWeekAgo) };
        Cursor cursorCall = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                new String[] { Calls.NUMBER, Calls.TYPE, Calls.DATE },
                selection, selectionArgs, Calls.DEFAULT_SORT_ORDER);
        
        if (cursorCall != null) {
            while (cursorCall.moveToNext()) {
                int type = cursorCall.getInt(cursorCall
                        .getColumnIndex(Calls.TYPE));
                switch (type) {
                case Calls.INCOMING_TYPE:
                case Calls.OUTGOING_TYPE:
                    callNum++;
                    break;
                }
            }

            cursorCall.close();
        }

        Log.d(TAG, "call count = " + callNum);
        return callNum;
    }
}
