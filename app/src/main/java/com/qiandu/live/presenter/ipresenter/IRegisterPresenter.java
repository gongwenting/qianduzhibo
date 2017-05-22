package com.qiandu.live.presenter.ipresenter;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;

/**
 * author : qubian on 2016/12/26 11:08
 * description :
 */

public abstract class IRegisterPresenter implements BasePresenter {
	protected BaseView mBaseView;

	public IRegisterPresenter(BaseView baseView) {
		mBaseView = baseView;
	}

	public abstract void sendVerifyCode(String phoneNum);

	protected abstract boolean checkNormalRegister(String username, String password, String passwordVerify);

	protected abstract boolean checkPhoneRegister(String phoneNum, String verifyCode);

	public abstract void normalRegister(final String username, final String password, String passwordVerify);

	public abstract void phoneRegister(final String username, String verifyCode);


	public interface IRegisterView extends BaseView {

		/**
		 * 注册成功
		 */
		void onSuccess(String username);

		/**
		 * 注册失败
		 *
		 * @param code 错误码
		 * @param msg  错误信息
		 */
		void onFailure(int code, String msg);

		void showPasswordError(String errorMsg);

		void showPhoneError(String errorMsg);

		void showRegistError(String errorMsg);

		void verifyCodeError(String errorMsg);

		void verifyCodeFailed(String errorMsg);

		void verifyCodeSuccess(int reaskDuration, int expireDuration);

	}

}
