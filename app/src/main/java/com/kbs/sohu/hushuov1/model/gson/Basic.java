package com.kbs.sohu.hushuov1.model.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tarena on 2016/12/19.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String WeatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
