package com.qiandu.live.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.qiandu.live.R;
import com.qiandu.live.utils.AsimpleCache.ACache;

public class ShehuigongyueActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout btnHoutui;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shehuigongyue);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        webView = (WebView) findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        btnHoutui= (RelativeLayout) findViewById(R.id.btn_houtui);
        btnHoutui.setOnClickListener(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.loadUrl("http://zhibonew.zzsike.com/h5/shequ.html?userid="+ ACache.get(this).getAsString("user_id"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_houtui:
                finish();
                break;
        }
    }
}