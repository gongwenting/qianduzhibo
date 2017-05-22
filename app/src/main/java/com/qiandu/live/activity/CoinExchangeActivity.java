
package com.qiandu.live.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.utils.AsimpleCache.ACache;

public class CoinExchangeActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private RelativeLayout btnHoutui;
    private TextView withdrawRecord;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_exchange;
    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_zhubo_renzhen);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        btnHoutui= (RelativeLayout) findViewById(R.id.btn_houtui);
        withdrawRecord = obtainView(R.id.recode_withdraw);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.loadUrl("http://www.qianduzhibo.com/h5/bili.html?user_id="+ ACache.get(this).getAsString("user_id"));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btnHoutui.setOnClickListener(this);
        withdrawRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.recode_withdraw:
                // TODO: 2017/5/16
                startActivity(new Intent(CoinExchangeActivity.this, ShouyiActivity.class));
                break;
        }
    }
}
