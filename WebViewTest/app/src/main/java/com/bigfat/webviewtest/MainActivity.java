package com.bigfat.webviewtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btnTopBack;
    private Button btnTopRefresh;
    private TextView tvTopTitle;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        initWebView();

        webView.loadUrl("http://shouji.baidu.com/software/");
    }

    private void initView() {
        btnTopBack = (Button) findViewById(R.id.id_button_top_back);
        btnTopRefresh = (Button) findViewById(R.id.id_button_top_refresh);
        tvTopTitle = (TextView) findViewById(R.id.id_textview_top_title);
        webView = (WebView) findViewById(R.id.id_webView);
    }

    private void initEvent() {
        btnTopBack.setOnClickListener(this);
        btnTopRefresh.setOnClickListener(this);
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTopTitle.setText(title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("file:///android_asset/error.html");
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.i(TAG, "downloadUrl--->" + url);
                if (url.endsWith(".apk")) {
                    //开启线程下载
//                    new HttpThread(url).start();

                    //调用系统下载
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_button_top_back:
                finish();
                break;

            case R.id.id_button_top_refresh:
                webView.reload();
                break;
        }
    }
}