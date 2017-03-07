package com.kbs.sohu.hushuov1.utils;

import android.util.Log;

import com.kbs.sohu.hushuov1.model.bean.UserInfo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by cs on 2017/03/1.
 */

public class BmobUtil {

    private static final String TAG = "BmobUtil";

    public static void CreateUser(String registerPhone,String password,String nick){
        UserInfo user = new UserInfo();
        user.setUsername(registerPhone);
        user.setPassword(password);
        user.setPassword2(password);
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

    public static void reCode(String oldPwd,String newPwd){

        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    ToastUtil.show("修改成功");
                }else{
                    ToastUtil.show("修改失败");
                }
            }
        });
    }

    public static UserInfo getUser(){
        UserInfo userInfo = BmobUser.getCurrentUser(UserInfo.class);
        return userInfo;
    }
}
