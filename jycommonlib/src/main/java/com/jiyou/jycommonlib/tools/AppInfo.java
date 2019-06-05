package com.jiyou.jycommonlib.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jiyou.jycommonlib.config.ExtApp;

import java.util.ArrayList;
import java.util.List;

public class AppInfo {
    /*
     * 获取应用名
     */
    public static String getVersionName(){
        String versionName = null;
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            //获取packageInfo
            PackageInfo info = pm.getPackageInfo(ExtApp.app().getPackageName(), 0);
            //获取versionName
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }
    /*
     * 获取应用版本
     */
    public static int getVersionCode(){

        int versionCode = 0;
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            //获取packageInfo
            PackageInfo info = pm.getPackageInfo(ExtApp.app().getPackageName(), 0);
            //获取versionCode
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    //获取名字
    public static String getAppName(){

        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(ExtApp.app().getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {}
        return "";
    }

    //包名
    public static String getAppPackage(){
        return ExtApp.app().getPackageName();
    }

//    public static Drawable getAppIcon(){
//        try {
//            //获取包管理者
//            PackageManager pm = AkSDK.getInstance().getActivity().getPackageManager();
//            ApplicationInfo info = pm.getApplicationInfo(getAppPackage(), 0);
//
//            return info.loadIcon(pm);
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static int getAppIconId(){
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(getAppPackage(), 0);

            return info.icon;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }


    public static int getAppLogo(){
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(getAppPackage(), 0);

            return info.logo;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * 获取程序的权限
     */
    public static String[] getAppPremission(String packname){
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /*
     * 检查程序的权限
     */
    public static boolean checkPermission(Context ctx, String strpermission){
            PackageManager localPackageManager = ctx.getPackageManager();
            return localPackageManager.checkPermission(strpermission, ctx.getPackageName()) == 0;
    }

    public static boolean checkPhoneState(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) == 0;
    }

    /*
     * 获取程序的签名
     */
    public static String getAppSignature(String packname){
        try {
            //获取包管理者
            PackageManager pm = ExtApp.app().getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
            return true;
        return false;
    }

    public static boolean SupportAndroidVersion(){
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        // This work only for android 4.0+
        if(curApiVersion >= 16){
            return true;
        }
        return false;
    }
//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
//    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.INTERNET" />

    public final static List<Class> getActivitiesClass(List<Class> excludeList){

        List<Class> returnClassList = new ArrayList<Class>();
        try {
            //Get all activity classes in the AndroidManifest.xml
            PackageInfo packageInfo = ExtApp.app().getPackageManager().getPackageInfo(getAppPackage(), PackageManager.GET_ACTIVITIES);
            if(packageInfo.activities!=null){
                //Log.d(TAG, "Found "+packageInfo.activities.length +" activity in the AndrodiManifest.xml");
                for(ActivityInfo ai: packageInfo.activities){
                    Class c;
                    try {
                        c = Class.forName(ai.name);
                        // Maybe isAssignableFrom is unnecessary
                        if(Activity.class.isAssignableFrom(c)){
                            returnClassList.add(c);
                            //Log.d(TAG, ai.name +"...OK");
                        }
                    } catch (ClassNotFoundException e) {
                        //Log.d(TAG, "Class Not Found:"+ai.name);
//                                                e.printStackTrace();
                    }
                }
                //Log.d(TAG, "Filter out, left "+ returnClassList.size()  +" activity," + Arrays.toString(returnClassList.toArray()));

                //Exclude some activity classes
                if(excludeList!=null){
                    returnClassList.removeAll(excludeList);
                    //Log.d(TAG, "Exclude " + excludeList.size() +" activity,"+ Arrays.toString(excludeList.toArray()));
                }
                //Log.d(TAG, "Return "+ returnClassList.size()  +" activity," + Arrays.toString(returnClassList.toArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnClassList;
    }
}
