package com.yueban.launcherdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
  private static final String URL = "https://www.baidu.com";
  private WebView mWebView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mWebView = findViewById(R.id.web_view);
    WebSettings settings = mWebView.getSettings();
    settings.setJavaScriptEnabled(true);
    mWebView.setWebChromeClient(new WebChromeClient());
    mWebView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //if (!Tools.urlCheck(url)) {
        //  return true;
        //}
        //if (!TextUtils.isEmpty(url) && url.endsWith("apk")) {
        //  Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //  startActivity(viewIntent);
        //} else {
        if (!TextUtils.isEmpty(url)) {
          view.loadUrl(url);
          return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
      }
    });

    mWebView.loadUrl(URL);
  }

  @Override
  public void onBackPressed() {
    if (mWebView.canGoBack()) {
      mWebView.goBack();
    }
  }
}
