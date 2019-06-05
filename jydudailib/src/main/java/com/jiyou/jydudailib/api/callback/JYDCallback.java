package com.jiyou.jydudailib.api.callback;

/**
 * Created by tzw on 2018/6/5.
 * 全局回调
 */

public interface JYDCallback<T> {
    void callback(int code, T response);
}
