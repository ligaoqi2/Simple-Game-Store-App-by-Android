package com.ligaoqi.finalpractice.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    private static final Gson gson = new Gson();

    private static final Gson gsonBuilder = new GsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }

    public static Gson getGsonBuilder(){
        return gsonBuilder;
    }
}
