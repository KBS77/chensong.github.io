package com.kbs.sohu.hushuov1.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cs on 2017/03/6.
 */

public class BaseActivity extends AppCompatActivity {

    public static <T extends BaseActivity> void start(Context from, Class clacc){
        Intent intent = new Intent(from,clacc);
        if(!(from instanceof BaseActivity)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        from.startActivity(intent);
    }
}
