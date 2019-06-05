package com.jiyou.jydudailib.api;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.constants.JYDStatusCode;
import com.jiyou.jydudailib.api.model.JYDPayParam;
import com.jiyou.jydudailib.api.model.JYDRoleParam;
import com.jiyou.jydudailib.base.IDDLogic;
import com.jiyou.jydudailib.config.DuDaiUrlConstants;
import com.jiyou.jydudailib.http.HttpImp;
import com.jiyou.jydudailib.model.JYDuDaiGameUrlBean;
import com.jiyou.jydudailib.model.JYDuDaiOrderBean;
import com.jiyou.jydudailib.model.JYDuDaiTokenBean;
import com.jiyou.jydudailib.tools.GsonUtils;
import com.jiyou.jydudailib.tools.JYFactory;

public class JYProxySDK implements IDDLogic {
    private JYProxySDK() {
    }

    private volatile static JYProxySDK JYProxySDK;
    private IDDLogic iddLogic;

    public static JYProxySDK getInstance() {
        if (JYProxySDK == null) {
            synchronized (JYProxySDK.class) {
                if (JYProxySDK == null) {
                    JYProxySDK = new JYProxySDK();
                }
            }
        }
        return JYProxySDK;
    }

    public void instanceIDDLogic(String type) {
        iddLogic = (IDDLogic) JYFactory.newPluginNoParam(type);
//        iddLogic = new JYGLogicImpPlugin();
    }

    private IDDLogic getIDDLogicImp() {
        if (iddLogic == null) {
            synchronized (JYProxySDK.class) {
                if (iddLogic == null) {
                    iddLogic = new IDDLogic() {
                        @Override
                        public void onCreate(Context context, Bundle savedInstanceState) {

                        }

                        @Override
                        public void onStart(Context context) {

                        }

                        @Override
                        public void onRestart(Context context) {

                        }

                        @Override
                        public void onResume(Context context) {

                        }

                        @Override
                        public void onPause(Context context) {

                        }

                        @Override
                        public void onStop(Context context) {

                        }

                        @Override
                        public void onDestroy(Context context) {

                        }

                        @Override
                        public void onNewIntent(Context context, Intent newIntent) {

                        }

                        @Override
                        public void onSaveInstanceState(Context context, Bundle outState) {

                        }

                        @Override
                        public void onConfigurationChanged(Context context, Configuration newConfig) {

                        }

                        @Override
                        public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {

                        }

                        @Override
                        public boolean onBackPressed(Context context) {
                            return false;
                        }

                        @Override
                        public void init(Context context, JYDCallback callback) {

                        }

                        @Override
                        public void login(Context context, JYDCallback callback) {

                        }

                        @Override
                        public void logout(Context context, JYDCallback callback) {

                        }

                        @Override
                        public void pay(Context context, JYDPayParam payParam) {

                        }

                        @Override
                        public void createRole(Context context, JYDRoleParam param) {

                        }

                        @Override
                        public void enterGame(Context context, JYDRoleParam param) {

                        }

                        @Override
                        public void roleUpLevel(Context context, JYDRoleParam param) {

                        }

                        @Override
                        public void loginAuthNotify(Context context, String str) {

                        }

                        @Override
                        public void getGameUrl(Context context, JYDCallback callback) {

                        }
                    };
                }
            }
        }
        return iddLogic;
    }

    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        getIDDLogicImp().onCreate(context, savedInstanceState);
    }

    @Override
    public void onStart(Context context) {
        getIDDLogicImp().onStart(context);
    }

    @Override
    public void onRestart(Context context) {
        getIDDLogicImp().onRestart(context);
    }

    @Override
    public void onResume(Context context) {
        getIDDLogicImp().onResume(context);
    }

    @Override
    public void onPause(Context context) {
        getIDDLogicImp().onPause(context);
    }

    @Override
    public void onStop(Context context) {
        getIDDLogicImp().onStop(context);
    }

    @Override
    public void onDestroy(Context context) {
        getIDDLogicImp().onDestroy(context);
    }

    @Override
    public void onNewIntent(Context context, Intent newIntent) {
        getIDDLogicImp().onNewIntent(context, newIntent);
    }

    @Override
    public void onSaveInstanceState(Context context, Bundle outState) {
        getIDDLogicImp().onSaveInstanceState(context, outState);
    }

    @Override
    public void onConfigurationChanged(Context context, Configuration newConfig) {
        getIDDLogicImp().onConfigurationChanged(context, newConfig);
    }

    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        getIDDLogicImp().onActivityResult(context, requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed(Context context) {
        return getIDDLogicImp().onBackPressed(context);
    }

    @Override
    public void init(final Context context, final JYDCallback callback) {
        getIDDLogicImp().init(context, new JYDCallback() {
            @Override
            public void callback(int code, Object response) {
                if (code == JYDStatusCode.SUCCESS) {
                    callback.callback(code, response);
                    HttpImp.duDaiLog(context, DuDaiUrlConstants.URL_DUDAI_LOG, "activate");
                }
            }
        });

    }

    @Override
    public void login(final Context context, final JYDCallback callback) {
        getIDDLogicImp().login(context, new JYDCallback() {
            @Override
            public void callback(int code, Object response) {
                callback.callback(code, response);
            }
        });
    }

    @Override
    public void logout(Context context, JYDCallback callback) {
        getIDDLogicImp().logout(context, callback);
    }

    @Override
    public void pay(final Context context, final JYDPayParam payParam) {
        HttpImp.duDaiOrderMethod(context, DuDaiUrlConstants.URL_DUDAI_ORDER, payParam, new JYDCallback<JYDuDaiOrderBean>() {
            @Override
            public void callback(int code, JYDuDaiOrderBean bean) {
                if (code == JYDStatusCode.SUCCESS) {
                    payParam.setCpBill(bean.getData().getOrder_sn());
                    getIDDLogicImp().pay(context, payParam);
                }
            }
        });
    }


    @Override
    public void createRole(Context context, JYDRoleParam param) {
        getIDDLogicImp().createRole(context, param);
        HttpImp.duDaiLog(context, DuDaiUrlConstants.URL_DUDAI_LOG, "create_role");
    }

    @Override
    public void enterGame(Context context, JYDRoleParam param) {
        getIDDLogicImp().enterGame(context, param);
        HttpImp.duDaiLog(context, DuDaiUrlConstants.URL_DUDAI_LOG, "enter_game");
    }

    @Override
    public void roleUpLevel(Context context, JYDRoleParam param) {
        getIDDLogicImp().roleUpLevel(context, param);
        HttpImp.duDaiLog(context, DuDaiUrlConstants.URL_DUDAI_LOG, "roleu_pLevel");
    }

    @Override
    public void loginAuthNotify(Context context, String str) {
        JYDuDaiTokenBean bean = GsonUtils.GsonToBean(str, JYDuDaiTokenBean.class);
        if (bean.getState() == 1) {
            HttpImp.duDaiLog(context, DuDaiUrlConstants.URL_DUDAI_LOG, "login");
        }
    }

    @Override
    public void getGameUrl(Context context, final JYDCallback callback) {
        HttpImp.duDaiGameUrl(context, DuDaiUrlConstants.URL_DUDAI_GAME_URL, new JYDCallback<JYDuDaiGameUrlBean>() {
            @Override
            public void callback(int code, JYDuDaiGameUrlBean bean) {
                if (code == JYDStatusCode.SUCCESS) {
                    callback.callback(JYDStatusCode.SUCCESS, bean.getData().getGame_url());
                }
            }
        });
    }
}
