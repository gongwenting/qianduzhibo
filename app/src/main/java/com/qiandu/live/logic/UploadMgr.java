package com.qiandu.live.logic;

import android.content.Context;

import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.UpfinilyloadPicRequest;
import com.qiandu.live.http.request.UploadPicRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.http.response.UpfinilyResp;
import com.qiandu.live.http.response.UploadResp;
import com.qiandu.live.http.response.UploadRespp;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * @description: 图片上传类
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class UploadMgr {
	private static final String TAG = "UploadMgr";

	private final static int MAIN_CALL_BACK = 1;
	private final static int MAIN_PROCESS = 2;
	private final static int UPLOAD_AGAIN = 3;

	private Context mContext;
	private OnUploadListener mListerner;

	public UploadMgr(final Context context, OnUploadListener listener) {
		mContext = context;
		mListerner = listener;
	}

	public void uploadCover(final String path) {
		try {
			UploadPicRequest req = new UploadPicRequest(new File(path));
			LogUtil.d(TAG,"response------UP---PIC"+ (new File(path)));
			AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
				@Override
				public void onStart(int requestId) {

				}

				@Override
				public void onSuccess(int requestId, Response response) {
					LogUtil.d(TAG,"response------UP---PIC"+response);
					if (response.code == RequestComm.SUCCESS) {
						try {
							//获取网络图片路径
							UploadRespp resp = (UploadRespp) response.data;
							LogUtil.d(TAG,"resp+++++++++++++");
//							if (mListerner != null) {
							mListerner.onUploadResult(0,"http://zhibonew.zzsike.com/static/"+resp.getThumb());//"http://zhibonew.zzsike.com/static/"+
							ACache.get(mContext).put("thumb",resp.getThumb());
//							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						if (mListerner != null) {
							mListerner.onUploadResult(-1, "");//, null
						}
					}
				}

				@Override
				public void onFailure(int requestId, int httpStatus, Throwable error) {
					if (mListerner != null) {
						mListerner.onUploadResult(-1, "");//, null
					}
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void upfinily(String path,String userid) {
		try {
			UpfinilyloadPicRequest req = new UpfinilyloadPicRequest(userid,new File(path));
			LogUtil.d(TAG,"response------UP---PIC"+ (new File(path)));
			AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
				@Override
				public void onStart(int requestId) {

				}

				@Override
				public void onSuccess(int requestId, Response response) {
					LogUtil.d(TAG,"response------UP---PIC"+response);
					if (response.code == RequestComm.SUCCESS) {
						try {
							//获取网络图片路径
							UpfinilyResp resp = (UpfinilyResp) response.data;
//							if (mListerner != null) {
							mListerner.onUploadResult(0, resp.getClanLogo());//"http://zhibonew.zzsike.com/static/"+
							ACache.get(mContext).put("clan_logo",resp.getClanLogo());
							LogUtil.d("clan","resp.getClanLogo()+++++++++++++++++"+resp.getClanLogo());
//							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						if (mListerner != null) {
							mListerner.onUploadResult(-1, "");//, null
						}
					}
				}

				@Override
				public void onFailure(int requestId, int httpStatus, Throwable error) {
					if (mListerner != null) {
						mListerner.onUploadResult(-1, "");//, null
					}
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public interface OnUploadListener {
		/**
		// * @param code:0,表示成功，-1表示失败
		//		 * @param imageId
		 * @param url
		 */
		public void onUploadResult(int code, String url);//, String imageId
	}
}
