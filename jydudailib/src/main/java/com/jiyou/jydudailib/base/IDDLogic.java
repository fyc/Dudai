package com.jiyou.jydudailib.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.model.JYDPayParam;
import com.jiyou.jydudailib.api.model.JYDRoleParam;

public interface IDDLogic {
    void onCreate(Context context, Bundle savedInstanceState);

    void onStart(Context context);

    void onRestart(Context context);

    void onResume(Context context);

    void onPause(Context context);

    void onStop(Context context);

    void onDestroy(Context context);

    void onNewIntent(Context context, Intent newIntent);

    void onSaveInstanceState(Context context, Bundle outState);

    void onConfigurationChanged(Context context, Configuration newConfig);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

    boolean onBackPressed(Context context);


    //游戏初始化:
    void init(Context context, JYDCallback callback);

    //登录:
    void login(Context context, JYDCallback callback);

    //退出
    void logout(Context context, JYDCallback callback);

    //支付:
    void pay(Context context, JYDPayParam payParam);


    void createRole(Context context, JYDRoleParam param);

    void enterGame(Context context, JYDRoleParam param);

    void roleUpLevel(Context context, JYDRoleParam param);
    //登录验证结果通知
    void loginAuthNotify(Context context, String str);

    void getGameUrl(Context context, JYDCallback callback);
}
