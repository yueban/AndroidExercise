package com.bigfat.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bigfat.coolweather.R;
import com.bigfat.coolweather.db.CoolWeatherDB;
import com.bigfat.coolweather.model.City;
import com.bigfat.coolweather.model.Country;
import com.bigfat.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class ChooseAreaActivity extends Activity {

    public static final String TAG = "ChooseAreaActivity";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;

    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<Country> countryList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    /**
     * 是否从WeatherActivity中跳转过来
     */
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        //如果已经选择了地区并且不是从WeatherActivity跳转过来的，则直接显示该地区的天气
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("city_selected", false) && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }


//        String url = WeatherApiUtil.getWeatherUrl("101010100", "forecast_v");
//        Log.d(TAG, "url--->" + url);
//        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                Log.d(TAG, "response--->" + response);
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

        coolWeatherDB = CoolWeatherDB.getInstance(this);//初始化数据库操作实例

        setContentView(R.layout.choose_area);
        //绑定控件
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);

        //初始化控件
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentLevel) {
                    case LEVEL_PROVINCE:
                        selectedProvince = provinceList.get(position);
                        queryCities();
                        break;

                    case LEVEL_CITY:
                        selectedCity = cityList.get(position);
                        queryCountries();
                        break;

                    case LEVEL_COUNTRY:
                        Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                        intent.putExtra("area_id", countryList.get(position).getAreaId());
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });

        queryProvinces();
    }

    /**
     * 显示省级列表
     */
    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvCn());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }
    }

    /**
     * 显示市级列表
     */
    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getProvCn());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getDistrictCn());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvCn());
            currentLevel = LEVEL_CITY;
        }
    }

    /**
     * 显示县级列表
     */
    private void queryCountries() {
        countryList = coolWeatherDB.loadCountries(selectedCity.getDistrictCn());
        if (countryList.size() > 0) {
            dataList.clear();
            for (Country country : countryList) {
                dataList.add(country.getNameCn());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getDistrictCn());
            currentLevel = LEVEL_COUNTRY;
        }
    }

    /**
     * 捕获Back按键，根据当前级别判断，是返回省、市级列表还是退出程序
     */
    @Override
    public void onBackPressed() {
        switch (currentLevel) {
            case LEVEL_COUNTRY:
                queryCities();
                break;

            case LEVEL_CITY:
                queryProvinces();
                break;

            default:
                if (isFromWeatherActivity) {
                    Intent intent = new Intent(this, WeatherActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }
}
