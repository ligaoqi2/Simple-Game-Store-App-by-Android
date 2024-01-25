package com.ligaoqi.finalpractice.http;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HttpInterceptor implements Interceptor {

    private final String TAG = "LoggerInterceptor";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(TAG, "request信息:" + request.toString());

        String method = request.method();
        if (TextUtils.equals(method, "POST")) {
            RequestBody body = request.body();
            Log.i(TAG, "body信息：" + bodyToString(body));
        }

        Response response = chain.proceed(request);
        int code = response.code();
        ResponseBody body = response.body();

        Log.i(TAG, "response信息：code=" + code);
        if (body != null) {
            //todo body只能读取一次
            String bodyStr = body.string();
            Log.i(TAG, "response信息：body：" + bodyStr);
            MediaType mediaType = body.contentType();
            return response.newBuilder().body(ResponseBody.create(mediaType, bodyStr)).build();
        }
        return response;
    }

    private String bodyToString(final RequestBody requestBody) {
        try {
            final Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
