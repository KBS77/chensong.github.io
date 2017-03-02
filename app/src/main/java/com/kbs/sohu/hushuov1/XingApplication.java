package com.kbs.sohu.hushuov1;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.kbs.sohu.hushuov1.utils.handler.CrashHandler;
import com.kbs.sohu.hushuov1.model.Model;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Created by tarena on 2017/02/13.
 */

public class XingApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);// 设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);// 设置需要同意后才能接受群邀请
        EaseUI.getInstance().init(this,options);

        //初始化百度地图
        SDKInitializer.initialize(this);
        LitePalApplication.initialize(this);

        //初始化Bmob云存储
        Bmob.initialize(this,"e9206b4f2e812ac231e5e998d2b687b5","Bmob");

        //初始化数据集成类Model
        Model.getInstance().init(this);

        //初始化异常捕捉器
        initCrashException();

        //初始化上下文对象
        mContext = this;
    }

    public static Context getGlobalApplication(){
        return mContext;
    }

    public void initCrashException(){
        CrashHandler.getsInstance().init(this);
    }
}
