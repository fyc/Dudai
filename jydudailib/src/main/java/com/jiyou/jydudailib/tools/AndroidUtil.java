package com.jiyou.jydudailib.tools;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 * @desc 手机信息 & MAC地址 & 开机时间 & ip地址 &手机的imei、imsi等号码
 * @auth 方毅超
 * @time 2017/10/24 11:27
 */

public class AndroidUtil {
    /**
     * MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
 /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，
 不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，
 这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 获得IP地址，分为两种情况，一是wifi下，二是移动网络下，得到的ip地址是不一样的
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public static String getIPAddress(Context context) {
        String address = "0.0.0.0";
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                address = inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //调用方法将int转换为地址字符串
                address = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            address = "0.0.0.0";
        }
        return TextUtils.isEmpty(address) ? "0.0.0.0" : address;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取 ANDROID_ID
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        return h + ":" + m;
    }

    /**
     * 手机信息
     * 需要 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @return
     */
    public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID); // Either  a changelist number, or a label like "M4-rc20".
        sb.append("\nBRAND              :").append(Build.BRAND); //品牌名 如 Xiaomi
        sb.append("\nMODEL              :").append(Build.MODEL); //手机型号
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE); //frimware版本(系统版本) 如：2.1-update1
        sb.append("\nSDK                :").append(Build.VERSION.SDK); //sdk版本号

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD); //基板名 如 MSM8974
        sb.append("\nPRODUCT            :").append(Build.PRODUCT); //The name of the overall product.
        sb.append("\nDEVICE             :").append(Build.DEVICE); //品牌型号名，如小米4对应cancro
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT); //包含制造商，设备名，系统版本等诸多信息 如  Xiaomi/cancro_wc_lte/cancro:6.0.1/MMB29M/V8.1.3.0.MXDCNDI:user/release-keys
        sb.append("\nHOST               :").append(Build.HOST); // 如 c3-miui-ota-bd43
        sb.append("\nTAGS               :").append(Build.TAGS); //Comma-separated tags describing the build, like "unsigned,debug".
        sb.append("\nTYPE               :").append(Build.TYPE); //The type of build, like "user" or "eng".
        sb.append("\nTIME               :").append(Build.TIME); //当前时间，毫秒值
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY); // 如 MMB29M
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER); // The manufacturer of the product/hardware. 如 Xiaomi
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER); //The system bootloader version number. 如
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI); // 如 armeabi-v7a
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2); // 如 armeabi
            sb.append("\nHARDWARE           :").append(Build.HARDWARE); // The name of the hardware (from the kernel command line or /proc). 如 qcom
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN); // Value used for when a build property is unknown.
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append("\nSERIAL             :").append(Build.SERIAL); // A hardware serial number, if available. 如 abcdefgh
        }
        return sb.toString();
    }

    /**
     * 获取手机的IMEI号码
     * 使用TelephonyManager时需要 <uses-permission android:name="READ_PHONE_STATE" />
     */
    public static String getPhoneIMEI(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "unknown";
        }
        String imei = mTm.getDeviceId();
        return imei;
    }

    /**
     * 获取手机的imsi号码
     * 使用TelephonyManager时需要 <uses-permission android:name="READ_PHONE_STATE" />
     */
    public static String getPhoneIMSI(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "unknown";
        }
        String imsi = mTm.getSubscriberId();
        return TextUtils.isEmpty(imsi) ? "unknown" : imsi;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getPxWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getPxHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static String getUserAgent(Context context) {
        String useragent = new WebView(context).getSettings().getUserAgentString();
        return TextUtils.isEmpty(useragent) ? "unknown" : useragent;
    }

    public static String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) return "00:00:00:00:00:00";
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return "00:00:00:00:00:00";
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if (method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if (obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "00:00:00:00:00:00";
    }

    public static int getBatteryLevel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) context.getSystemService(context.BATTERY_SERVICE);
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(context.getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
    }

    /**
     * 获取屏幕亮度
     */

    public static int getScreenBrightness(Context context) {
        int value = -1;
        ContentResolver cr = context.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
        }
        return value;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static int isRoot() {
        int bool = 0;
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = 0;
            } else {
                bool = 1;
            }
        } catch (Exception e) {
        }
        return bool;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String s = tz.getDisplayName(false, TimeZone.SHORT);
        return s;
    }

    /**
     * 获取手机旋转角度
     */
    public static String getOritation() {
        /**
         * 初始化传感器
         * */
//        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        //获取Sensor
//        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //初始化数组
        float[] gravity = new float[3];//用来保存加速度传感器的值
        float[] r = new float[9];//
        float[] geomagnetic = new float[3];//用来保存地磁传感器的值
        float[] values = new float[3];//用来保存最终的结果

        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        double degreeZ = Math.toDegrees(values[0]);
        double degreeX = Math.toDegrees(values[1]);
        double degreeY = Math.toDegrees(values[2]);
        return degreeX + "," + degreeY + "," + degreeZ;
    }

    public static String getDeviceName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        if (myDevice == null) return "unknown";
        String deviceName = myDevice.getName();
        return TextUtils.isEmpty(deviceName) ? "unknown" : deviceName;
    }
//    public static String getDeviceName() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + " " + model;
//        }
//    }
//
//    private static String capitalize(String s) {
//        if (s == null || s.length() == 0) {
//            return "";
//        }
//        char first = s.charAt(0);
//        if (Character.isUpperCase(first)) {
//            return s;
//        } else {
//            return Character.toUpperCase(first) + s.substring(1);
//        }
//    }
}

