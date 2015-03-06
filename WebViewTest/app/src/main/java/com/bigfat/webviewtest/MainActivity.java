package com.bigfat.webviewtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

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

        webView.loadUrl("http://www.baidu.com");


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTopTitle.setText(title);
            }
        });
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.id_button_top_back:
                finish();
                break;

            case R.id.id_button_top_refresh:
                webView.reload();
                break;
        }
    }
}