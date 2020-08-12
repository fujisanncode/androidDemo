package com.fujisann.ink.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    // 异步http请求，回调中处理数据
    public static void send(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }
}
