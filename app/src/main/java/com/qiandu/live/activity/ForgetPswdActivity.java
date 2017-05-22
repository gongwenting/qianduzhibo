package com.qiandu.live.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.ForgetPswdRequest;
import com.qiandu.live.http.request.ForgetPswdSMSRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;

public class ForgetPswdActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private String mPassword;
    //共用控件
    private LinearLayout relativeLayout;
    private ProgressBar progressBar;
    private EditText etPassword;
    private EditText etPasswordVerify;
    private EditText etRegister;
    private TextView tvPhoneRegister;
    private TextInputLayout tilRegister, tilPassword, tilPasswordVerify;
    private TextView btnReswtPswd;
    private TextView tvBackBtn;
    //手机验证注册控件
    private Button tvVerifyCode;

    public static void invoke(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ForgetPswdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pswd;
    }

    @Override
    protected void initView() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        etRegister = obtainView(R.id.et_register);
        etPassword = obtainView(R.id.et_password);
        etPasswordVerify=obtainView(R.id.et_yanzhengma);
        btnReswtPswd = obtainView(R.id.btn_reset_pswd);
        tvVerifyCode = obtainView(R.id.btn_verify_code);
        tvBackBtn = obtainView(R.id.tv_back);
        relativeLayout = (LinearLayout) findViewById(R.id.rl_register_root);
    }

    @Override
    protected void initData() {
        tvVerifyCode.setOnClickListener(this);
        btnReswtPswd.setOnClickListener(this);
        tvBackBtn.setOnClickListener(this);
    }

    @Override
    protected void setListener() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verify_code:
                requestVertifycode();
                break;
            case R.id.btn_reset_pswd:
                requestResetPswd();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void requestResetPswd() {

        if(OtherUtils.isPhoneNumValid(etRegister.getText().toString())){
            if (OtherUtils.isVerifyCodeValid(etPasswordVerify.getText().toString())){
                if (OtherUtils.isPasswordValid(etPassword.getText().toString())){
                    String mobile = etRegister.getText().toString();
                    String vertifycode = etPasswordVerify.getText().toString();
                    String newPswd = etPassword.getText().toString();

                    ForgetPswdRequest request = new ForgetPswdRequest(mobile, vertifycode, newPswd);
                    AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
                        @Override
                        public void onStart(int requestId) {

                        }

                        @Override
                        public void onSuccess(int requestId, Response response) {
                            String msg = response.msg;
                            LogUtil.i("MSG","msg="+ msg);

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int requestId, int httpStatus, Throwable error) {

                        }
                    });
                } else{
                    Toast.makeText(getApplicationContext(), "密码为空", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "验证码为空", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "手机号为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestVertifycode() {

        if (!TextUtils.isEmpty(etRegister.getText().toString())){
            ForgetPswdSMSRequest request = new ForgetPswdSMSRequest(etRegister.getText().toString());
            AsyncHttp.instance().post(request, new AsyncHttp.IHttpListener() {
                @Override
                public void onStart(int requestId) {

                }

                @Override
                public void onSuccess(int requestId, Response response) {
                    String msg = response.msg;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(int requestId, int httpStatus, Throwable error) {

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }


    }
}
