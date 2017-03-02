package com.kbs.sohu.hushuov1.model.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tarena on 2016/12/19.
 */

public class Weather {

    public String status;

    public Basic basic;

    public Now now;

    public AQI aqi;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
