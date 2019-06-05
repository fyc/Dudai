package com.jiyou.general.callback;

/**
 * Created by tzw on 2018/6/5.
 * 全局回调
 */

public interface JYGCallback<T> {
    void callback(int code, T response);
}
