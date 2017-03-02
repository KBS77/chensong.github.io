package com.kbs.sohu.hushuov1.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.gson.Forecast;
import com.kbs.sohu.hushuov1.model.gson.Weather;
import com.kbs.sohu.hushuov1.utils.HttpUtil;
import com.kbs.sohu.hushuov1.utils.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.iv_search_city) ImageView iv_search_city;
    @BindView(R.id.iv_weather_bg) ImageView iv_weather_bg;
    @BindView(R.id.tv_city_name) TextView tv_city_name;
    @BindView(R.id.tv_update_time) TextView tv_update_time;
    @BindView(R.id.tv_comfort_text) TextView tv_comfort_text;
    @BindView(R.id.tv_carwash_text) TextView tv_carwash_text;
    @BindView(R.id.tv_sport_text) TextView tv_sport_text;
    @BindView(R.id.tv_degree_text) TextView tv_degree_text;
    @BindView(R.id.tv_weather_info_text) TextView tv_weather_info_text;
    @BindView(R.id.tv_info_text) TextView tv_info_text;
    @BindView(R.id.tv_date_text) TextView tv_date_text;
    @BindView(R.id.tv_min_text) TextView tv_min_text;
    @BindView(R.id.tv_max_text) TextView tv_max_text;
    @BindView(R.id.tv_aqi_text) TextView tv_aqi_text;
    @BindView(R.id.tv_pm25_text) TextView tv_pm25_text;
    @BindView(R.id.sc_weather_layout) ScrollView sc_weather_layout;
    @BindView(R.id.ll_forecast_layout) LinearLayout ll_forecast_layout;
    public @BindView(R.id.srl_refresh_weather) SwipeRefreshLayout srl_refresh_weather;
    public @BindView(R.id.dl_weather_layout) DrawerLayout dl_weather_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        srl_refresh_weather.setColorSchemeResources(R.color.colorTheme);
        initData();
    }

    private void initData() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        final String weatherId;
        if(weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.WeatherId;
            showWeatherInfo(weather);
        }else{
            weatherId = getIntent().getStringExtra("weather_id");
            sc_weather_layout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        srl_refresh_weather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        String bingPic = prefs.getString("ad_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(iv_weather_bg);
        }else{
            requestBingPic();
        }
        iv_search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_weather_layout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void requestBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("ad_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(iv_weather_bg);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=c2b63832b4b2412ba5af5c871207858d";
        HttpUtil.sendOkhttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                        srl_refresh_weather.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                        }
                        srl_refresh_weather.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        tv_city_name.setText(cityName);
        tv_update_time.setText(updateTime);
        tv_degree_text.setText(degree);
        tv_weather_info_text.setText(weatherInfo);
        ll_forecast_layout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,ll_forecast_layout,false);
            tv_info_text = (TextView) view.findViewById(R.id.tv_info_text);
            tv_date_text = (TextView) view.findViewById(R.id.tv_date_text);
            tv_min_text = (TextView) view.findViewById(R.id.tv_min_text);
            tv_max_text = (TextView) view.findViewById(R.id.tv_max_text);
            tv_date_text.setText(forecast.date);
            tv_info_text.setText(forecast.more.info);
            tv_max_text.setText(forecast.temperature.max);
            tv_min_text.setText(forecast.temperature.min);
            ll_forecast_layout.addView(view);
        }
        if(weather.aqi != null){
            tv_aqi_text.setText(weather.aqi.city.aqi);
            tv_pm25_text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carcash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动指数："+weather.suggestion.sport.info;
        tv_comfort_text.setText(comfort);
        tv_carwash_text.setText(carcash);
        tv_sport_text.setText(sport);
        sc_weather_layout.setVisibility(View.VISIBLE);
    }
}
