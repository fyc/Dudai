/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jiyou.jydudailib.thirdtools.http.okhttp3.internal;

import com.jiyou.jydudailib.thirdtools.http.okhttp3.Address;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Call;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.ConnectionPool;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.ConnectionSpec;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Headers;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.HttpUrl;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.OkHttpClient;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Request;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Response;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.Route;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.internal.cache.InternalCache;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.internal.connection.RealConnection;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.internal.connection.RouteDatabase;
import com.jiyou.jydudailib.thirdtools.http.okhttp3.internal.connection.StreamAllocation;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

/**
 * Escalate internal APIs in {@code okhttp3} so they can be used from OkHttp's implementation
 * packages. The only implementation of this interface is in {@link OkHttpClient}.
 */
public abstract class Internal {

  public static void initializeInstanceForTests() {
    // Needed in tests to ensure that the instance is actually pointing to something.
    new OkHttpClient();
  }

  public static Internal instance;

  public abstract void addLenient(Headers.Builder builder, String line);

  public abstract void addLenient(Headers.Builder builder, String name, String value);

  public abstract void setCache(OkHttpClient.Builder builder, InternalCache internalCache);

  public abstract RealConnection get(ConnectionPool pool, Address address,
                                     StreamAllocation streamAllocation, Route route);

  public abstract boolean equalsNonHost(Address a, Address b);

  public abstract Socket deduplicate(
          ConnectionPool pool, Address address, StreamAllocation streamAllocation);

  public abstract void put(ConnectionPool pool, RealConnection connection);

  public abstract boolean connectionBecameIdle(ConnectionPool pool, RealConnection connection);

  public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);

  public abstract int code(Response.Builder responseBuilder);

  public abstract void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket,
                             boolean isFallback);

  public abstract HttpUrl getHttpUrlChecked(String url)
      throws MalformedURLException, UnknownHostException;

  public abstract StreamAllocation streamAllocation(Call call);

  public abstract Call newWebSocketCall(OkHttpClient client, Request request);
}
