package com.bigfat.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigfat.coolweather.R;
import com.bigfat.coolweather.db.CoolWeatherDB;
import com.bigfat.coolweather.model.City;
import com.bigfat.coolweather.model.Country;
import com.bigfat.coolweather.model.Province;
import com.bigfat.coolweather.util.HttpCallbackListener;
import com.bigfat.coolweather.util.HttpUtil;
import com.bigfat.coolweather.util.Utility;
import com.bigfat.coolweather.util.WeatherApiUtil;

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

    private ProgressDialog progressDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        String url = WeatherApiUtil.getWeatherUrl("101010100", "index_f");
        Log.d(TAG, "url--->" + url);
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "response--->" + response);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
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
                }
            }
        });

        queryProvinces();
    }

    /**
     * 查询全国所有的生，优先从数据库查询
     */
    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getQuName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询选中的省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getQuName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getPyName(), "city");
        }
    }

    /**
     * 查询选中的城市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCountries() {
        countryList = coolWeatherDB.loadCountries(selectedCity.getId());
        if (countryList.size() > 0) {
            dataList.clear();
            for (Country country : countryList) {
                dataList.add(country.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityname());
            currentLevel = LEVEL_COUNTRY;
        } else {
            queryFromServer(selectedCity.getPyName(), "country");
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县的数据
     */
    private void queryFromServer(final String pyName, final String type) {
        String address;
        if (!TextUtils.isEmpty(pyName)) {
            address = "http://flash.weather.com.cn/wmaps/xml/" + pyName + ".xml";
        } else {
            address = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                switch (type) {
                    case "province":
                        result = Utility.handleProvincesResponse(coolWeatherDB, response);
                        break;

                    case "city":
                        result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
                        break;

                    case "country":
                        result = Utility.handleCountriesResponse(coolWeatherDB, response, selectedCity.getId());
                        break;
                }
                if (result) {
                    //通过runOnUiThread方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (type) {
                                case "province":
                                    queryProvinces();
                                    break;

                                case "city":
                                    queryCities();
                                    break;

                                case "country":
                                    queryCountries();
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                //通过runOnUiThread方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
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
                finish();
                break;
        }
    }
}
