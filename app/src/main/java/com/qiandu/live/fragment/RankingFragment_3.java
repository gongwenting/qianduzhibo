package com.qiandu.live.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qiandu.live.R;
import com.qiandu.live.http.request.IRequest;
import com.qiandu.live.utils.AsimpleCache.ACache;


/**
 * Created by admin on 2017/3/30.
 */
public class RankingFragment_3 extends android.support.v4.app.Fragment implements View.OnClickListener {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_3, container, false);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.reload();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new android.webkit.WebChromeClient() {

        });
        webView.loadUrl(IRequest.HTMLURL+"/jiazupaihang.html?userid="+ ACache.get(getActivity()).getAsString("user_id"));
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}