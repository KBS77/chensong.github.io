package com.kbs.sohu.hushuov1.model.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tarena on 2016/12/19.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
