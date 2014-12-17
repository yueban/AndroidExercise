package com.bigfat.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.bigfat.coolweather.db.CoolWeatherDB;
import com.bigfat.coolweather.model.City;
import com.bigfat.coolweather.model.Country;
import com.bigfat.coolweather.model.Province;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.Iterator;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public class Utility {

    public static final String TAG = "Utility";

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setPyName(array[0]);
                    province.setQuName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setPyName(array[0]);
                    city.setCityname(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String c : allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setUrl(array[0]);
                    country.setCityname(array[1]);
                    country.setCityId(cityId);
                    coolWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    public static void parseXMLWithDom(String xmlData) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new StringReader(xmlData));
            //获取根节点
            Element elementRoot = document.getRootElement();
            Log.d(TAG, "根节点：" + elementRoot.getName());
            for (Iterator<Element> elementIterator = elementRoot.elementIterator(); elementIterator.hasNext(); ) {
                Element element = elementIterator.next();
                Log.d(TAG, "节点：" + element.getName());
                for (Iterator<Attribute> attrIterator = element.attributeIterator(); attrIterator.hasNext(); ) {
                    Attribute attribute = attrIterator.next();
                    Log.d(TAG, "属性：" + attribute.getName() + "\t值：" + attribute.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
