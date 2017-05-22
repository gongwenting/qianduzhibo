package com.qiandu.live.presenter;

import android.util.Log;

import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.model.UserInfo;
import com.qiandu.live.http.request.RegisterRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.VerifyCodeRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.presenter.ipresenter.IRegisterPresenter;
import com.qiandu.live.utils.OtherUtils;

/**
 * @description: 注册信息管理
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class RegisterPresenter extends IRegisterPresenter {
	public static final String TAG = RegisterPresenter.class.getSimpleName();
	private IRegisterView mIRegisterView;

	public RegisterPresenter(IRegisterView baseView) {
		super(baseView);
		mIRegisterView = baseView;
	}

	@Override
	public void start() {

	}

	@Override
	public void finish() {

	}

	@Override
	public void sendVerifyCode(String phoneNum) {
		if (OtherUtils.isPhoneNumValid(phoneNum)) {
			if (OtherUtils.isNetworkAvailable(mIRegisterView.getContext())) {
				VerifyCodeRequest req = new VerifyCodeRequest(phoneNum);
				AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
					@Override
					public void onStart(int requestId) {
						mIRegisterView.showLoading();
					}

					@Override
					public void onSuccess(int requestId, Response response) {
						Log.d("yanzhengma", "发送成功");
						if (response.code == RequestComm.SUCCESS) {

//                            if (null != mIRegisterView) {
////                                mIRegisterView.verifyCodeSuccess(60, 60);
//                            }
//                            if (response.code == RequestComm.livestatus) {
//                                mIRegisterView.verifyCodeFailed("该号码已注册过");
//                            }
						} else {
							mIRegisterView.verifyCodeFailed("该号码已注册过");
						}
						mIRegisterView.dismissLoading();
					}

					@Override
					public void onFailure(int requestId, int httpStatus, Throwable error) {
						Log.d("yanzhengma", "发送失败");
						if (null != mIRegisterView) {
							mIRegisterView.verifyCodeFailed("该号码已注册过");
						}
						mIRegisterView.dismissLoading();
					}
				});
			} else {
				mIRegisterView.showMsg("当前无网络连接");
			}
		} else {
			mIRegisterView.showRegistError("手机号码不符合规范");
		}

	}


	@Override
	protected boolean checkNormalRegister(String username, String verifyCode, String password) {
		if (OtherUtils.isPhoneNumValid(username)) {
			if (OtherUtils.isVerifyCodeValid(verifyCode)) {
				if (OtherUtils.isPasswordValid(password)) {
					if (OtherUtils.isNetworkAvailable(mIRegisterView.getContext())) {
						return true;
					} else {
						mIRegisterView.showMsg("当前无网络连接");
					}
				} else {
					mIRegisterView.showPasswordError("验证码格式错误");
				}
			} else {
				mIRegisterView.showPasswordError("密码长度应为6-16位");
			}
		} else {
			mIRegisterView.showRegistError("用户名不符合规范");
		}
		return false;
	}

	@Override
	protected boolean checkPhoneRegister(String phoneNum, String verifyCode) {
		if (OtherUtils.isPhoneNumValid(phoneNum)) {
			if (OtherUtils.isVerifyCodeValid(verifyCode)) {
				if (OtherUtils.isNetworkAvailable(mIRegisterView.getContext())) {
					return true;
				} else {
					mIRegisterView.showMsg("当前无网络连接");
				}
			} else {
				mIRegisterView.showPasswordError("验证码格式错误");
			}
		} else {
			mIRegisterView.showPhoneError("手机号码不符合规范");
		}
		return false;
	}

	/**
	 * tls用户名注册
	 *
	 * @param username   用户名
	 * @param verifyCode 密码
	 */
	@Override
	public void normalRegister(final String username, final String verifyCode, final String password) {
		if (checkNormalRegister(username, verifyCode, password)) {
			RegisterRequest req = new RegisterRequest(username, verifyCode, password);
			AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
				@Override
				public void onStart(int requestId) {
					mIRegisterView.showLoading();
				}

				@Override
				public void onSuccess(int requestId, Response response) {
					if (response.code == RequestComm.SUCCESS) {
						UserInfo userInfo = (UserInfo) response.data;
						if (null != mIRegisterView) {
							mIRegisterView.onSuccess(username);
						}
					} else {
						mIRegisterView.onFailure(response.code, response.msg);
					}
					mIRegisterView.dismissLoading();
				}

				@Override
				public void onFailure(int requestId, int httpStatus, Throwable error) {
					if (null != mIRegisterView) {
						mIRegisterView.onFailure(httpStatus, error.getMessage());
					}
					mIRegisterView.dismissLoading();
				}
			});
		}

	}

	@Override
	public void phoneRegister(String mobile, String verifyCode) {
//		if (checkPhoneRegister(mobile, verifyCode)) {
//			PhoneRegisterRequest req = new PhoneRegisterRequest(RequestComm.register, mobile, verifyCode);
//			AsyncHttp.instance().postJson(req, new AsyncHttp.IHttpListener() {
//				@Override
//				public void onStart(int requestId) {
//					mIRegisterView.showLoading();
//				}
//
//				@Override
//				public void onSuccess(int requestId, Response response) {
//					if (response.code == RequestComm.SUCCESS) {
//						if (null != mIRegisterView) {
//							mIRegisterView.onSuccess(null);
//						}
//					} else {
//						mIRegisterView.verifyCodeError("验证码不正确");
//					}
//					mIRegisterView.dismissLoading();
//				}
//
//				@Override
//				public void onFailure(int requestId, int httpStatus, Throwable error) {
//					if (null != mIRegisterView) {
//						mIRegisterView.onFailure(httpStatus, "网络异常");
//					}
//					mIRegisterView.dismissLoading();
//				}
//			});
//		}
	}

}