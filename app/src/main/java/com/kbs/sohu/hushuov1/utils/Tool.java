package com.kbs.sohu.hushuov1.utils;

import android.content.Context;

/**
 * Created by cs on 2017/03/6.
 */

public class Tool {

    private static Context context;

    private void init(Context context){
        Tool.context = context.getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
