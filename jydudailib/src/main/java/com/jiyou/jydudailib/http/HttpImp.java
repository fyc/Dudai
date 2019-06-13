package com.jiyou.jydudailib.http;

import android.app.Activity;
import android.content.Context;

import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.constants.JYDStatusCode;
import com.jiyou.jydudailib.api.model.JYDPayParam;
import com.jiyou.jydudailib.config.DuUrlConstants;
import com.jiyou.jydudailib.config.LoadConfig;
import com.jiyou.jydudailib.model.JYDuGameUrlBean;
import com.jiyou.jydudailib.model.JYDuOrderBean;
import com.jiyou.jydudailib.tools.GsonUtils;
import com.jiyou.jydudailib.tools.ProgressBarUtil;
import com.jiyou.jydudailib.tools.ToastUtil;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

public class HttpImp {

    /**
     * 独代验证
     */
    public static void token(final Context context, String url, String auth_code) {
        SortedMap paramObj = new TreeMap<>();
        paramObj.put("auth_code", auth_code);
        paramObj.put("game_id", LoadConfig.JY_GAMEID);
        paramObj.put("channel_id", LoadConfig.JY_CHANNEL_ID);
        paramObj.put("cp_id", LoadConfig.JY_CP_ID);
        paramObj.put("game_name", LoadConfig.JY_GAMENAME);
        paramObj.put("game_version", LoadConfig.JY_GAME_VERSION);
        paramObj.put("ts", System.currentTimeMillis());
        String sign = DuParamHelper.duDaiCreateSign("UTF-8", paramObj, DuParamHelper.CP_SECRET);
        paramObj.put("sign", sign);

        HttpRequestUtil.okPostJsonRequest(url, paramObj, new HttpRequestUtil.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
//                JYSdkLoginBean loginBean = GsonUtils.GsonToBean(result, JYSdkLoginBean.class);
//                int state = loginBean.getState();
//                if (state == 1) {
//                    UserAccountManager.parseSuccessLogin(context, loginBean, loginBean.getData().getUsername(), loginBean.getData().getPassword(), false);
//                    Delegate.getListener().callback(JYDStatusCode.SUCCESS, result);
//                } else {
//                    ToastUtil.showLongHideSoftInput(context,"登录失败");
//                }
            }

            @Override
            public void requestFailure(String request, IOException e) {
                ToastUtil.showLongHideSoftInput(context, "登录失败");
            }

            @Override
            public void requestNoConnect(String msg, String data) {
                ToastUtil.showLongHideSoftInput(context, "登录失败");
            }
        });
    }

    public static void duGameUrl(final Context context, String url, final JYDCallback callback) {
        SortedMap paramObj = new TreeMap<>();
        paramObj.put("game_id", LoadConfig.JY_GAMEID + "");
        paramObj.put("channel_id", LoadConfig.JY_CHANNEL_ID + "");
        paramObj.put("cp_id", LoadConfig.JY_CP_ID + "");
        paramObj.put("ts", System.currentTimeMillis() + "");

        String sign = DuParamHelper.duDaiCreateSign("UTF-8", paramObj, DuParamHelper.GAME_SECRET);
        paramObj.put("sign", sign);

        HttpRequestUtil.okGetFormRequest(url, paramObj, new HttpRequestUtil.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JYDuGameUrlBean bean = GsonUtils.GsonToBean(result, JYDuGameUrlBean.class);
                if (bean.getStatus().equals("success")) {
                    callback.callback(JYDStatusCode.SUCCESS, bean);
                } else {
                    ToastUtil.showLongHideSoftInput(context, bean.getMessage());
                }
            }

            @Override
            public void requestFailure(String request, IOException e) {
                ToastUtil.showLongHideSoftInput(context, request);
            }

            @Override
            public void requestNoConnect(String msg, String data) {
                ToastUtil.showLongHideSoftInput(context, msg);
            }
        });
    }

    public static void duOrderMethod(final Context context, String url, final JYDPayParam payParam, final JYDCallback callback) {
        ProgressBarUtil.showProgressBar((Activity) context);
        SortedMap Param = DuParamHelper.duDaiMapParam(context);
        Param.put("user_id", payParam.getUserid());
        Param.put("product_id", payParam.getProductId());
        Param.put("product_name", payParam.getProductName());
        Param.put("money", payParam.getPrice());
        Param.put("cp_order_sn", payParam.getCpBill());
        Param.put("notify_cp_url", payParam.getPayNotifyUrl());
        Param.put("extension", payParam.getExtension());
        Param.put("is_test", 2);
        String sign = DuParamHelper.duDaiCreateSign("UTF-8", Param, DuParamHelper.GAME_SECRET);
        Param.put("sign", sign);

        HttpRequestUtil.okPostJsonRequest(url, Param, new HttpRequestUtil.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                ProgressBarUtil.hideProgressBar((Activity) context);
                JYDuOrderBean bean = GsonUtils.GsonToBean(result, JYDuOrderBean.class);
                if (bean.getStatus().equals("success")) {
                    callback.callback(JYDStatusCode.SUCCESS, bean);
                } else {
                    ToastUtil.showLongHideSoftInput(context, bean.getMessage());
                }
            }

            @Override
            public void requestFailure(String request, IOException e) {
                ProgressBarUtil.hideProgressBar((Activity) context);
                ToastUtil.showLongHideSoftInput(context, request);
            }

            @Override
            public void requestNoConnect(String msg, String data) {
                ProgressBarUtil.hideProgressBar((Activity) context);
                ToastUtil.showLongHideSoftInput(context, msg);
            }
        });
    }

    /**
     * 独代上报
     */
    public static void duLog(final Context context, String url, String type) {
        SortedMap Param = DuParamHelper.duDaiMapParam(context);
        Param.put("type", type);
        String sign = DuParamHelper.duDaiCreateSign("UTF-8", Param, DuParamHelper.GAME_SECRET);
        Param.put("sign", sign);

        HttpRequestUtil.okPostJsonRequest(url, Param, new HttpRequestUtil.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
            }

            @Override
            public void requestFailure(String request, IOException e) {
            }

            @Override
            public void requestNoConnect(String msg, String data) {
            }
        });
    }

    public static void testauth() {
        String json = "{\"agent\":{\"channel_id\":1,\"cp_id\":5},\"client_time_zone\":\"GMT+08:00\",\"client_ts\":1559790560111,\"cp_order_sn\":\"000100465jiyou0040\",\"device\":{\"android_adv_id\":\"unknown\",\"android_id\":\"cac7cf576d46e688\",\"android_imei\":\"865124036901694\",\"battery_level\":56,\"bluetooth\":\"00:00:00:00:00:00\",\"brand\":\"OnePlus\",\"cpu_type\":\"armeabi-v7a\",\"device_id\":\"4c0bbd3e-d758-3cec-987a-5dd78fc90399\",\"device_name\":\"OnePlus 5\",\"imsi\":\"460110407192185\",\"inner_ip\":\"192.168.31.46\",\"ios_idfa\":\"00000000-0000-0000-0000-000000000000\",\"is_root\":0,\"mac\":\"00000000-7dfb-9adf-ffff-ffffb2646a32\",\"model\":\"ONEPLUS A5000\",\"net_type\":\"wifi\",\"orientation_sensor\":\"-0.0,-0.0,0.0\",\"os_type\":\"android\",\"os_version\":\"9\",\"package_name\":\"com.jiyou.jyh5\",\"screen_brightness\":40,\"screen_height\":1920,\"screen_width\":1080,\"sdk_version\":\"28\",\"user_agent\":\"Mozilla\\/5.0 (Linux; Android 9; ONEPLUS A5000 Build\\/PKQ1.180716.001; wv) AppleWebKit\\/537.36 (KHTML, like Gecko) Version\\/4.0 Chrome\\/66.0.3359.158 Mobile Safari\\/537.36\",\"wifi_name\":\"<~!@#$%^&*()_+-={}[]|:<>,./?unknown ssid>\"},\"extension\":\"000100465jiyou0040WW4000100000049\",\"game\":{\"id\":17,\"name\":\"136\",\"version\":\"1\"},\"is_test\":2,\"money\":10,\"notify_cp_url\":\"https:\\/\\/snh5.server.game-as.com\\/snh5_center\\/game\\/api\\/jiyou\\/pay_new.php\",\"product_id\":\"60106\",\"product_name\":\"198\\u5143\\u5957\\u9910\",\"role\":{\"id\":\"4000100000049\",\"level\":10,\"name\":\"\\u519c\\u4f36\\u97f5\",\"server_id\":1,\"server_name\":\"S1\"},\"user_id\":\"624545\"}";
        SortedMap Param = null;
        Param = (SortedMap) GsonUtils.gsonToSortedMap(json);
//        Param = (SortedMap) GsonUtils.GsonToSortedMaps(json);

//        try {
//            Param = (SortedMap) GsonUtils.jsonToSortedMap(new JSONObject(json));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String sign = DuParamHelper.duDaiCreateSign("UTF-8", Param, DuParamHelper.GAME_SECRET);
        Param.put("sign", sign);
        HttpRequestUtil.okPostJsonRequest(DuUrlConstants.URL_DUDAI_ORDER, Param, new HttpRequestUtil.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
            }

            @Override
            public void requestFailure(String request, IOException e) {
            }

            @Override
            public void requestNoConnect(String msg, String data) {
            }
        });
    }



}
