package com.kbs.sohu.hushuov1.utils;

import android.util.Log;

import com.kbs.sohu.hushuov1.model.bean.UserInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by cs on 2017/03/1.
 */

public class BmobUtil {

    private static final String TAG = "BmobUtil";
    private static BmobUtil sInstance = new BmobUtil();
    private static UserInfo user;

    private BmobUtil(){

    }

    public static BmobUtil getInstance(){
        return sInstance;
    }

    public void addUser(String registerPhone,String password,String nick){
        UserInfo user = new UserInfo();
        user.setUsername(registerPhone);
        user.setPassword(password);
        user.setNick(nick);
        user.signUp(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.d(TAG,"成功");
                }else{
                    Log.i(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void deleteUser(){

    }

    private void updateUser(){

    }

    public UserInfo getUser(String username){
        final BmobQuery<UserInfo> bmobQuery	 = new BmobQuery<UserInfo>();
        bmobQuery.addWhereEqualTo("username",username);
        //先判断是否有缓存
        boolean isCache = bmobQuery.hasCachedResult(UserInfo.class);
        if(isCache){
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
        }else{
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
        }
        bmobQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> users, BmobException e) {
                if(e == null){
                    Log.d(TAG,"login success");
                    user = users.get(0);
                }
            }
        });
        return user;
    }
}
