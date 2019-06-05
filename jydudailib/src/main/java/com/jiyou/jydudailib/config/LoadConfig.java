package com.jiyou.jydudailib.config;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoadConfig {
    public static String JY_DU_SDK_TYPE = "";
    public static int JY_GAMEID = 1;
    public static int JY_CHANNEL_ID = 1;
    public static int JY_CP_ID = 1;
    public static String JY_GAMENAME = "";
    public static String JY_GAME_VERSION = "1";
    public static String JY_DUDAI_HOST = "";

    public static void loadConfigJson(Context context) {
        String ConfigFile = "jydudai_config.json";

        String strCfg = readFileToString(context, ConfigFile);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(strCfg);
            Map map = jsonToMap(jsonObj);
            JY_DU_SDK_TYPE = (String) map.get("JY_DU_SDK_TYPE");
            JY_CHANNEL_ID = (int) map.get("JY_CHANNEL_ID");
            JY_GAMEID = (int) map.get("JY_GAMEID");
            JY_CP_ID = (int) map.get("JY_CP_ID");
            JY_GAMENAME = (String) map.get("JY_GAMENAME");
            JY_GAME_VERSION = (String) map.get("JY_GAME_VERSION");
            JY_DUDAI_HOST = (String) map.get("JY_DUDAI_HOST");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String readFileToString(Context mContext, String fileName) {
        String jo = "";
        try {
            InputStream inStream = mContext.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inStream, "UTF-8"));
            String str = br.readLine();
            while (str != null) {
                jo = jo + str;
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jo.replace("/n", "").replace(" ", "").replace("/t", "");
        return jo;
    }

    private static Map<String, String> jsonToMap(JSONObject jsonObj) {
        Map map = new HashMap();
        Iterator keys = jsonObj.keys();
        JSONObject jo = null;
        JSONArray ja = null;

        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                Object o = jsonObj.get(key);
                if ((o instanceof String)) {
                    map.put(key, (String) o);
                } else if ((o instanceof JSONObject)) {
                    jo = (JSONObject) o;
                    if (jo.keys().hasNext())
                        map = setMap(jsonToMap(jo), map);
                } else if ((o instanceof JSONArray)) {
                    ja = (JSONArray) o;
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if (jo.keys().hasNext())
                            map = setMap(jsonToMap(jo), map);
                    }
                } else {
                    map.put(key, o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return map;
    }

    private static Map<String, String> setMap(Map<String, String> map1, Map<String, String> map2) {
        if (map2 == null) {
            map2 = new HashMap();
        }
        for (String key : map1.keySet()) {
            String value = (String) map1.get(key);
            map2.put(key, value);
        }

        return map2;
    }
}
