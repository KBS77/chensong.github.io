package com.kbs.sohu.hushuov1.model.db;

import android.content.Context;

import com.kbs.sohu.hushuov1.model.dao.ContactTableDao;
import com.kbs.sohu.hushuov1.model.dao.InviteTableDao;

/**
 * Created by tarena on 2017/02/13.
 */

public class DBManager {

    private final DBHelper dbHelper;
    private ContactTableDao contactTableDao;
    private InviteTableDao inviteTableDao;

    public DBManager(Context context, String name) {
        // 创建数据库
        dbHelper = new DBHelper(context, name);
        contactTableDao = new ContactTableDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }
    //获取联系人列表数据
    public ContactTableDao getContactTableDao(){
        return contactTableDao;
    }
    //获取邀请人信息数据
    public InviteTableDao getInviteTableDao(){
        return inviteTableDao;
    }

    // 关闭数据库的方法
    public void close() {
        dbHelper.close();
    }
}
