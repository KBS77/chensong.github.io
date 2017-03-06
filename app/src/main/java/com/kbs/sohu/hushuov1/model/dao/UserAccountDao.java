package com.kbs.sohu.hushuov1.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kbs.sohu.hushuov1.model.bean.UserInfo;
import com.kbs.sohu.hushuov1.model.db.UserAccountDB;

/**
 * Created by tarena on 2017/02/10.
 */

public class UserAccountDao {

    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB(context);
    }

    // 添加用户到数据库
    public void addAccount(UserInfo user) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getWritableDatabase();


        // 执行添加操作
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_HXID, user.getHxid());
        values.put(UserAccountTable.COL_NAME, user.getName());
        values.put(UserAccountTable.COL_NICK, user.getNick());

        db.replace(UserAccountTable.TAB_NAME, null, values);
    }

    public void updateAccount(String hxid,String nick){

        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        if(nick != null){
            values.put(UserAccountTable.COL_NICK, nick);
        }

        db.update(UserAccountTable.TAB_NAME,values,"hxid = ?",new String[]{hxid});
    }

    // 根据环信id获取所有用户信息
    public UserInfo getAccountByHxId(String hxId) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // 执行查询语句
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + UserAccountTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();

            // 封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
        }

        // 关闭资源
        cursor.close();

        // 返回数据
        return  userInfo;
    }
}
