package com.qiandu.live.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.qiandu.live.R;
import com.qiandu.live.http.request.IRequest;
import com.qiandu.live.utils.AsimpleCache.ACache;

import java.io.File;

/**
 * Created by admin on 2017/3/30.
 */
public class RankingFragment_2 extends android.support.v4.app.Fragment implements View.OnClickListener {
    private WebView webView;
    private static final String TAG = RankingFragment_2.class.getSimpleName();
    //设置缓存webview的路径
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private LinearLayout webViewLayout;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_2, container, false);
        webViewLayout = (LinearLayout) view.findViewById(R.id.webview_layout);
        webView = new WebView(getActivity().getApplicationContext());
        webViewLayout.addView(webView);

        WebSettings webSettings = webView.getSettings();
        //设置渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        webSettings.setLoadWithOverviewMode(true);
        //设置WebView支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("UTF-8");

        if (isNetworkAvailable(getActivity().getApplicationContext())) {
            //有网络连接，设置默认缓存模式
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //无网络连接，设置本地缓存模式
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100){
                    if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                }else{
                    if(dialog == null){
                        dialog = new ProgressDialog(getActivity());
                        dialog.setTitle("正在加载中.....");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setProgress(newProgress);
                        dialog.show();
                    }else{
                        dialog.setProgress(newProgress);
                    }
                }
            }
        });
        /**
         * WebViewClient帮助webView处理一些页面控制和请求通知
         * 当点击webview控件中的链接时,在这里设置显示在webview中,否则,会在浏览器打开
         */

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置网页在webview控件中打开,false在浏览器中打开
                view.loadUrl(url);
                return true;
            }
        });


        initData();
        return view;
    }

    private void initData() {
        webView.loadUrl(IRequest.HTMLURL+"/fuhao.html?userid="+ ACache.get(getActivity()).getAsString("user_id"));
    }



    /**
     * 检测当前网络可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            getActivity().deleteDatabase("webview.db");
            getActivity().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getActivity().getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
    @Override
    public void onClick(View view) {

    }
}