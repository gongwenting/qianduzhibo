package com.qiandu.live.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.http.request.IRequest;
import com.qiandu.live.utils.AsimpleCache.ACache;


public class ShouyiActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private RelativeLayout btnHoutui;
    private TextView withdrawRecode;

    private RecyclerView rvDetailInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shouyi;
    }

    @Override
    protected void initView() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        btnHoutui= (RelativeLayout) findViewById(R.id.btn_houtui);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.loadUrl(IRequest.HTMLURL+"/tixian.html?user_id="+ ACache.get(this).getAsString("user_id"));

        withdrawRecode = obtainView(R.id.recode_withdraw);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btnHoutui.setOnClickListener(this);
        withdrawRecode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.recode_withdraw:
                // TODO: 2017/5/16
                startActivity(new Intent(ShouyiActivity.this, ShouyiActivity.class));
                break;
        }
    }
}
