package com.bigfat.coolweather.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/17
 */
public class WeatherApiUtil {

    private static final char last2byte = (char) Integer.parseInt("00000011", 2);
    private static final char last4byte = (char) Integer.parseInt("00001111", 2);
    private static final char last6byte = (char) Integer.parseInt("00111111", 2);
    private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
    private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
    private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
    private static final char[] encodeTable = new char[]{'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * 获取气象数据开放平台请求url
     *
     * @param areaid 地区码
     * @param type   获取气象数据类型
     * @return 气象数据开放平台请求url
     */
    public static String getWeatherUrl(String areaid, String type) {
        String public_key = getWeatherPublicKey(areaid, type);
        StringBuilder sb = new StringBuilder();
        sb.append(public_key.substring(0, public_key.length() - 10));
        sb.append("&key=");
        sb.append(standardURLEncoder(public_key));
        return sb.toString();
    }

    /**
     * 根据气象编号获取气象中文名称
     *
     * @param weatherId 气象编号
     * @return 气象中文名称
     */
    public static String getWeatherById(String weatherId) {
        switch (weatherId) {
            case "00":
                return "晴";

            case "01":
                return "多云";

            case "02":
                return "阴";

            case "03":
                return "阵雨";

            case "04":
                return "雷阵雨";

            case "05":
                return "雷阵雨伴有冰雹";

            case "06":
                return "雨夹雪";

            case "07":
                return "小雨";

            case "08":
                return "中雨";

            case "09":
                return "大雨";

            case "10":
                return "暴雨";

            case "11":
                return "大暴雨";

            default:
                return "";
        }
    }

    /**
     * 获取当前时间：yyyyMMddHHmm
     *
     * @return String
     */
    private static String getDate4yyyyMMddHHmm() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        // String secs = String.valueOf(c.get(Calendar.SECOND));

        return year + month + day + hour + mins;
    }

    /**
     * 拼接气象数据开放平台public_key，用于生成秘钥
     *
     * @param areaid 地区码
     * @param type   获取气象数据类型
     * @return 气象数据开放平台public_key
     */
    public static String getWeatherPublicKey(String areaid, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.WEATHER_API);
        sb.append("areaid=");
        sb.append(areaid);
        sb.append("&type=");
        sb.append(type);
        sb.append("&date=");
        sb.append(getDate4yyyyMMddHHmm());
        sb.append("&appid=");
        sb.append(Constant.WEATHER_APPID);
        return sb.toString();
    }

    /**
     * 计算气象数据开放平台传参key（令牌）
     *
     * @param public_key 明文（public_key）
     * @return 令牌
     */
    public static String standardURLEncoder(String public_key) {
        byte[] byteHMAC = null;
        String urlEncoder = "";
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(Constant.WEATHER_PRIVATE_KEY.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(public_key.getBytes());
            if (byteHMAC != null) {
                String oauth = encode(byteHMAC);
                if (oauth != null) {
                    urlEncoder = URLEncoder.encode(oauth, "utf8");
                }
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncoder;
    }

    public static String encode(byte[] from) {
        StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
        int num = 0;
        char currentByte = 0;
        for (int i = 0; i < from.length; i++) {
            num = num % 8;
            while (num < 8) {
                switch (num) {
                    case 0:
                        currentByte = (char) (from[i] & lead6byte);
                        currentByte = (char) (currentByte >>> 2);
                        break;
                    case 2:
                        currentByte = (char) (from[i] & last6byte);
                        break;
                    case 4:
                        currentByte = (char) (from[i] & last4byte);
                        currentByte = (char) (currentByte << 2);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead2byte) >>> 6;
                        }
                        break;
                    case 6:
                        currentByte = (char) (from[i] & last2byte);
                        currentByte = (char) (currentByte << 4);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead4byte) >>> 4;
                        }
                        break;
                }
                to.append(encodeTable[currentByte]);
                num += 6;
            }
        }
        if (to.length() % 4 != 0) {
            for (int i = 4 - to.length() % 4; i > 0; i--) {
                to.append("=");
            }
        }
        return to.toString();
    }
}