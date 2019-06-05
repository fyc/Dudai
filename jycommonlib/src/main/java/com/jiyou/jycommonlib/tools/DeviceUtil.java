
package com.jiyou.jycommonlib.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.jiyou.jycommonlib.config.ExtApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


public class DeviceUtil {

    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static final String PREFS_ADINFO_ID = "adinfo_id";
    protected static UUID uuid;
    private static String _versionCode = "";
    private static String _versionName = "";
    private static String _appName = "";
    private static String _imei = "";
    private static Context mCtx = null;
    private static String _imei1 = "";
    private static String _imei2 = "";
    private static String _meid = "";

    public static void initDeviceInfo(Context context) {
        mCtx = context;
        getApplicationName();
        getVersionCode();
        getVersionName();
        getImei();
    }

    public static String getOneDeviceId() { //一键注册使用
        String OneId = "";
        try {
            OneId = AppUtil.getLocalConfigData(AppUtil.AKCONFIG_FILE, "onekeyregisterid", "");
            if (OneId != null && OneId.equals("")) { //一键注册为空处理
                String mac = getMac();
                String uniqueId = getUniquePsuedoID();
                String key = _imei + mac + uniqueId;
                OneId = MD5Util.getMd5(key);
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, "onekeyregisterid", OneId);
            } else if (OneId == null) {
                String mac = getMac();
                String uniqueId = getUniquePsuedoID();
                String key = _imei + mac + uniqueId;
                OneId = MD5Util.getMd5(key);
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, "onekeyregisterid", OneId);
            }
            return OneId;
        } catch (Exception e) {
//            JYLogUtil.e("getOneDeviceId error");
        }
        return OneId;
    }

    public static String getAppDeviceId() {
        String strDevcie = SPDataUtils.getInstance().getString(SPDataUtils.JY_DEVICE, "");
        if (strDevcie.equals("")) {
            //deviceId +当前时间
            String defValue = getOneDeviceId() + System.currentTimeMillis();
            SPDataUtils.getInstance().savaString(SPDataUtils.JY_DEVICE, defValue);
            return defValue;
        }
        return strDevcie;
    }

    public static String getMacLocal() {
        String str = "";
        String macSerial = "";
        try {
            if (mCtx != null) {
                macSerial = getLocalMacAddress(mCtx);
                if (macSerial != null && macSerial.equals("02:00:00:00:00:00")) {
                    macSerial = "";
                }
            }

            if (macSerial == null || "".equals(macSerial)) {
                try {
                    Process pp = Runtime.getRuntime().exec(
                            "cat /sys/class/net/wlan0/address");
                    InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                    LineNumberReader input = new LineNumberReader(ir);

                    for (; null != str; ) {
                        str = input.readLine();
                        if (str != null) {
                            macSerial = str.trim();// 去空格
                            break;
                        }
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    JYLogUtil.e("Not Found /sys/class/net/wlan0/address");
                }
            }
            if (macSerial == null || "".equals(macSerial)) {
                try {
                    return loadFileAsString("/sys/class/net/eth0/address")
                            .toUpperCase().substring(0, 17);
                } catch (Exception e) {
                    macSerial = getLocalMacAddress();
                    JYLogUtil.e("Not Found /sys/class/net/eth0/address");
                }
            }

            if (macSerial == null || macSerial.equals("02:00:00:00:00:00") || macSerial.equals("")) {
                macSerial = getUniquePsuedoID(); //如果没用的WiFi信息就设置uiq
            }
        } catch (Exception e) {
            macSerial = getUniquePsuedoID();
        }

        return macSerial;
    }

    public static String getMac() {
        String macKey = "MacLocal";
        String OneId = "";
        try {
            OneId = AppUtil.getLocalConfigData(AppUtil.AKCONFIG_FILE, macKey, "");
            if (OneId != null && OneId.equals("")) { //一键注册为空处理
                OneId = getMacLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, macKey, OneId);
            } else if (OneId == null) {
                OneId = getMacLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, macKey, OneId);
            }
            return OneId;
        } catch (Exception e) {
            OneId = getMacLocal();
            JYLogUtil.e("getMac error");
        }
        return OneId;
    }


    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    public static String getLocalMacAddress(Context context) {
        return getLocalMacAddress();
    }

    public static String getLocalMacAddress() {
        String permission = "android.permission.ACCESS_WIFI_STATE";
        if (ExtApp.app().getApplicationContext() == null) {
            return "";
        }
        if (!AppInfo.checkPermission(ExtApp.app().getApplicationContext(), permission)) {
            JYLogUtil.e("!!!MISSING permission [" + permission + "]");
            return "";
        } else {
            WifiManager wifi = (WifiManager) ExtApp.app().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        }
    }

    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImeiLocal() {
        if (!_imei.equals("")) {
            return _imei;
        }
        if (AppInfo.checkPermission(ExtApp.app(), "android.permission.READ_PHONE_STATE")) {
            if (AppInfo.checkPhoneState(ExtApp.app())) {
                TelephonyManager tm = (TelephonyManager) ExtApp.app().getSystemService(Context.TELEPHONY_SERVICE);
                _imei = tm.getDeviceId();
            }

            if (_imei != null && !_imei.equals("")) {
                return _imei;
            } else {
                _imei = getOneDeviceId();
                return _imei;
            }
        } else {
            _imei = getOneDeviceId();
            return _imei;
        }
    }

    public static String getAndroid_Id() {
        try {
            String andid = Secure.getString(ExtApp.app().getContentResolver(), "android_id");
            return andid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "unknown";
        }
    }

    public static String getImei() {
        String imeiKey = "ImeiLocal";
        String OneId = "";
        try {
            OneId = AppUtil.getLocalConfigData(AppUtil.AKCONFIG_FILE, imeiKey, "");
            if (OneId != null && OneId.equals("")) { //一键注册为空处理
                OneId = getImeiLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, imeiKey, OneId);
            } else if (OneId == null) {
                OneId = getImeiLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, imeiKey, OneId);
            }
            return OneId;
        } catch (Exception e) {
            OneId = getImeiLocal();
            JYLogUtil.e("getImei error");
        }
        return OneId;
    }

    public static UUID getDeviceUuid(Context context) {
        if (uuid == null) {
            synchronized (DeviceUtil.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Secure.getString(context.getContentResolver(),
                                Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context
                                        .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId
                                        .getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
        return uuid;
    }

    public static String getSystemVer() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceNameLocal() {
        return Build.MODEL;
    }

    public static String getDeviceName() {
        String deviceNameKey = "DeviceNameLocal";
        try {
            String OneId = AppUtil.getLocalConfigData(AppUtil.AKCONFIG_FILE, deviceNameKey, "");
            if (OneId != null && OneId.equals("")) { //一键注册为空处理
                OneId = getDeviceNameLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, deviceNameKey, OneId);
            } else if (OneId == null) {
                OneId = getDeviceNameLocal();
                AppUtil.saveLocalConfigData(AppUtil.AKCONFIG_FILE, deviceNameKey, OneId);
            }
            return OneId;
        } catch (Exception e) {
            JYLogUtil.e("getImei error");
        }
        return "";
    }

    /**
     * 获取SN
     *
     * @return
     */
    public static String getSn(Context ctx) {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception ignored) {

        }

        return serial;
    }

    /**
     * 获取手机序列号
     *
     * @return Serial Number
     */
    public static String getDeviceSerial() {
        try {
            if (AppInfo.checkPhoneState(ExtApp.app())) {
                return Build.SERIAL;
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //通过反射获取imei1和imei2， meid电信
    public static Map getImeiAndMeid(Context ctx) {
        Map<String, String> map = new HashMap<String, String>();
        if (AppInfo.checkPhoneState(ExtApp.app())) {
            TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = null;
            try {
                method = manager.getClass().getMethod("getDeviceId", int.class);
                _imei1 = manager.getDeviceId();
                _imei2 = (String) method.invoke(manager, 1);
                _meid = (String) method.invoke(manager, 2);
                map.put("imei1", _imei1);
                map.put("imei2", _imei2);
                map.put("meid", _meid);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //手机卡1
    public static String getImei1() {
        return _imei1;
    }

    //手机卡2
    public static String getImei2() {
        return _imei2;
    }

    //手机电信meid
    public static String getMeid() {
        return _meid;
    }

    public static String getAppName() {
        if (_appName.equals("")) {
            try {
                String _appName = getApplicationName();
            } catch (Exception e) {
            }
        }
        return _appName;
    }

    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = ExtApp.app().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(ExtApp.app().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        _appName = applicationName;
        return applicationName;
    }


    public static String getVersionName() {
        if (_versionName.equals("")) {
            try {
                PackageManager packageManager = ExtApp.app().getPackageManager();
                PackageInfo packInfo = packageManager.getPackageInfo(ExtApp.app().getPackageName(), 0);
                _versionName = packInfo.versionName;
            } catch (Exception e) {
            }
        }
        return _versionName;
    }

    public static String getVersionCode() {
        if (_versionCode.equals("")) {
            try {
                PackageManager packageManager = ExtApp.app().getPackageManager();
                PackageInfo packInfo = packageManager.getPackageInfo(ExtApp.app().getPackageName(), 0);
                _versionCode = String.valueOf(packInfo.versionCode);
            } catch (Exception e) {
                _versionCode = "";
            }
        }
        return _versionCode;
    }

    private static float density = -1F;
    private static int widthPixels = -1;
    private static int heightPixels = -1;

    public static float getDensity() {
        if (density <= 0F) {
            density = ExtApp.app().getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5F);
    }

    public static int getScreenWidth(Context ctx) {
        if (ctx == null) {
            widthPixels = ExtApp.app().getResources().getDisplayMetrics().widthPixels;
        } else {
            widthPixels = ctx.getResources().getDisplayMetrics().widthPixels;
        }
        return widthPixels;
    }


    public static int getScreenHeight(Context ctx) {
        if (ctx == null) {
            heightPixels = ExtApp.app().getResources().getDisplayMetrics().heightPixels;
        } else {
            heightPixels = ctx.getResources().getDisplayMetrics().heightPixels;
        }
        return heightPixels;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        int currTime = (int) (System.currentTimeMillis() / 1000);
        return String.valueOf(currTime);
    }

    private static final int NET_TYPE_UNKNOW = 0;
    private static final int NET_TYPE_WIFI = 1;
    private static final int NET_TYPE_2G = 2;
    private static final int NET_TYPE_3G = 3;
    private static final int NET_TYPE_4G = 4;
    private static final int NET_TYPE_OTHER = 5;

    public static String getNetWork() {
        switch (getNetType(ExtApp.app().getApplicationContext())) {
            case NET_TYPE_UNKNOW:
                return "未知";
            case NET_TYPE_WIFI:
                return "wifi";
            case NET_TYPE_2G:
                return "2g";
            case NET_TYPE_3G:
                return "3g";
            case NET_TYPE_4G:
                return "4g";
            case NET_TYPE_OTHER:
                return "其它";
            default:
                return "未知";
        }
    }

    public static String getWifiName() {
        WifiManager wifiManager = (WifiManager) ExtApp.app().getApplicationContext().getSystemService(ExtApp.app().WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    @SuppressLint("ServiceCast")
    public static int getNetType(Context context) {
        ConnectivityManager connectionManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NET_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (networkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return NET_TYPE_2G;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return NET_TYPE_3G;
                    case 13:
                        return NET_TYPE_4G;
                    default:
                        String subTypeName = networkInfo.getSubtypeName();
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA")
                                || subTypeName.equalsIgnoreCase("WCDMA")
                                || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            return NET_TYPE_3G;
                        } else {
                            return NET_TYPE_OTHER;
                        }
                }
            } else {
                return NET_TYPE_OTHER;
            }
        } else {
            return NET_TYPE_UNKNOW;
        }
    }

    public static String getAndroidId(Context context) {
        String androidId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        return androidId;
    }

    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getSystemRom() {
        String SYS = "";
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                SYS = SYS_EMUI;//华为
            } else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
                SYS = SYS_FLYME;//魅族
            }
            ;
        } catch (Exception e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * Converts this this string to upper case, using the rules of {@code locale}.
     *
     * @param s
     * @return
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase(Locale.getDefault());
    }

}
