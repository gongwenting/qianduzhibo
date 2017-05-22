package com.qiandu.live.presenter;

import android.util.Log;

import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.LoginRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.VerifyCodeRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.logic.IUserInfoMgrListener;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.model.UserInfo;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.presenter.ipresenter.ILoginPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.AsimpleCache.CacheConstants;
import com.qiandu.live.utils.OtherUtils;

/**
 * @description: 登陆逻辑管理
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class LoginPresenter extends ILoginPresenter implements IMLogin.IMLoginListener {
	private ILoginView mLoginView;

	private IMLogin mIMLogin = IMLogin.getInstance();

	public LoginPresenter(ILoginView loginView) {
		super(loginView);
		mLoginView = loginView;
	}

	@Override
	public void start() {

	}

	@Override
	public void finish() {

	}


	@Override
	public boolean checkPhoneLogin(String phone, String verifyCode) {
		if (OtherUtils.isPhoneNumValid(phone)) {
			if (OtherUtils.isVerifyCodeValid(verifyCode)) {
				if (OtherUtils.isNetworkAvailable(mLoginView.getContext())) {
					return true;
				} else {
					mLoginView.showMsg("当前无网络连接");
				}
			} else {
				mLoginView.phoneError("验证码错误");
			}
		} else {
			mLoginView.phoneError("手机格式错误");
		}
		mLoginView.dismissLoading();
		return false;
	}

	@Override
	public boolean checkUserNameLogin(String userName, String password) {
		if (OtherUtils.isPhoneNumValid(userName)) {
			if (OtherUtils.isPasswordValid(password)) {
				if (OtherUtils.isNetworkAvailable(mLoginView.getContext())) {
					return true;
				} else {
					mLoginView.showMsg("当前无网络连接");
				}
			} else {
				mLoginView.passwordError("密码过短");
			}
		} else {
			mLoginView.usernameError("用户名不符合规范");
		}
		mLoginView.dismissLoading();
		return false;
	}

	@Override
	public void phoneLogin(final String mobile, String password) {
	}

	@Override
	public void usernameLogin(final String userName, final String password) {
		if (checkUserNameLogin(userName, password)) {
			LoginRequest req = new LoginRequest(userName, password);
			Log.e("Dazui","req"+req);
			AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
				@Override
				public void onStart(int requestId) {
					mLoginView.showLoading();
				}

				@Override
				public void onSuccess(int requestId, Response response) {
					if (response.code == RequestComm.SUCCESS) {
						UserInfo info = (UserInfo) response.data;
						Log.d("DAZUI","userid+"+info	.userid+"+sigId+"+info.sigId);
						UserInfoCache.saveCache(mLoginView.getContext(), info);
						mIMLogin.setIMLoginListener(LoginPresenter.this);
						mIMLogin.imLogin(info.userid, info.sigId);

						ACache.get(mLoginView.getContext()).put(CacheConstants.LOGIN_USERNAME, userName);
						ACache.get(mLoginView.getContext()).put(CacheConstants.LOGIN_PASSWORD, password);
						Log.d("DAZUI","成功登录im");
					} else {
						mLoginView.loginFailed(response.code, response.msg);
						mLoginView.dismissLoading();
					}
				}

				@Override
				public void onFailure(int requestId, int httpStatus, Throwable error) {
					mLoginView.loginFailed(httpStatus, error.getMessage());
					mLoginView.dismissLoading();
				}
			});
		}
	}

	@Override
	public void sendVerifyCode(String phoneNum) {
		if (OtherUtils.isPhoneNumValid(phoneNum)) {
			if (OtherUtils.isNetworkAvailable(mLoginView.getContext())) {
				VerifyCodeRequest req = new VerifyCodeRequest(phoneNum);
				AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
					@Override
					public void onStart(int requestId) {
						mLoginView.showLoading();
					}

					@Override
					public void onSuccess(int requestId, Response response) {

						if (response.code == RequestComm.SUCCESS) {
							UserInfo userInfo = (UserInfo) response.data;
							if (null != mLoginView) {
								mLoginView.verifyCodeSuccess(60, 60);
							}
						} else {
							mLoginView.verifyCodeFailed("该号码已注册过");
						}
						mLoginView.dismissLoading();
					}

					@Override
					public void onFailure(int requestId, int httpStatus, Throwable error) {
						if (null != mLoginView) {
							mLoginView.verifyCodeFailed("该号码已注册过");
						}
						mLoginView.dismissLoading();
					}
				});
			} else {
				mLoginView.showMsg("当前无网络连接");
			}
		} else {
			mLoginView.phoneError("手机号码不符合规范");
		}
	}

	public void setIMLoginListener() {
		mIMLogin.setIMLoginListener(this);
	}

	public void removeIMLoginListener() {
		mIMLogin.removeIMLoginListener();
	}

	@Override
	public void onSuccess() {
		UserInfoMgr.getInstance().setUserId(ACache.get(mBaseView.getContext()).getAsString("user_id"), new IUserInfoMgrListener() {
			@Override
			public void OnQueryUserInfo(int error, String errorMsg) {

			}

			@Override
			public void OnSetUserInfo(int error, String errorMsg) {
				if (0 != error) {
					mLoginView.showMsg("设置 User ID 失败");
				}
			}
		});
		UserInfoMgr.getInstance().setUserInfo();
		mLoginView.showMsg("登陆成功");
		//界面消失
		mIMLogin.removeIMLoginListener();
		//登录提示
		mLoginView.dismissLoading();
		//登录跳转
		mLoginView.loginSuccess();
	}

	@Override
	public void onFailure(int code, String msg) {
		Log.d("log", "IM Login Error errCode:" + code + " msg:" + msg);
		//被踢下线后弹窗显示被踢
		if (6208 == code) {
			OtherUtils.showKickOutDialog(mLoginView.getContext());
		}
		mLoginView.showMsg("登录失败");
		//登录提示
		mLoginView.dismissLoading();
		mLoginView.loginFailed(code, msg);
	}
}