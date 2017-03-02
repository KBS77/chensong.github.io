package com.kbs.sohu.hushuov1.model;

import android.content.Context;

import com.kbs.sohu.hushuov1.model.bean.UserInfo;
import com.kbs.sohu.hushuov1.model.dao.UserAccountDao;
import com.kbs.sohu.hushuov1.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tarena on 2017/02/10.
 */

public class Model {

    private Context mContext;
    //创建对象
    private static Model model = new Model();
    private ExecutorService executors = Executors.newCachedThreadPool();
    private UserAccountDao mUserAccountDao;
    private DBManager dbManager;

    private Model(){

    }

    //获取单例对象
    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        mContext = context;
        mUserAccountDao = new UserAccountDao(context);
        EventListener eventListener = new EventListener(context);
    }

    //获取全局线程池
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    //登录成功后的流程
    public void loginSuccess(UserInfo account){

        if(account == null){
            return;
        }

        if(dbManager != null){
            dbManager.close();
        }

        dbManager = new DBManager(mContext,account.getName());
    }


    //获取数据库管理类
    public DBManager getDbManager(){
        return dbManager;
    }

    //获取用户数据库的操作类对象
    public UserAccountDao getUserAccountDao(){
        return mUserAccountDao;
    }
}
