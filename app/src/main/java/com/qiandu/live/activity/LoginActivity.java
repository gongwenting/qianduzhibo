package com.qiandu.live.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.presenter.LoginPresenter;
import com.qiandu.live.presenter.ipresenter.ILoginPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.AsimpleCache.CacheConstants;
import com.qiandu.live.utils.OtherUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @Description: 登陆页面
 * @author: Andruby
 * @date: 2016年7月8日 下午4:46:44
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginPresenter.ILoginView, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //共用控件
    private RelativeLayout rootRelativeLayout;
    private ProgressBar progressBar;
    private EditText etPassword;
    private EditText etLogin;
    private TextView btnLogin;
    private TextInputLayout tilLogin, tilPassword;
    private TextView btnRegister;
    private TextView zhuce;
    private ImageView QQ;
    private ImageView weixin;
    //手机验证登陆控件
    private TextView tvVerifyCode;
    private LoginPresenter mLoginPresenter;

    private TextView forgetPswd;

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mLoginPresenter = new LoginPresenter(this);
        zhuce= (TextView) findViewById(R.id.zhuce);
        etLogin = obtainView(R.id.et_login);
        etPassword = obtainView(R.id.et_password);
//        btnRegister = obtainView(R.id.btn_register);
        btnLogin = obtainView(R.id.btn_login);
        tvVerifyCode = obtainView(R.id.btn_verify_code);
        forgetPswd = obtainView(R.id.forget_pswd);
        QQ=obtainView(R.id.qq);
        weixin=obtainView(R.id.weixin);
        QQ.setOnClickListener(this);
        weixin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        forgetPswd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        etLogin.setText(ACache.get(this).getAsString(CacheConstants.LOGIN_USERNAME));
        etPassword.setText(ACache.get(this).getAsString(CacheConstants.LOGIN_PASSWORD));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置登录回调,resume设置回调避免被registerActivity冲掉
        mLoginPresenter.setIMLoginListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //删除登录回调
        mLoginPresenter.removeIMLoginListener();
    }

    //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mLoginPresenter.usernameLogin(etLogin.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.btn_verify_code:
                mLoginPresenter.sendVerifyCode(etLogin.getText().toString());
                break;
            case R.id.zhuce:
                RegisterActivity.invoke(this);
                break;
            case R.id.forget_pswd:
                ForgetPswdActivity.invoke(this);
                break;
            case R.id.qq:
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.weixin:
//                UmengTool.checkWx(LoginActivity.this);
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
        }
    }

    @Override
    public void loginSuccess() {
        dismissLoading();
        MainActivity.invoke(this);
        finish();
    }

    @Override
    public void loginFailed(int status, String msg) {
        dismissLoading();
        showMsg("登陆失败:" + msg);
    }

    @Override
    public void usernameError(String errorMsg) {
        etLogin.setError(errorMsg);
    }

    @Override
    public void phoneError(String errorMsg) {
        etLogin.setError(errorMsg);
    }

    @Override
    public void passwordError(String errorMsg) {
        etPassword.setError(errorMsg);
    }

    @Override
    public void verifyCodeError(String errorMsg) {
        showMsg("验证码错误");
    }

    @Override
    public void verifyCodeFailed(String errorMsg) {
        showMsg("获取验证码失败");
    }

    @Override
    public void verifyCodeSuccess(int reaskDuration, int expireDuration) {
        showMsg("注册短信下发,验证码" + expireDuration / 60 + "分钟内有效");
        OtherUtils.startTimer(new WeakReference<>(tvVerifyCode), "验证码", reaskDuration, 1);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void dismissLoading() {
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void showMsg(int resId) {
        showToast(resId);
    }

    @Override
    public Context getContext() {
        return this;
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}