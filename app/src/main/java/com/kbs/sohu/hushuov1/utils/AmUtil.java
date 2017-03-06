package com.kbs.sohu.hushuov1.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by cs on 2017/03/6.
 */

public class AmUtil {

    public static void onInactive(Context context, EditText et) {

        if (et == null)
            return;

        et.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
