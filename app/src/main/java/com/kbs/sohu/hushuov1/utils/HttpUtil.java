package com.kbs.sohu.hushuov1.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tarena on 2017/02/7.
 */

public class HttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    public static void sendOkhttpRequest(String address,okhttp3.Callback callback){
        Request request =new Request.Builder().url(address).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public static JsonObject getResposeJsonObject(String action1, Context context, boolean forceCache) {
        try {
            File sdcache = context.getCacheDir();
            Cache cache = new Cache(sdcache.getAbsoluteFile(), 1024 * 1024 * 30); //30Mb
            Request.Builder builder = new Request.Builder()
                    .url(action1);
            if (forceCache) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            }
            Request request = builder.build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String c = response.body().string();
                Log.e("cache", c);
                JsonParser parser = new JsonParser();
                JsonElement el = parser.parse(c);
                return el.getAsJsonObject();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
