package com.jiyou.jydudailib.config;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

public class ExtApp {
    private static boolean debug;
    private static Application app;

    private ExtApp() {
    }


    public static void init(Application app) {
        if (ExtApp.app == null) {
            ExtApp.app = app;
        }
    }
    public static Application app() {
        if (ExtApp.app == null) {
            try {
                // 在IDE进行布局预览时使用
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                ExtApp.app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate()"
                        + " and register your Application in manifest.");
            }
        }
        return ExtApp.app;
    }

    public static void setDebug(boolean debug) {
        ExtApp.debug = debug;
    }

    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }

}


