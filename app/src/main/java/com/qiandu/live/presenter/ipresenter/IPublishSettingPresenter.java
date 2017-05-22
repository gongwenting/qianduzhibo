package com.qiandu.live.presenter.ipresenter;

import android.app.Activity;
import android.net.Uri;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;


/**
 * @description: 开始直播设置
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class IPublishSettingPresenter implements BasePresenter {


	protected BaseView mBaseView;

	public IPublishSettingPresenter(BaseView baseView) {
		mBaseView = baseView;
	}


	/**
	 * 检查推流权限
	 *
	 * @param activity
	 * @return
	 */
	public abstract boolean checkPublishPermission(Activity activity);

	/**
	 * 检查录制权限
	 *
	 * @return
	 */
	public abstract boolean checkScrRecordPermission();

	/**
	 * 截取图片
	 *
	 * @param uri
	 * @return
	 */
	public abstract Uri cropImage(Uri uri);

	/**
	 * 开始直播
	 *
	 * @param title
	 * @param liveType
	 * @param location
	 * @param bitrateType
	 */
	public abstract void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord);

	/**
	 * 直播定位
	 */
	public abstract void doLocation();

	/**
	 * 选择图片方式：相册、相机
	 *
	 * @param mPermission
	 * @param type
	 * @return
	 */
	public abstract Uri pickImage(boolean mPermission, int type);

	/**
	 * 上传图片
	 *
	 * @param path
	 */
	public abstract void doUploadPic(String path);


	public interface IPublishSettingView extends BaseView {

		Activity getActivity();

		/**
		 * 定位成功
		 *
		 * @param location
		 */
		void doLocationSuccess(String location);

		/**
		 * 定位失败
		 */
		void doLocationFailed();

		/**
		 * 上传成功
		 *
		 * @param url
		 */
		void doUploadSuceess(String url);

		/**
		 * 图片上传失败
		 *
		 */
		void doUploadFailed();

		/**
		 * 结束页面
		 */
		void finishActivity();

	}
}
