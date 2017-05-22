package com.qiandu.live.http.request;

import com.qiandu.live.LiveApp;
import com.qiandu.live.http.IDontObfuscate;
import com.qiandu.live.model.UserInfoCache;
import com.google.gson.Gson;

import java.lang.reflect.Type;


/**
 * @description: 网络请求基本类
 * @author: Andruby
 * @time: 2016/11/19 18:07
 */
public abstract class IRequest extends IDontObfuscate {

	private boolean DEBUG = false;

//	public static final String HTMLURL = "http://www.qianduzhibo.com/";
//	public static final String HOST_DEBUG = "http://www.qianduzhibo.com/m/molie/";
//	public static final String HOST_PUBLIC = "http://www.qianduzhibo.com/m/molie/";

	//http://qianduzhibo.com/h5/certification.html?userid=68509
	public static final String HTMLURL = "http://qianduzhibo.com/h5/";

	public static final String HOST_DEBUG = "http://qianduzhibo.com/m/molie/";
	public static final String HOST_PUBLIC = "http://qianduzhibo.com/m/molie/";

	protected RequestParams mParams = new RequestParams();
	public int mRequestId = 0;
	protected int mDraw = 0;
	protected final static Gson mGson = new Gson();

	public IRequest() {
	}

	/**
	 * 接口请求参数
	 *
	 * @return
	 * @throws
	 * @Title:getParams
	 * @return:RequestParams
	 * @Create: 2015年11月19日 下午11:54:51
	 * @Author : zhm
	 */
	public RequestParams getParams() {
		String token = UserInfoCache.getaToken(LiveApp.getApplication());
		//login接口不应该包含token参数，也不一定，需要根据项目的具体业务逻辑实现
		if (token != null) {
			mParams.put("token", token);
		}
		return mParams;
	}

	/**
	 * http直接post数据
	 *
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getPostData
	 * @return:String
	 * @Create: 2015年11月20日 上午12:02:54
	 * @Author : zhm
	 */
	public String getPostData() {
		return null;
	}

	/**
	 * 设置接口请求唯一标识
	 *
	 * @param requestId
	 * @throws
	 * @Description:
	 * @Title:setRequestId
	 * @return:void
	 * @Create: 2015年11月19日 下午11:56:32
	 * @Author : zhm
	 */
	public void setRequestId(int requestId) {
		mRequestId = requestId;
	}

	/**
	 * 返回请求接口唯一标识
	 *
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getRequestId
	 * @return:int
	 * @Create: 2015年11月19日 下午11:56:43
	 * @Author : zhm
	 */
	public int getRequestId() {
		return mRequestId;
	}

	/**
	 * @return
	 * @throws
	 * @Description:当前接口的url地址
	 * @Title:getUrl
	 * @return:String
	 * @Create: 2015年11月19日 下午11:53:21
	 * @Author : zhm
	 */
	public abstract String getUrl();

	/**
	 * 获取解析类型
	 *
	 * @return
	 * @throws
	 * @Description:
	 * @Title:getParserType
	 * @return:Type
	 * @Create: 2015年11月20日 上午12:02:38
	 * @Author : zhm
	 */
	public abstract Type getParserType();

	/**
	 * @return
	 * @throws
	 * @Description:返回服务器接口地址
	 * @Title:getHost
	 * @return:String
	 * @Create: 2015年11月19日 下午11:54:28
	 * @Author : zhm
	 */
	protected String getHost() {
		return DEBUG ? HOST_DEBUG : HOST_PUBLIC;
	}


	@Override
	public String toString() {
		return "IRequest [DEBUG=" + DEBUG
				+ ", mParams=" + mParams + ", mRequestId=" + mRequestId
				+ ", mDraw=" + mDraw + "]";
	}

	public boolean isCache() {
		return false;
	}

	public boolean cleanCookie() {
		return false;
	}


}