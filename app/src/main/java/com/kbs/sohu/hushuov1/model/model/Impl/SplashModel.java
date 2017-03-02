package com.kbs.sohu.hushuov1.model.model.Impl;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kbs.sohu.hushuov1.XingApplication;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.model.IModel.ISplashModel;
import com.kbs.sohu.hushuov1.utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by cs on 2017/02/24.
 */

public class SplashModel implements ISplashModel {

    public SplashModel(){

    }

    @Override
    public void getAdPic(final CallBack callBack){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(XingApplication.getGlobalApplication());
        String adPic = prefs.getString("ad_pic",null);
        if(adPic == null){
            final String requestAdPic = "http://guolin.tech/api/bing_pic";
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    HttpUtil.sendOkhttpRequest(requestAdPic, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String adPic = response.body().string();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(XingApplication.getGlobalApplication()).edit();
                            editor.putString("ad_pic",adPic);
                            editor.apply();
                            callBack.onSuccess(adPic);
                        }
                    });
                }
            });
        }else{
            callBack.onSuccess(adPic);
        }
    }
}
