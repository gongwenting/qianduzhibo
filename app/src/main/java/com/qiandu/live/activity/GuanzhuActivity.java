package com.qiandu.live.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.qiandu.live.R;

public class GuanzhuActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout btnHoutui;
    private RelativeLayout layouShehuigongyue;
    private RelativeLayout layouYingsitiaokuan;
    private RelativeLayout layouFuwutiaokuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanzhu);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        btnHoutui= (RelativeLayout) findViewById(R.id.btn_houtui);
        layouShehuigongyue= (RelativeLayout) findViewById(R.id.layou_shehuigongyue);
        layouYingsitiaokuan=(RelativeLayout) findViewById(R.id.layou_yingsitiaokuan);
        layouFuwutiaokuan= (RelativeLayout) findViewById(R.id.layou_fuwutiaokuan);
        layouShehuigongyue.setOnClickListener(this);
        layouYingsitiaokuan.setOnClickListener(this);
        layouFuwutiaokuan.setOnClickListener(this);
        btnHoutui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.layou_shehuigongyue:
                startActivity(new Intent(this,ShehuigongyueActivity.class));
                break;
            case R.id.layou_yingsitiaokuan:
                startActivity(new Intent(this,YingsitiaokuanActivity.class));
                break;
            case R.id.layou_fuwutiaokuan:
                startActivity(new Intent(this,FuwutiaokuanActivity.class));
                break;
        }
    }
}
