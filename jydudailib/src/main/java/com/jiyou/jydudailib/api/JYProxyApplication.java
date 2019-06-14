package com.jiyou.jydudailib.api;

import android.content.Context;

import com.jiyou.jydudailib.config.ExtApp;
import com.jiyou.jydudailib.config.LoadConfig;
import com.jiyou.jydudailib.tools.SPDataUtils;
import com.jiyou.jygeneralimp.api.JYGApplication;

public class JYProxyApplication extends JYGApplication {
    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ExtApp.init(this);
        SPDataUtils.init(this.getApplicationContext());
//        LoadConfig.loadConfigJson(getApplicationContext());
        JYProxySDK.getInstance().instanceIDDLogic(LoadConfig.JY_DU_SDK_TYPE);
    }
}
