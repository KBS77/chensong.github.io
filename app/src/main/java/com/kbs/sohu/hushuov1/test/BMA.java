package com.kbs.sohu.hushuov1.test;

/**
 * Created by tarena on 2017/02/28.
 */

public class BMA {

    private static final String FORMATE = "json";
    private static final String BASE = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=" + FORMATE;

    public static String focusPic(int num){
        StringBuffer sb = new StringBuffer(BASE);
        sb.append("&method=").append("baidu.ting.plaza.getFocusPic")
                .append("&num=").append(num);
        return sb.toString();
    }
}
