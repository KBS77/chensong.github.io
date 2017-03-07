package com.kbs.sohu.hushuov1.utils;

/**
 * Created by cs on 2017/03/7.
 */

public class BMA {

    public static final String BASE = APi.ZHIHU_NEWS ;

    public static String focusPic(int num) {
        StringBuffer sb = new StringBuffer(BASE);
        sb.append("&num=").append(num);
        return sb.toString();
    }
}
