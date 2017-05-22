package com.qiandu.live.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.qiandu.live.R;
import com.qiandu.live.adapter.HomeHotAdapter;

import com.qiandu.live.adapter.WealthCoinAdapter;
import com.qiandu.live.http.AsyncHttp;

import com.qiandu.live.http.request.MoneyInfoRequest;
import com.qiandu.live.http.request.MoneyUserInfoRequest;
import com.qiandu.live.http.request.PayRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.MoneyUsetInfo;
import com.qiandu.live.http.response.Oderstring;
import com.qiandu.live.http.response.ResList;

import com.qiandu.live.http.response.Response;
import com.qiandu.live.http.response.WealthCoinInfo;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.PayResult;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaifuActivity extends AppCompatActivity implements View.OnClickListener {
    private int co;
    private boolean no;
    private Button apigo;
    private RelativeLayout houttui;
    private ProgressDialog progressDialog;

    private RecyclerView rvCoinList;
    private static final int ALIPAY_CODE = 0;
    private WealthCoinAdapter mWealthCoinAdapter;
    private List<WealthCoinInfo> wealthCoinInfoList;

    private TextView wealthAccount;
    private ImageView wealthLevel;
    private TextView wealthCoin;
    private TextView wealthLeft;

    private String payCoin;
    private String payMoney;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALIPAY_CODE:

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.d("API","resultInfo++++++"+resultInfo);
                    LogUtil.d("API","resultStatus++++++"+resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //       getThree(sharedPreferences.getString("userid",""));
                        Toast.makeText(CaifuActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                        // TODO: 2017/5/20
                        requestUserAndMoneyInfo();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(CaifuActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        requestUserAndMoneyInfo();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caifu);
        initView();
        infoData();

        initData();
    }

    private void initData() {
        wealthCoinInfoList = new ArrayList<>();

        requestUserAndMoneyInfo();

        requestCoinAndMoney();

    }

    private void requestUserAndMoneyInfo() {
        MoneyUserInfoRequest moneyUserInfoRequest = new MoneyUserInfoRequest(ACache.get(this).getAsString("user_id"));
        AsyncHttp.instance().post(moneyUserInfoRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                LogUtil.i("INFO", response.msg);
                final MoneyUsetInfo moneyUsetInfo = (MoneyUsetInfo) response.data;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 2017/5/18
                        wealthCoin.setText(moneyUsetInfo.getCoin());
                        wealthAccount.setText(moneyUsetInfo.getUsername());
                        wealthLeft.setText(moneyUsetInfo.getBalance());
                        RequestManager req = Glide.with(getApplicationContext());
                        req.load(moneyUsetInfo.getLevel_img()).placeholder(R.drawable.lv01).into(wealthLevel);

//                        LogUtil.i("", );
                    }
                });
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    private void requestCoinAndMoney() {
        MoneyInfoRequest request = new MoneyInfoRequest(ACache.get(this).getAsString("user_id"));
        AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                ResList<WealthCoinInfo> resList = (ResList<WealthCoinInfo>) response.data;
                ArrayList<WealthCoinInfo> result = (ArrayList<WealthCoinInfo>) resList.datas;

                LogUtil.i("CAIFU", result.size()+"");
                LogUtil.i("CAIFU", result.get(0).getMoney());
                LogUtil.i("CAIFU", result.get(0).getCoin());

                wealthCoinInfoList.addAll(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWealthCoinAdapter = new WealthCoinAdapter(getApplicationContext(), wealthCoinInfoList);
                        rvCoinList.setAdapter(mWealthCoinAdapter);
                        rvCoinList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        mWealthCoinAdapter.setOnItemClickListener(new HomeHotAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), wealthCoinInfoList.get(position).getCoin()+
                                        "&"+wealthCoinInfoList.get(position).getMoney(), Toast.LENGTH_SHORT).show();

                                payCoin = wealthCoinInfoList.get(position).getCoin();
                                payMoney = wealthCoinInfoList.get(position).getMoney();
                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    private void infoData() {
        apigo.setOnClickListener(this);
        houttui.setOnClickListener(this);
    }

    private void initView() {
        rvCoinList = (RecyclerView) findViewById(R.id.rv_caifu_list);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        apigo = (Button) findViewById(R.id.img_xiayibu);
        houttui= (RelativeLayout) findViewById(R.id.btn_houtui);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        wealthCoin = (TextView) findViewById(R.id.wealth_user_coin);
        wealthAccount = (TextView) findViewById(R.id.wealth_user_account);
        wealthLeft = (TextView) findViewById(R.id.wealth_user_left);
        wealthLevel = (ImageView) findViewById(R.id.wealth_user_level);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.img_xiayibu:
                if (!TextUtils.isEmpty(payCoin) && !TextUtils.isEmpty(payMoney)){
                    pay(ACache.get(this).getAsString("user_id"),payCoin,payMoney);
                }else{
                    Toast.makeText(getApplicationContext(), "请选择充值的选项", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private void pay(String user_id,String subject ,String money ) {
        PayRequest pay=new PayRequest(user_id,subject,money);
        AsyncHttp.instance().post(pay, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {
            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    Oderstring oder=(Oderstring)response.data;
                    LogUtil.d("dddddddddd",""+oder.getOrderstring().toString());
                    String ii=oder.getOrderstring();
                    String gogogo=ii.replace("amp;","");
                    AliPay(gogogo);
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }
    private void  AliPay(final String oder){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                PayTask alipay=new PayTask(CaifuActivity.this);
                Map<String,String> result=alipay.payV2(oder,true);
                Message message=new Message();
                message.what=ALIPAY_CODE;
                message.obj=result;
                handler.sendMessage(message);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(runnable);
        payThread.start();
    }
}
