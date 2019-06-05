package com.jiyou.jycommonlib.tools;

import com.google.jygson.Gson;
import com.google.jygson.JsonNull;
import com.google.jygson.JsonSyntaxException;
import com.google.jygson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 */

public class GsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtils() {
    }

    public static <T> T parserGsonToObject(String json, Class<T> classOft) {
        return (new Gson()).fromJson(json, classOft);
    }

    /**
     * @param src :
     * @return :转化后的JSON数据
     * @Description : 将对象转为JSON串，此方法能够满足大部分需求
     */
    public static String toJson(Object src) {
        if (null == src) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        try {
            return gson.toJson(src);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将json转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * @param gsonString 需要转换的字符串，
     * @param <T>
     * @return 返回map类型数据，多用于不确定键值对类型
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * @param json
     * @param classOfT
     * @return
     * @Description : 用来将JSON串转为对象，但此方法不可用来转带泛型的集合
     */
    public static <T> Object fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, (Type) classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param json
     * @return
     * @Description : 用来将JSON串转为对象，此方法可用来转带泛型的集合，如：Type为 new
     * TypeToken<List<T>>(){}.getType()
     * ，其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
     */
    public static Object fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取json中的某个值，也可先将其转成键值对再获取
     *
     * @param json
     * @param key
     * @return
     */
    public static String getValue(String json, String key) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static Map<String, String> jsonToMap(JSONObject jsonObj) {
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

}
