package com.bigfat.coolweather.util;

import android.text.TextUtils;

import com.bigfat.coolweather.db.CoolWeatherDB;
import com.bigfat.coolweather.model.City;
import com.bigfat.coolweather.model.Country;
import com.bigfat.coolweather.model.Province;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
            List<HashMap<String, String>> list = parseXMLWithDom(response);
            if (list != null && list.size() > 0) {
                for (HashMap<String, String> map : list) {
                    Province province = new Province();
                    province.setPyName(map.get("pyName"));
                    province.setQuName(map.get("quName"));
                    coolWeatherDB.saveProvince(province);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            List<HashMap<String, String>> list = parseXMLWithDom(response);
            if (list != null && list.size() > 0) {
                for (HashMap<String, String> map : list) {
                    City city = new City();
                    city.setCityname(map.get("cityname"));
                    city.setPyName(map.get("pyName"));
                    city.setUrl(map.get("url"));
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            List<HashMap<String, String>> list = parseXMLWithDom(response);
            if (list != null && list.size() > 0) {
                for (HashMap<String, String> map : list) {
                    Country country = new Country();
                    country.setCityname(map.get("cityname"));
                    country.setUrl(map.get("url"));
                    country.setCityId(cityId);
                    coolWeatherDB.saveCountry(country);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 将xml数据解析为List<HashMap<String, String>>集合
     */
    public static List<HashMap<String, String>> parseXMLWithDom(String xmlData) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            List<HashMap<String, String>> list = new ArrayList<>();
            document = reader.read(new StringReader(xmlData));
            //获取根节点
            Element elementRoot = document.getRootElement();
            for (Iterator<Element> elementIterator = elementRoot.elementIterator(); elementIterator.hasNext(); ) {
                HashMap<String, String> map = new HashMap<>();
                Element element = elementIterator.next();
                for (Iterator<Attribute> attrIterator = element.attributeIterator(); attrIterator.hasNext(); ) {
                    Attribute attribute = attrIterator.next();
                    map.put(attribute.getName(), attribute.getValue());
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
