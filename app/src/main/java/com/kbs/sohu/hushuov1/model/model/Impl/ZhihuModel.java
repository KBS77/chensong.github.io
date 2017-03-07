package com.kbs.sohu.hushuov1.model.model.Impl;

import android.content.Context;

import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.model.IModel.IZhihuModel;
import com.kbs.sohu.hushuov1.utils.HttpUtil;
import com.kbs.sohu.hushuov1.utils.ToastUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by cs on 2017/03/7.
 */

public class ZhihuModel implements IZhihuModel {

    private Context context;

    public ZhihuModel(Context context){
        this.context = context;
    }

    @Override
    public void load(final String url, final CallBack callback) {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkhttpRequest(url, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String c = response.body().string();
                        callback.onSuccess(c);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.show("网络故障");
                    }
                });
            }
        });
    }
}
