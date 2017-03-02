package com.kbs.sohu.hushuov1.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.utils.handler.HandlerUtil;
import com.kbs.sohu.hushuov1.ui.widget.CircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LBSActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.mv_location_map) MapView mv_location_map;
    @BindView(R.id.cv_bt_start) CircleView cv_bt_start;
    @BindView(R.id.tv_road_data) TextView tv_road_data;
    @BindView(R.id.tv_sport_time) TextView tv_sport_time;
    private LocationClient mLocationClient;
    private BaiduMap baiduMap = mv_location_map.getMap();;
    private Boolean isFirstLocate = true;
    private Runnable mTicker;
    private List<LatLng> points = new ArrayList<LatLng>();
    private OverlayOptions overlayOptionsooPolyline;
    private long startTime;
    private double sum_distance = 0;
    private LatLng p1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_lbs);
        ButterKnife.bind(this);
        cv_bt_start.setOnClickListener(this);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissons = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LBSActivity.this,permissons,1);
        }else{
            requestLocation();
        }
        baiduMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        String bt_text = cv_bt_start.getText().toString();
        if("开始".equalsIgnoreCase(bt_text)){
            cv_bt_start.setText("停止");
            startTime();
            startDistance();
        }else if("停止".equalsIgnoreCase(bt_text)){
            cv_bt_start.setText("开始");
            stopTime();
            stopDistance();
        }
    }

    private void startDistance() {

    }

    private void stopDistance() {

    }

    private void startTime() {
        startTime = System.currentTimeMillis();
        mTicker = new Runnable() {
            public void run() {
                final String content = showTimeCount(System.currentTimeMillis() - startTime);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_sport_time.setText(content);
                    }
                });
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                HandlerUtil.getInstance(LBSActivity.this).postAtTime(mTicker, next);
            }
        };
        mTicker.run();

    }

    private void stopTime() {
        HandlerUtil.getInstance(LBSActivity.this).removeCallbacks(mTicker);
    }

    private String showTimeCount(long time) {
                 if(time >= 360000000){
                         return "00:00:00";
                 }
                 String timeCount = "";
                 long hourc = time/3600000;
                 String hour = "0" + hourc;
                 hour = hour.substring(hour.length()-2, hour.length());
                 long minuec = (time-hourc*3600000)/(60000);
                 String minue = "0" + minuec;
                 minue = minue.substring(minue.length()-2, minue.length());
                 long secc = (time-hourc*3600000-minuec*60000)/1000;
                 String sec = "0" + secc;
                 sec = sec.substring(sec.length()-2, sec.length());
                 timeCount = hour + ":" + minue + ":" + sec;
                 return timeCount;
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setScanSpan(2000);
        mLocationClient.setLocOption(option);
    }

    private void navigateTo(BDLocation location){
        if(isFirstLocate){
            p1 = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(p1);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }else{
            LatLng p2 = p1;
            p1 = new LatLng(location.getLatitude(),location.getLongitude());
            points.add(p1);
            sum_distance = sum_distance + DistanceUtil.getDistance(p2,p1);
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(p1.latitude);
        locationBuilder.longitude(p1.longitude);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
        overlayOptionsooPolyline = new PolylineOptions().width(15).color(0x5584cceb).points(points);
        double road_data = sum_distance/100*100;
        tv_road_data.setText(road_data + "m");
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(LBSActivity.this,"需要同意所有权限方可使用",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(LBSActivity.this,"程序员狗带",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv_location_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_location_map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mv_location_map.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

//    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
//        double radLat1 = (lat_a * Math.PI / 180.0);
//        double radLat2 = (lat_b * Math.PI / 180.0);
//        double a = radLat1 - radLat2;
//        double b = (lng_a - lng_b) * Math.PI / 180.0;
//        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//                + Math.cos(radLat1) * Math.cos(radLat2)
//                * Math.pow(Math.sin(b / 2), 2)));
//        s = s * EARTH_RADIUS;
//        //s = Math.round(s * 10000) / 10000;
//        return 1.38*s;
//
//    }
}
