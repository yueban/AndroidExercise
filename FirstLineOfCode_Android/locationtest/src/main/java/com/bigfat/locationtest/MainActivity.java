package com.bigfat.locationtest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";
    public static final int SHOW_LOCATION = 0;

    private TextView positionTextView;

    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        positionTextView = (TextView) findViewById(R.id.position_text_view);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {//没有可用的位置提供器时，提醒用户
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_LONG).show();
        }
        //显示最后一次的位置
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, Boolean.toString(location == null));
        if (location != null) {
            showLocation(location);
        }
        //启动位置监听
        locationManager.requestLocationUpdates(provider, 5000, 0, locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //组装反向地理编码的接口地址
                    StringBuilder url = new StringBuilder();
                    url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    url.append(location.getLatitude());
                    url.append(",");
                    url.append(location.getLongitude());
                    url.append("&sensor=false");
                    //发送请求
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url.toString());
                    //在请求消息头中指定语言，保证服务器会返回中文数据
                    httpGet.addHeader("Accept-Language", "zh-CN");
                    HttpResponse httpResponse = client.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity);
//                        Log.d(TAG, "response--->" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        //获取resultArray节点下的位置信息
                        JSONArray resultArray = jsonObject.getJSONArray("results");
//                        Log.d(TAG, "resultArray.length()--->" + resultArray.length());
                        if (resultArray.length() > 0) {
                            JSONObject subObject = resultArray.getJSONObject(0);
                            //取出格式化后的位置信息
                            String formatted_address = subObject.getString("formatted_address");
//                            Log.d(TAG, "formatted_address--->" + formatted_address);
                            Message message = new Message();
                            message.what = SHOW_LOCATION;
                            message.obj = formatted_address;
                            handler.handleMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOCATION:
                    String currentPosition = (String) msg.obj;
                    positionTextView.setText(currentPosition);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
