package com.jiyou.jydudailib.http;

import android.app.Activity;
import android.content.Context;

import com.jiyou.jydudailib.api.callback.JYDCallback;
import com.jiyou.jydudailib.api.constants.JYDStatusCode;
import com.jiyou.jydudailib.api.model.JYDPayParam;
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
        String sign = DuParamHelper.duDaiCreateSign("UTF-8", paramObj, DuParamHelper.CP_SECRET);
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
}
