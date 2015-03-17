package com.bigfat.volleytest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    public static final String appkey = "1672939984";
    public static final String appSecret = "af036026d51e425d9d17c6cae5d8465a";

    public static final String url = "https://raw.githubusercontent.com/yueban/AndroidExercise/master/VolleyTest/app/src/main/java/com/bigfat/volleytest/MainActivity.java";
    public static final String url_post = "http://webservice.ncuhome.cn/NcuhomeUS.asmx/getEmployeeInfoByEmp_Name";
    public static final String url_post_mingdao = "https://api2.mingdao.com/oauth2/access_token";
    public static final String url_img = "http://img1.gamersky.com/image2015/03/20150316ydx_5/gamersky_014origin_027_20153161758A83.jpg";

    private ImageView imgMain;
    private NetworkImageView netImgMain;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        requestQueue = Volley.newRequestQueue(MainActivity.this);
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();
//        test8();
    }

    private void initView() {
        imgMain = (ImageView) findViewById(R.id.img_main);
        netImgMain = (NetworkImageView) findViewById(R.id.netImg_main);
    }

    private void test1() {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void test2() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Emp_Name", "范柏舟");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void test3() throws JSONException {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_post, new JSONObject().put("Emp_Name", "范柏舟"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void test4() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", appkey);
        jsonObject.put("app_secret", appSecret);
        jsonObject.put("grant_type", "password");
        jsonObject.put("format", "json");
        jsonObject.put("p_signature", "");
        jsonObject.put("username", "Peter.Fan@mingdao.com");
        jsonObject.put("password", "Mwvagh45D!@#");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_post_mingdao, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void test5() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_post_mingdao,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("app_key", appkey);
                map.put("app_secret", appSecret);
                map.put("grant_type", "password");
                map.put("format", "json");
                map.put("p_signature", "");
                map.put("username", "Peter.Fan@mingdao.com");
                map.put("password", "Mwvagh45D!@#");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void test6() {
        ImageRequest imageRequest = new ImageRequest(url_img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgMain.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgMain.setImageResource(R.mipmap.ic_launcher);
            }
        });
        requestQueue.add(imageRequest);
    }

    private void test7() {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imgMain, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(url_img, imageListener, 200, 200);
    }

    private void test8() {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        netImgMain.setDefaultImageResId(R.mipmap.ic_launcher);
        netImgMain.setErrorImageResId(R.mipmap.ic_launcher);
        netImgMain.setImageUrl(url_img, imageLoader);
    }
}
