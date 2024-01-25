package com.ligaoqi.finalpractice.http;

import okhttp3.OkHttpClient;

public class HttpManager {

    public final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new HttpInterceptor()).build();

    private static volatile HttpManager instance;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }
}
