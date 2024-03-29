package com.jiyou.jydudailib.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jiyou.jydudailib.config.JYDRoleParamManager;
import com.jiyou.jydudailib.config.LoadConfig;
import com.jiyou.jydudailib.tools.AndroidUtil;
import com.jiyou.jydudailib.tools.DeviceUtil;
import com.jiyou.jydudailib.tools.GsonUtils;
import com.jiyou.jydudailib.tools.MD5Util;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class DuParamHelper {
    public static String CP_SECRET = "4ad153cd297acb33ef907f1fa13319be";
    //    public static String GAME_SECRET = "36251974e5a3909ba148bb47d6477156";
    public static String GAME_SECRET = "3ac8af80e6e0e0b3eb0a8a84a02ad400";
    public static String GAME_SECRET_TEst = "2702b00c0bbfa49b6c052606de78102d";
    private DuParamHelper() {

    }

    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        public static final DuParamHelper INSTANCE = new DuParamHelper();
    }

    public static DuParamHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /*公共参数*/
    public static SortedMap<String, String> duDaiMapParam(Context context) {
        SortedMap paramObj = new TreeMap<>();
        SortedMap agentMap = new TreeMap<>();
        agentMap.put("channel_id", LoadConfig.JY_CHANNEL_ID);
        agentMap.put("cp_id", LoadConfig.JY_CP_ID);
        paramObj.put("agent", agentMap);

        SortedMap gameMap = new TreeMap<>();
        gameMap.put("id", LoadConfig.JY_GAMEID);
        gameMap.put("name", LoadConfig.JY_GAMENAME);
        gameMap.put("version", LoadConfig.JY_GAME_VERSION);
        paramObj.put("game", gameMap);

        SortedMap roleMap = new TreeMap<>();
        roleMap.put("id", JYDRoleParamManager.onEnterRoleInfo.getRoleId());
        roleMap.put("name", JYDRoleParamManager.onEnterRoleInfo.getRoleName());
        roleMap.put("level", JYDRoleParamManager.onEnterRoleInfo.getRoleLevel());
        roleMap.put("server_id", Integer.valueOf(JYDRoleParamManager.onEnterRoleInfo.getServerId()));
        roleMap.put("server_name", JYDRoleParamManager.onEnterRoleInfo.getServerName());
        paramObj.put("role", roleMap);

        SortedMap deviceMap = new TreeMap<>();
        deviceMap.put("device_id", DeviceUtil.getDeviceUuid(context));
        deviceMap.put("screen_height", AndroidUtil.getPxHeight(context));
        deviceMap.put("screen_width", AndroidUtil.getPxWidth(context));
        deviceMap.put("ios_idfa", "00000000-0000-0000-0000-000000000000");
        deviceMap.put("android_imei", DeviceUtil.getImei());
        deviceMap.put("android_adv_id", "unknown");
        deviceMap.put("android_id", DeviceUtil.getAndroid_Id());
        deviceMap.put("device_name", AndroidUtil.getDeviceName());
        deviceMap.put("os_version", TextUtils.isEmpty(Build.VERSION.RELEASE) ? "unknown" : Build.VERSION.RELEASE);
        ;
//        deviceMap.put("sdk_version", TextUtils.isEmpty(String.valueOf(Build.VERSION.SDK_INT)) ? "unknown" : String.valueOf(Build.VERSION.SDK_INT));
        deviceMap.put("sdk_version", "1.0.3");
        deviceMap.put("os_type", "android");
        deviceMap.put("net_type", DeviceUtil.getNetWork());
        deviceMap.put("user_agent", AndroidUtil.getUserAgent(context));
        deviceMap.put("imsi", AndroidUtil.getPhoneIMSI(context));
        deviceMap.put("wifi_name", "<unknown ssid>");
        deviceMap.put("bluetooth", AndroidUtil.getBtAddressByReflection());
        deviceMap.put("mac", DeviceUtil.getMac());
        deviceMap.put("brand", TextUtils.isEmpty(Build.BRAND) ? "unknown" : Build.BRAND);
        deviceMap.put("model", TextUtils.isEmpty(Build.MODEL) ? "unknown" : Build.MODEL);
        deviceMap.put("battery_level", AndroidUtil.getBatteryLevel(context));
        deviceMap.put("screen_brightness", AndroidUtil.getScreenBrightness(context));
        deviceMap.put("cpu_type", TextUtils.isEmpty(Build.CPU_ABI) ? "unknown" : Build.CPU_ABI);
        deviceMap.put("inner_ip", AndroidUtil.getIPAddress(context));
        deviceMap.put("is_root", AndroidUtil.isRoot());
        deviceMap.put("orientation_sensor", AndroidUtil.getOritation());
        deviceMap.put("package_name", AndroidUtil.getPackageName(context));
        paramObj.put("device", deviceMap);

        paramObj.put("client_time_zone", AndroidUtil.getCurrentTimeZone());
        paramObj.put("client_ts", System.currentTimeMillis()/1000);
        return paramObj;
    }

    public static String duDaiCreateSign(String characterEncoding, SortedMap<String, String> parameters, String dudaiKey) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();

            if ("".equals(sb.toString())) {
                if (v instanceof Map) {
                    v = GsonUtils.toJson(v);
                }
                sb.append(k + "=" + v);
            } else {
                if (null != v && !"sign".equals(k)) {
                    if (v instanceof Map) {
                        v = GsonUtils.toJson(v);
                    }
                    sb.append("&" + k + "=" + v);
                }
            }
        }
        sb.append(dudaiKey);
//        String str = decode2(sb.toString());
//        String str = (String) filter(sb.toString());
        String sign = MD5Util.getMd5(sb.toString(), characterEncoding).toLowerCase();
        return sign;
    }

    public static String createUrl(String characterEncoding, SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();

            if ("".equals(sb.toString())) {//&&!v.equals("")
                sb.append(k + "=" + toURLEncoded(v.toString()));
            } else {
                if (null != v && !"sign".equals(k)) {//&&!v.equals("")
                    sb.append("&" + k + "=" + toURLEncoded(v.toString()));
                }
            }
        }
        return sb.toString();
    }

    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.d("", "toURLEncoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            //Log.e("toURLEncoded error:"+paramString, localException.printStackTrace());
        }

        return "";
    }

//    public static CharSequence filter(CharSequence source) {
//
//        String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//
//        Pattern pattern = Pattern.compile(speChat);
//
//        Matcher matcher = pattern.matcher(source.toString());
//
//        if (matcher.find())
//            return "";
//        else
//            return null;
//
//    }

    public static String filter(String keyword) {
        if (keyword != "") {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

}