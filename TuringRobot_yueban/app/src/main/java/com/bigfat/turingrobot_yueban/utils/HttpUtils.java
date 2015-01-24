package com.bigfat.turingrobot_yueban.utils;

import com.alibaba.fastjson.JSON;
import com.bigfat.turingrobot_yueban.bin.ChatMessage;
import com.bigfat.turingrobot_yueban.bin.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/23
 */
public class HttpUtils {
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "e77d3093ba29664c9de2b9461f312e8b";

    /**
     * 发送一个消息，得到返回的消息
     */
    public static ChatMessage sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        String jsonResult = doGet(msg);
        Result result = JSON.parseObject(jsonResult, Result.class);
        try {
            chatMessage.setMsg(result.getText());
        } catch (Exception e) {
            chatMessage.setMsg("服务器繁忙，请稍后再试");
        }
        chatMessage.setType(ChatMessage.Type.INCOMING);
        chatMessage.setDate(new Date());

        return chatMessage;
    }

    /**
     * 发送HttpGet请求，得到返回结果
     */
    public static String doGet(String msg) {
        String result = "";
        String requestUrl = setParams(msg);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            //发送HttpGet请求
            java.net.URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            //获得返回数据
            is = conn.getInputStream();
            //读取返回数据
            int len;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();//清除缓冲区
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String setParams(String msg) {
        String url = "";
        try {
            url = URL + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
