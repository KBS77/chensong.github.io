package com.kbs.sohu.hushuov1.utils;

import android.widget.Toast;

/**
 * Created by cs on 2017/03/7.
 */

public class ToastUtil extends Tool{

    private static Toast toast;

    public static void show(String text){
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}
