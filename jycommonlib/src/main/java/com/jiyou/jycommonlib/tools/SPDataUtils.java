package com.jiyou.jycommonlib.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.jiyou.jycommonlib.config.ExtApp;

/**
 */

public class SPDataUtils {

    private static SharedPreferences shared = null;
    private static SharedPreferences.Editor editor = null;
    private static String xmlName = "JIYOUGAMESDK";

    private static SPDataUtils sp;

    //安装后首次打开：1-是/0-否
    public static String IS_SDK_FIRST_INSTALL = "isSdkFirstInstall";
    //用于第一次安装 上报
    public static String IS_SDK_FIRST_INSTALL_Report = "isSdkFirstInstallReport";
    public static String JY_DEVICE = "jy_device";


    public static String JY_CHANNEL = "jychannel";
    public static String AK_CHANNEL_ID = "AK_CHANNEL_ID";
    public static String JY_PROXY_SDK_DATAXML = "JYProxySdkDataXml";


    private SPDataUtils() {
    }

    public static void init(Context context) {
        if (sp == null) {
            synchronized (SPDataUtils.class) {
                if (sp == null) {
                    sp = new SPDataUtils();
                    shared = context.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
                    editor = shared.edit();
                }
            }
        }
    }

    public static SPDataUtils getInstance() {
        return sp;
    }

    public void savaString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public String getString(String key, String value) {
        return shared.getString(key, value);
    }

    public void savaBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return shared.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean value) {
        return shared.getBoolean(key, value);
    }

    public void savaLong(String key, long value) {

        editor.putLong(key, value).commit();
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value).commit();
    }


    public boolean setPreferences(int MODE, String fileName, String key, String value) {
        SharedPreferences sharedPreferences = ExtApp.app().getSharedPreferences(fileName, MODE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        boolean commit = edit.commit();
        return commit;
    }

    public String getPreferences(int MODE, String fileName, String key, String defValue) {
        SharedPreferences sharedPreferences = ExtApp.app().getSharedPreferences(fileName, MODE);
        return sharedPreferences.getString(key, defValue);
    }
}
