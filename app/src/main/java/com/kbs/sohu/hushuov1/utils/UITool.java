package com.kbs.sohu.hushuov1.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by cs on 2017/03/6.
 */

public class UITool{

    public static void settingBgAlpha(float f, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = f;
        activity.getWindow().setAttributes(lp);
    }

}
