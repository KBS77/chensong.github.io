package com.kbs.sohu.hushuov1.model.gson;

/**
 * Created by tarena on 2016/12/19.
 */

public class AQI {

    public AQICity city;

    public class AQICity{

        public String aqi;

        public String pm25;
    }
}
