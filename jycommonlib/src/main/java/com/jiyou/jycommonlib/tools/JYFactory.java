
package com.jiyou.jycommonlib.tools;

import android.content.Context;

import java.lang.reflect.Constructor;

public class JYFactory {

    /**
     * 实例化插件
     */
    public static Object newPluginContext(String pluginName, Context context) {

        try {
            Class clazz = Class.forName(pluginName);
            if(clazz!=null) {
                Constructor constructor = clazz.getDeclaredConstructor(new Class[]{
                        Context.class
                });
                return constructor.newInstance(new Object[]{
                        context
                });
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static Object newPluginNoParam(String pluginName){
        try {
            Class clazz = Class.forName(pluginName);
            if(clazz!=null) {
                Constructor constructor = clazz.getDeclaredConstructor(new Class[]{});
                return constructor.newInstance(new Object[]{});
            }
        } catch (Exception ex) {
        }
        return null;
    }


}
