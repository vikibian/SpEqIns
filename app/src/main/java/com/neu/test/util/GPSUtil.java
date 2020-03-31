package com.neu.test.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * created by Viki on 2020/3/27
 * system login name : lg
 * created time : 10:09
 * email : 710256138@qq.com
 */
public class GPSUtil {
    private static final String TAG = "GPSUtil";
    private Context context;
    private LocationManager mLocationManager;
    private String local = "";

    public GPSUtil(Context context){
        this.context = context;

    }

    @SuppressLint("MissingPermission")
    public void startLocate() {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) { //GPS已开启
            /**
             * 绑定监听
             * 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
             * 参数2，位置信息更新周期.单位是毫秒
             * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
             * 参数4，监听
             * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
             */
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(context, "请打开GPS定位", Toast.LENGTH_SHORT).show();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //位置信息变化时触发

            DecimalFormat df = new DecimalFormat("0.00");
            StringBuffer sb = new StringBuffer(256);
            String El = df.format(location.getLongitude());
            String Nl = df.format(location.getLatitude());
            sb.append("E:"+El);
            sb.append(" N:"+Nl);
            local = sb.toString();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //GPS状态变化时触发
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.e("onStatusChanged", "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.e("onStatusChanged", "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.e("onStatusChanged", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            //GPS开启时触发
            Log.e("xyh", "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //GPS禁用时触发
            Log.e("xyh", "onProviderDisabled: ");
        }
    };

    public String getLocal(){
        return local;
    }

    public void shutLocate(){
        mLocationManager.removeUpdates(locationListener);
    }

}
