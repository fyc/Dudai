package com.jiyou.jydudailib.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jiyou.jydudailib.thirdtools.gson.Gson;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Call;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Callback;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.FormBody;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Headers;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.MediaType;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.OkHttpClient;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Request;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.RequestBody;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Response;
import com.jiyou.jydudailib.config.ExtApp;
import com.jiyou.jydudailib.tools.CheckNetStatueUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 网络工具类
 */

public class HttpRequestUtil {

    private volatile static HttpRequestUtil netRequest;
    private static OkHttpClient okHttpClient;
    private Handler mHandler;
    final String TAG = "okhttp";
    private boolean checkNet;

    private HttpRequestUtil() {

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                /*    .addInterceptor(new AddCookiesInterceptor())
                      .addInterceptor(new SaveCookiesInterceptor())*/
                .build();

        mHandler = new Handler(Looper.getMainLooper());

    }

    private static HttpRequestUtil getInstance() {
        if (netRequest == null) {
            netRequest = new HttpRequestUtil();
        }
        return netRequest;
    }

    /**
     * 异步get请求（Form），内部实现方法
     *
     * @param url    url
     * @param params key value
     */
    public void inner_GetFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {

        if (params == null) {
            params = new HashMap<>();
        }
        final String doUrl = urlJoint(url, params);
        final Request request = new Request.Builder().url(doUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                deliverDataFailure(HttpConstants.NET_ON_FAILURE, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /**
     * get请求  没有请求体
     *
     * @param url
     * @param callBack
     */
    private void getMethod(String url, final DataCallBack callBack) {
        final Request req = new Request.Builder().url(url).build();
        okHttpClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(HttpConstants.NET_ON_FAILURE, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response != null && response.isSuccessful()) {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    deliverDataSuccess("请求异常", callBack);

                }
            }
        });
    }

    /**
     * 异步post请求,内部实现方法
     *
     * @param url      url
     * @param params   params
     * @param callBack callBack
     */

    private void inner_PostFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }

        requestBody = builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(HttpConstants.SERVER_ERROR, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    Headers newHead = response.networkResponse().request().headers();
                    Log.i(TAG, "new headers :: " + newHead);
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    private void inner_PostJsonAsync(String url, Map<String, String> params, final DataCallBack callBack) {

        String mapToJson = new Gson().toJson(params);

        final Request request = buildJsonPostRequest(url, mapToJson);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(HttpConstants.SERVER_ERROR, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    private Request buildJsonPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 失败结果回调
     *
     * @param request  request
     * @param e        e
     * @param callBack callBack
     */
    private void deliverDataFailure(final String request, final IOException e, final DataCallBack callBack) {
        /**
         * 在这里使用异步处理
         */
        checkNet = CheckNetStatueUtil.check_NET(ExtApp.app());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        if (checkNet) {
                            callBack.requestFailure(request, e);
                        } else {
                            callBack.requestNoConnect(HttpConstants.NET_NO_LINKING, HttpConstants.NET_NO_LINKING);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    /**
     * 成功结果回调
     *
     * @param result   result
     * @param callBack callBack
     */
    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        checkNet = CheckNetStatueUtil.check_NET(ExtApp.app());

                        if (checkNet) {
                            callBack.requestSuccess(result);
                        } else {
                            callBack.requestNoConnect(HttpConstants.NET_NO_LINKING, HttpConstants.NET_NO_LINKING);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 数据回调接口
     */
    public interface DataCallBack {
        //请求成功 响应成功
        void requestSuccess(String result) throws Exception;

        //服务器宕机
        void requestFailure(String request, IOException e);

        //客户端没有网络链接
        void requestNoConnect(String msg, String data);

    }

    /**
     * 拼接url和请求参数
     *
     * @param url    url
     * @param params key value
     * @return String url
     */
    private static String urlJoint(String url, Map<String, String> params) {

        StringBuilder endUrl = new StringBuilder(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();

        for (Map.Entry<String, String> entry : entrySet) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                endUrl.append("?");
            } else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();

    }

    //-------------对外提供的方法Start--------------------------------

    /**
     * 建立网络框架，获取网络数据，异步get请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack data
     */
    public static void okGetFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_GetFormAsync(url, params, callBack);
    }

    /**
     * 建立网络框架，获取网络数据，异步post请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack data
     */
    public static void okPostFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_PostFormAsync(url, params, callBack);
    }

    /**
     * get 请求
     * 没有请求体
     */
    public static void okGetRequest(String url, DataCallBack callBack) {
        getInstance().getMethod(url, callBack);
    }

    public static void okPostJsonRequest(String url, Map<String, String> params, final DataCallBack callBack) {
        getInstance().inner_PostJsonAsync(url,params,callBack);
    }
}
