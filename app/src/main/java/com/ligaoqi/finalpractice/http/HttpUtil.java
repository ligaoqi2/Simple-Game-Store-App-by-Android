package com.ligaoqi.finalpractice.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ligaoqi.finalpractice.entity.BaseBean;
import com.ligaoqi.finalpractice.entity.NetCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {

    private static final String TAG = "httpUtil";

    private final static String PRE_HTTP_URL = "your url";

    private Handler handler = new Handler(Looper.getMainLooper());

    private Gson gson = new Gson();

    private volatile static HttpUtil instance;

    private HttpUtil() {
    }

    // 单例
    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    // get请求
    public <T> void get(String path, HashMap<String, String> paramsHashMap, NetCallBack<BaseBean<T>> netCallBack) {
        Request request = new Request.Builder()
                .get()
                .url(PRE_HTTP_URL)
                .build();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addPathSegments(path);

        Set<Map.Entry<String, String>> entries = paramsHashMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        Request newRequest = request.newBuilder()
                .url(httpUrlBuilder.build())
                .build();

        HttpManager.getInstance().okHttpClient.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "网络请求失败");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(500, Log.getStackTraceString(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, "网络请求成功，code " + response.code());
                int code = response.code();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String resStr = body.string();
                            BaseBean<T> baseBean = gson.fromJson(resStr, new TypeToken<BaseBean<T>>() {
                            }.getType());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (baseBean.code == 200) {
                                        netCallBack.onSuccess(baseBean);
                                    } else {
                                        netCallBack.onFailure(baseBean.code, "服务端请求失败");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    netCallBack.onFailure(200, Log.getStackTraceString(e));
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onFailure(code, "网络请求失败");
                        }
                    });
                }
            }
        });
    }

    // post请求
    public <T> void post(String path, RequestBody requestBody, NetCallBack<BaseBean<T>> netCallBack) {
        Request request = new Request.Builder()
                .post(requestBody)
                .url(PRE_HTTP_URL)
                .build();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addPathSegments(path);

        Request newRequest = request.newBuilder()
                .url(httpUrlBuilder.build())
                .build();
        HttpManager.getInstance().okHttpClient.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "网络请求失败");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(500, Log.getStackTraceString(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, "网络请求成功，code " + response.code());
                int code = response.code();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String resStr = body.string();
                            BaseBean<T> baseBean = gson.fromJson(resStr, new TypeToken<BaseBean<T>>() {
                            }.getType());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (baseBean.code == 200) {
                                        netCallBack.onSuccess(baseBean);
                                    } else {
                                        netCallBack.onFailure(baseBean.code, "服务端请求失败");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    netCallBack.onFailure(200, Log.getStackTraceString(e));
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onFailure(code, "网络请求失败");
                        }
                    });
                }
            }
        });
    }

    // 获取用户信息
    public <T> void getUserInformation(String path, String header, NetCallBack<BaseBean<T>> netCallBack) {
        Request request = new Request.Builder()
                .get()
                .url(PRE_HTTP_URL)
                .build();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addPathSegments(path);

        Request newRequest = request.newBuilder()
                .url(httpUrlBuilder.build())
                .header("Authorization", "Bearer " + header)
                .build();

        HttpManager.getInstance().okHttpClient.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "网络请求失败");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(500, Log.getStackTraceString(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, "网络请求成功，code " + response.code());
                int code = response.code();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String resStr = body.string();
                            BaseBean<T> baseBean = gson.fromJson(resStr, new TypeToken<BaseBean<T>>() {
                            }.getType());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (baseBean.code == 200) {
                                        netCallBack.onSuccess(baseBean);
                                    } else {
                                        netCallBack.onFailure(baseBean.code, "服务端请求失败");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    netCallBack.onFailure(200, Log.getStackTraceString(e));
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onFailure(code, "网络请求失败");
                        }
                    });
                }
            }
        });
    }

    // 获取游戏信息
    public <T> void getGameInformation(String path, NetCallBack<BaseBean<T>> netCallBack) {
        Request request = new Request.Builder()
                .get()
                .url(PRE_HTTP_URL)
                .build();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addPathSegments(path);

        Request newRequest = request.newBuilder()
                .url(httpUrlBuilder.build())
                .build();

        HttpManager.getInstance().okHttpClient.newCall(newRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "网络请求失败");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(500, Log.getStackTraceString(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, "网络请求成功，code " + response.code());
                int code = response.code();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String resStr = body.string();
                            BaseBean<T> baseBean = gson.fromJson(resStr, new TypeToken<BaseBean<T>>() {
                            }.getType());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (baseBean.code == 200) {
                                        netCallBack.onSuccess(baseBean);
                                    } else {
                                        netCallBack.onFailure(baseBean.code, "服务端请求失败");
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    netCallBack.onFailure(200, Log.getStackTraceString(e));
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onFailure(code, "网络请求失败");
                        }
                    });
                }
            }
        });
    }
}

