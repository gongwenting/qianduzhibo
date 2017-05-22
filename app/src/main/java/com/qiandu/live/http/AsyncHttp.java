package com.qiandu.live.http;

import android.content.Context;

import com.qiandu.live.LiveApp;
import com.qiandu.live.http.request.IRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.utils.LogUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @description: 网络管理类
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class AsyncHttp {
	private static final String TAG = AsyncHttp.class.getSimpleName();
	private static Gson mGson = null;
	private static AsyncHttp mInstance;
	private CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(LiveApp.getApplication()));
	private OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
			.readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
			.cookieJar(cookieJar)
			.build();

	private AsyncHttp() {
		if (mGson == null) {
			mGson = new GsonBuilder()
					.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//					.registerTypeAdapter(RowInfo.class, new RowInfoDeserializer())
					.create();
		}
		OkHttpUtils.initClient(okHttpClient);
	}

	public static AsyncHttp instance() {
		if (mInstance == null) {
			synchronized (OkHttpUtils.class) {
				if (mInstance == null) {
					mInstance = new AsyncHttp();
				}
			}
		}
		return mInstance;
	}


	public void get(IRequest request, IHttpListener listener) {
		LogUtil.e(TAG, "post: url=" + request.getUrl());
		if (request != null) {
			request.getParams().getGetBuilder().url(request.getUrl()).id(request.getRequestId()).
					build().execute(new HttpResponse(listener, request.getParserType()));
		} else {
			throw new RuntimeException("Request param is null");
		}
	}

	public void post(IRequest request, IHttpListener listener) {
		LogUtil.e(TAG, "post: url=" + request.getUrl());
		if (request != null) {
			request.getParams().getPostFormBuilder().url(request.getUrl()).id(request.getRequestId()).
					build().execute(new HttpResponse(listener, request.getParserType()));
		} else {
			throw new RuntimeException("Request param is null");
		}
	}

//	public void postJson(IRequest request, IHttpListener listener) {
//		LogUtil.e(TAG, "post: url=" + request.getUrl());
//		if (request.getParams().has("action")) {
//			LogUtil.e(TAG, "action：" + request.getParams().getUrlParams("action"));
//		}
//		if (request != null) {
//			if(request.cleanCookie()){
//				cookieJar.getCookieStore().removeAll();
//			}
//			request.getParams().getPostJsonBuilder().url(request.getUrl()).id(request.getRequestId()).
//					build().execute(new HttpResponse(listener, request.getParserType()));
//		} else {
//			throw new RuntimeException("Request param is null");
//		}
//	}

	private void addHeader(Context context) {
	/*	String token = PreManager.instance(context).getToken();
		if (token != null) {
			mHttpClient.addHeader("token", token);
		}*/
	}

	public void cancelRequest(String tag) {
		if (tag != null) {
			OkHttpUtils.getInstance().cancelTag(tag);
		}
	}

	/**
	 * 请求状态的接口
	 */
	public interface IHttpListener {
		void onStart(int requestId);

		void onSuccess(int requestId, Response response);

		void onFailure(int requestId, int httpStatus, Throwable error);

	}

	abstract class StringCallback extends Callback<String> {
		@Override
		public String parseNetworkResponse(okhttp3.Response response, int id) throws IOException {
			return response.body().string();
		}
	}

	/**
	 * 网络请求返回接口
	 */
	class HttpResponse extends Callback<Response> {
		private IHttpListener mHttpListener;
		private Type mParserType;

		public HttpResponse(IHttpListener httpListener, Type parserType) {
			mHttpListener = httpListener;
			mParserType = parserType;
		}

		@Override
		public void onBefore(Request request, int id) {
			if (mHttpListener != null) {
				mHttpListener.onStart(id);
			}
		}

		@Override
		public Response parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
			Response responseData = null;
			if (mHttpListener != null && response != null) {
				if (response.isSuccessful()) {
					String content = response.body().string();
					if (content != null) {
						try {
							LogUtil.e(TAG, "onSuccess: " + content);
							responseData = mGson.fromJson(content,
									mParserType);
						} catch (JsonSyntaxException e) {
//							mHttpListener.onFailure(id, response.code(), e);
							onError(null, e, id);
						}
					}
				} else {
//					mHttpListener.onFailure(id, response.code(), new Exception("net error"));
					onError(null, new Exception("net error"), id);
				}
			}
			return responseData;
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			LogUtil.e(TAG, "onError:" + id, e);
			if (mHttpListener != null) {
				if (!call.isCanceled()) {
					mHttpListener.onFailure(id, 0, e);
				}
			}
		}

		@Override
		public void onResponse(Response response, int id) {
			if (mHttpListener != null) {
				mHttpListener.onSuccess(id, response);
			}
		}
	}


}
