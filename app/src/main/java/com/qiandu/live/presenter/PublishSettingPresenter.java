package com.qiandu.live.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.qiandu.live.R;
import com.qiandu.live.activity.LivePublisherActivity;
import com.qiandu.live.logic.IUserInfoMgrListener;
import com.qiandu.live.logic.LocationMgr;
import com.qiandu.live.logic.UploadMgr;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.presenter.ipresenter.IPublishSettingPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 设置
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class PublishSettingPresenter extends IPublishSettingPresenter {

	IPublishSettingView mPublishSettingView;
	public static final int PICK_IMAGE_CAMERA = 100;
	public static final int PICK_IMAGE_LOCAL = 200;
	public static final int CROP_CHOOSE = 10;
	private boolean mUploading = false;

	public PublishSettingPresenter(IPublishSettingView baseView) {
		super(baseView);
		mPublishSettingView = baseView;
	}

	@Override
	public boolean checkPublishPermission(Activity activity) {
		if (Build.VERSION.SDK_INT >= 23) {
			List<String> permissions = new ArrayList<>();
			if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
			if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) {
				permissions.add(Manifest.permission.CAMERA);
			}
			if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)) {
				permissions.add(Manifest.permission.READ_PHONE_STATE);
			}
			if (permissions.size() != 0) {
				ActivityCompat.requestPermissions(activity,
						permissions.toArray(new String[0]),
						Constants.WRITE_PERMISSION_REQ_CODE);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkScrRecordPermission() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	@Override
	public Uri cropImage(Uri uri) {
		Uri cropUri = createCoverUri("_crop");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 750);
		intent.putExtra("aspectY", 550);
		intent.putExtra("outputX", 750);
		intent.putExtra("outputY", 550);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		mPublishSettingView.getActivity().startActivityForResult(intent, CROP_CHOOSE);
		return cropUri;
	}

	public Uri createCoverUri(String type) {
		String filename = UserInfoMgr.getInstance().getUserId() + type + ".jpg";
		String path = Environment.getExternalStorageDirectory() + "/cniao_live";

		File outputImage = new File(path, filename);
		if (ContextCompat.checkSelfPermission(mPublishSettingView.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(mPublishSettingView.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSION_REQ_CODE);
			return null;
		}
		try {
			File pathFile = new File(path);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			if (outputImage.exists()) {
				outputImage.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mPublishSettingView.showMsg("生成封面失败");
		}
		return Uri.fromFile(outputImage);
	}


	@Override
	public void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord) {
		//trim避免空格字符串
		if (TextUtils.isEmpty(title)) {
			mPublishSettingView.showMsg("请输入非空直播标题");
		} else if (OtherUtils.getCharacterNum(title) > Constants.TV_TITLE_MAX_LEN) {
			mPublishSettingView.showMsg("直播标题过长 ,最大长度为" + Constants.TV_TITLE_MAX_LEN / 2);
		} else if (mUploading) {
			mPublishSettingView.showMsg(R.string.publish_wait_uploading);
		} else if (!OtherUtils.isNetworkAvailable(mPublishSettingView.getContext())) {
			mPublishSettingView.showMsg("当前网络环境不能发布直播");
		} else {
			if (liveType == Constants.RECORD_TYPE_SCREEN) {
				//录屏直播
				mBaseView.showMsg("screen live");
			} else {
				//摄像头直播
//				mBaseView.showMsg("camera live");
				LivePublisherActivity.invoke(mPublishSettingView.getActivity(), title, location, isRecord, bitrateType);
			}

		}
	}

	public LocationMgr.OnLocationListener getLocationListener() {
		return mOnLocationListener;
	}


	private LocationMgr.OnLocationListener mOnLocationListener = new LocationMgr.OnLocationListener() {

		@Override
		public void onLocationChanged(int code, double lat1, double long1, String location) {
			if (0 == code) {
				mPublishSettingView.doLocationSuccess(location);
				UserInfoMgr.getInstance().setLocation(location, lat1, long1, new IUserInfoMgrListener() {
					@Override
					public void OnQueryUserInfo(int error, String errorMsg) {
					}

					@Override
					public void OnSetUserInfo(int error, String errorMsg) {
						if (0 != error) {
							mPublishSettingView.showMsg("设置位置失败" + errorMsg);
						}
					}
				});
			} else {
				mPublishSettingView.doLocationFailed();
			}
		}
	};

	@Override
	public void doLocation() {
		if (LocationMgr.checkLocationPermission(mPublishSettingView.getActivity())) {
			boolean success = LocationMgr.getMyLocation(mPublishSettingView.getActivity(), mOnLocationListener);
			if (!success) {
				mPublishSettingView.doLocationFailed();
			}
		}
	}

	@Override
	public Uri pickImage(boolean mPermission, int type) {
		Uri fileUri = null;
		if (!mPermission) {
			mPublishSettingView.showMsg(R.string.tip_no_permission);
			return null;
		}
		switch (type) {
			case PICK_IMAGE_CAMERA:
				fileUri = createCoverUri("");
				Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				mPublishSettingView.getActivity().startActivityForResult(intent_photo, PICK_IMAGE_CAMERA);
				break;
			case PICK_IMAGE_LOCAL:
				fileUri = createCoverUri("_select");
				Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
				intent_album.setType("image/*");
				mPublishSettingView.getActivity().startActivityForResult(intent_album, PICK_IMAGE_LOCAL);
				break;

		}
		return fileUri;
	}

	@Override
	public void doUploadPic(String path) {
		mUploading = true;
		new UploadMgr(mPublishSettingView.getContext(), new UploadMgr.OnUploadListener() {

			@Override
			public void onUploadResult(int code,String url) {
				if (0 == code) {
					UserInfoMgr.getInstance().setUserCoverPic(url, new IUserInfoMgrListener() {
						@Override
						public void OnQueryUserInfo(int error, String errorMsg) {
						}

						@Override
						public void OnSetUserInfo(int error, String errorMsg) {

						}
					});
					mPublishSettingView.showMsg("上传封面成功");
					mPublishSettingView.doUploadSuceess(url);

				} else {
					mPublishSettingView.showMsg("上传封面失败，错误码 " + code);
				}
				mUploading = false;
			}
		}).uploadCover(path);

		ACache.get(mPublishSettingView.getContext()).put("thumb",path);
		LogUtil.e("thumb",""+path);
	}

	@Override
	public void start() {

	}

	@Override
	public void finish() {
		mPublishSettingView.finishActivity();
	}
}
