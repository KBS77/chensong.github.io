package com.kbs.sohu.hushuov1.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kbs.sohu.hushuov1.model.dao.ContactTable;
import com.kbs.sohu.hushuov1.model.dao.InviteTable;

/**
 * Created by tarena on 2017/02/13.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建联系人的表
        db.execSQL(ContactTable.CREATE_TAB);

        // 创建邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
