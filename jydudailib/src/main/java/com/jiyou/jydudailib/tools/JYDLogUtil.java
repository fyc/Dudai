
package com.jiyou.jydudailib.tools;

import android.util.Log;

public class JYDLogUtil {

    private final static String LOG_TAG = "JYGameSDK";

    private static final boolean logTAG = false;

    public static void d(String msg) {
        if (logTAG) {
            Log.d(LOG_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (logTAG) {
            Log.i(LOG_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (logTAG) {
            Log.e(LOG_TAG, msg);
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (logTAG) {
            Log.e(LOG_TAG, msg, throwable);
        }
    }

    public static void d(String tag, String msg) {
        if (logTAG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (logTAG) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (logTAG) {
            Log.i(tag, msg);
        }
    }


}
