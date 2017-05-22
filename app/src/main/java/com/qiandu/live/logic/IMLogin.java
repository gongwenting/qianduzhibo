package com.qiandu.live.logic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSSmsLoginListener;
import tencent.tls.platform.TLSUserInfo;


/**
 * @description: IM登录模块
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class IMLogin {

	public static final String TAG = IMLogin.class.getSimpleName();
	private static IMLogin mInstance = null;

	private IMLogin() {
	}

	public static IMLogin getInstance() {
		if (mInstance == null) {
			synchronized (IMLogin.class) {
				mInstance = new IMLogin();
			}
		}

		return mInstance;
	}


	/*********************************************************************************************************************
	 * 登录模块介绍：
	 * TLS登录支持三种模式：1.托管模式 2.游客模式 3.独立模式
	 * <p>
	 * 1.托管模式：
	 * 1.1) 用户名密码等账号信息托管在腾讯后台，这是我们在苹果AppStore上的APP所采用的模式
	 * 1.2) 调用TLS(tencent login service)获取TLSUserInfo,内部包含了短期有效的id和签名，这段逻辑详见 LoginActivity.java
	 * 1.3) 使用TLS登录返回的id和签名，调用TCLoginMgr的imLogin()函数完成IM模块的登录，之后就可以收发消息了
	 * <p>
	 * 2.游客模式：
	 * 2.1) 如果您已有帐号体系，适合使用这种模式，该模式下腾讯云通讯模块会使用内部的一些匿名账号进行消息的收发。
	 * 2.2) 这种模式下，您只需要调用TCLoginMgr的guestLogin()函数即可实现，内部流程跟托管模式类似，只是账号换成了随机生成的匿名账号。
	 * 2.3) 如果想要将IM模块跟您的账号体系进行结合，实现您的两个账号间的私信收发，请看独立模式。
	 * <p>
	 * 3.独立模式
	 * 3.1) 如果您已有帐号体系且需要支持可靠的C2C消息，则需要使用此种方式。
	 * 此模式的目标是将腾讯云通讯模块跟您的账号体系结合起来实现安全可靠的消息通讯。
	 * 3.2) 您先使用自己的登录逻辑进行登录，之后交给您的服务器验证当前用户是不是一个合法的用户。
	 * 3.3) 如果当前用户通过验证，您的登录服务器要使用跟腾讯云协商的非对称加密密钥对用户ID进行签名。
	 * 这就好比您的服务器给这个用户ID做了担保：“这是个好孩子，我给他担保，有公章在此，请给他通过。”
	 * 3.4) APP在收到用户ID和签名后，TCLoginMgr的imLogin()函数完成IM模块的登录，之后就可以收发消息了
	 * <p>
	 * *******************************************************************************************************************
	 * <p>
	 * 如下是本Class的全流程代码逻辑介绍：
	 * <p>
	 * 1.注册登陆回调(IMLogin#setIMLoginListener(IMLoginListener))
	 * <p>
	 * 2.获取用户ID与PWD根据ID与PWD生成sig
	 * 独立模式：您的后台服务器负责用户登录验证，并对登录成功的用户生成UserSig签名(需提前于云通信后台配置签名用的非对称密钥)
	 * 托管模式：获取userid以及password后直接调用pwdLogin(String, String)函数完成登录，跳过步骤（3）
	 * 游客模式：直接调用TCLoginMgr的guestLogin()函数完成登录，跳过步骤（3）
	 * <p>
	 * 3.通过鉴权返回的ID与Sig进行登录(IMLogin#imLogin(String UserId, String UserSig))
	 * <p>
	 * 4.Login回调获取相应信息后，移除回调(IMLogin#removeIMLoginCallback())避免内存泄漏
	 * <p>
	 * 5.需要退出登录时，调用接口登出(IMLogin#imLogout())
	 *******************************************************************************************************************/

	//登录回调
	private IMLoginListener mIMLoginListener;

	private TCSmsCallback mTCSmsCallback;

	/**
	 * Login回调
	 */
	public interface IMLoginListener {

		/**
		 * 登录成功
		 */
		void onSuccess();

		/**
		 * 登录失败
		 *
		 * @param code 错误码
		 * @param msg  错误信息
		 */
		void onFailure(int code, String msg);

	}

	/**
	 * 获取验证码回调
	 */
	public interface TCSmsCallback {
		void onGetVerifyCode(int reaskDuration, int expireDuration);
	}

	public void setIMLoginListener(@NonNull IMLoginListener imLoginListener) {
		this.mIMLoginListener = imLoginListener;
	}

	public void removeIMLoginListener() {
		this.mIMLoginListener = null;
		this.mTCSmsCallback = null;
	}


	/**
	 * 检查是否存在缓存，若存在则登录，反之回调onFailure
	 */
	public boolean checkCacheAndLogin() {
		if (needLogin()) {
			return false;
		} else {
			imLogin(getLastUserInfo().identifier, getUserSig(getLastUserInfo().identifier));
		}
		return true;
	}

	/**
	 * imsdk登录接口，与tls登录验证成功后调用
	 *
	 * @param identify 用户id
	 * @param userSig  用户签名（托管模式下由TLSSDK生成 独立模式下由开发者在IMSDK云通信后台确定加密秘钥）
	 */
	public void imLogin(@NonNull String identify, @NonNull String userSig) {
		LogUtil.e(TAG, "imLogin ...");
		TIMUser user = new TIMUser();
		user.setAccountType(String.valueOf(Constants.IMSDK_ACCOUNT_TYPE));
		user.setAppIdAt3rd(String.valueOf(Constants.IMSDK_APPID));
		user.setIdentifier(identify);
		//发起登录请求
		TIMManager.getInstance().login(Constants.IMSDK_APPID, user, userSig, new TIMCallBack() {
			@Override
			public void onError(int i, String s) {
				LogUtil.e(TAG, "imLogin onError i：" + i + ",info:" + s);
				if (null != mIMLoginListener)
					mIMLoginListener.onFailure(i, s);
			}

			@Override
			public void onSuccess() {
				LogUtil.e(TAG, "imLogin onSuccess");
				if (null != mIMLoginListener)
					mIMLoginListener.onSuccess();
			}
		});
	}

	/**
	 * imsdk登出
	 */
	private void imLogout() {
		TIMManager.getInstance().logout(new TIMCallBack() {
			@Override
			public void onError(int i, String s) {
				LogUtil.e(TAG, "IMLogout fail ：" + i + " msg " + s);
			}

			@Override
			public void onSuccess() {
				//sendBroadcast(new Intent(Constants.EXIT_APP));
				LogUtil.e(TAG, "IMLogout succ !");
			}
		});

	}

	private static TLSLoginHelper mTLSLoginHelper;

	//手机ID缓存
	private String mMobileId;

	//TLS短信登录回调类
	private TLSSmsLoginListener mTLSSmsLoginListener = new TLSSmsLoginListener() {

		/**
		 * 短信登录监听
		 * @param reaskDuration reaskDuration时间内不可以重新请求下发短信
		 * @param expireDuration 短信验证码失效时间
		 */
		@Override
		public void OnSmsLoginAskCodeSuccess(int reaskDuration, int expireDuration) {
			if (null != mTCSmsCallback)
				mTCSmsCallback.onGetVerifyCode(reaskDuration, expireDuration);
		}

		/**
		 * 重新请求下发短信成功
		 * @param reaskDuration reaskDuration时间内不可以重新请求下发短信
		 * @param expireDuration 短信验证码失效时间
		 */
		@Override
		public void OnSmsLoginReaskCodeSuccess(int reaskDuration, int expireDuration) {
			if (null != mTCSmsCallback)
				mTCSmsCallback.onGetVerifyCode(reaskDuration, expireDuration);
		}

		/**
		 * 短信验证通过，下一步调用登录接口TLSSmsLogin完成登录
		 */
		@Override
		public void OnSmsLoginVerifyCodeSuccess() {
			smsLogin(mMobileId);
		}

		/**
		 * TLS手机登录成功
		 * @param tlsUserInfo TLS用户信息
		 */
		@Override
		public void OnSmsLoginSuccess(TLSUserInfo tlsUserInfo) {
			imLogin(tlsUserInfo.identifier, getUserSig(tlsUserInfo.identifier));
		}

		/**
		 * 短信登录失败
		 * @param tlsErrInfo 错误信息类
		 */
		@Override
		public void OnSmsLoginFail(TLSErrInfo tlsErrInfo) {
			if (null != mIMLoginListener)
				mIMLoginListener.onFailure(tlsErrInfo.ErrCode, tlsErrInfo.Msg);
		}


		/**
		 * 网络超时
		 * @param tlsErrInfo 错误信息类
		 */
		@Override
		public void OnSmsLoginTimeout(TLSErrInfo tlsErrInfo) {
			if (null != mIMLoginListener)
				mIMLoginListener.onFailure(tlsErrInfo.ErrCode, tlsErrInfo.Msg);
		}
	};

	/**
	 * 初始化TLS SDK
	 *
	 * @param context applicationContext
	 */
	public void init(Context context) {

		mTLSLoginHelper = TLSLoginHelper.getInstance().init(context,
				Constants.IMSDK_APPID, Constants.IMSDK_ACCOUNT_TYPE, "1.0");
		mTLSLoginHelper.setTimeOut(8000);
		mTLSLoginHelper.setLocalId(2052);
		mTLSLoginHelper.setTestHost("", true);

	}

	//登录逻辑

	/**
	 * tlssdk手机验证登录
	 *
	 * @param mobile 手机号
	 */
	public void smsLogin(String mobile) {
		mTLSLoginHelper.TLSSmsLogin(mobile, mTLSSmsLoginListener);
	}

	/**
	 * tls手机验证码验证
	 *
	 * @param verifyCode 手机验证码
	 */
	public void smsLoginVerifyCode(String verifyCode) {
		mTLSLoginHelper.TLSSmsLoginVerifyCode(verifyCode, mTLSSmsLoginListener);

	}

	/**
	 * 获取验证码
	 *
	 * @param mobile 手机号（默认中国+86）
	 */
	public void smsLoginAskCode(String mobile, TCSmsCallback tcSmsCallback) {
		this.mMobileId = mobile;
		this.mTCSmsCallback = tcSmsCallback;
		mTLSLoginHelper.TLSSmsLoginAskCode(mobile, mTLSSmsLoginListener);
	}

	/**
	 * tls登出
	 */
	public void logout() {
		if (mTLSLoginHelper != null && mTLSLoginHelper.getAllUserInfo() != null) {
			mTLSLoginHelper.clearUserInfo(mTLSLoginHelper.getLastUserInfo().identifier);
		}
		imLogout();
	}

	/**
	 * 获取tlsdk最后登录用户信息
	 *
	 * @return TLSUserInfo tls用户信息
	 */
	public TLSUserInfo getLastUserInfo() {
		return mTLSLoginHelper != null ? mTLSLoginHelper.getLastUserInfo() : null;
	}

	/**
	 * 检测是否需要执行登录操作
	 *
	 * @return false不需要登录/true需要登录
	 */
	public boolean needLogin() {
		TLSUserInfo userInfo = getLastUserInfo();
		return userInfo == null || mTLSLoginHelper.needLogin(userInfo.identifier);
	}

	/**
	 * 获取用户签名
	 *
	 * @param identifier 用户id
	 * @return 用户签名
	 */
	public String getUserSig(String identifier) {
		return mTLSLoginHelper.getUserSig(identifier);
	}

	/**
	 * 重新登录逻辑
	 */
	public void reLogin() {

		TLSUserInfo userInfo = getLastUserInfo();
		if (userInfo == null)
			return;

		mTLSLoginHelper.TLSRefreshUserSig(userInfo.identifier, new TLSRefreshUserSigListener() {
			@Override
			public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {
				//tls登录后重新登录imsdk
				imLogin(tlsUserInfo.identifier, getUserSig(tlsUserInfo.identifier));
			}

			@Override
			public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {
				Log.w(TAG, "OnRefreshUserSigFail->" + tlsErrInfo.ErrCode + "|" + tlsErrInfo.Msg);
			}

			@Override
			public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {
				Log.w(TAG, "OnRefreshUserSigTimeout->" + tlsErrInfo.ErrCode + "|" + tlsErrInfo.Msg);
			}
		});

	}

}
