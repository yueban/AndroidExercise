package com.bigfat.volleytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        try {
//            test1();
//            test2();
//            test3();
            test4();
            test5();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        }){
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
}
