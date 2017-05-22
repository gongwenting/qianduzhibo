package com.qiandu.live.ui.listload;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qiandu.live.R;

/**
 * @description: 列表加载动画
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class ProgressBarHelper {
	public static final int MODE_LOADING_IMAGE_WITH_ANIM = 0;
	public static final int MODE_LOADING_PROGRESS = 1;
	private ImageView image;
	private int mNetErrorTipImageId = R.drawable.net_error_tip;
	private int mNoDateTipImageResId = R.drawable.no_data_tip;
	private int mMode = MODE_LOADING_IMAGE_WITH_ANIM;

	public static final int STATE_LOADING = 0xf1;
	public static final int STATE_ERROR = 0xf2;
	public static final int STATE_EMPTY = 0xf3;
	public static final int STATE_FINISH = 0xf4;

	public interface ProgressBarClickListener {
		void clickRefresh();
	}

	public interface OnLoadingListener {
		void onLoading(int state);
	}

	public View loading;
	TextView tvTipText;
	ProgressBar progressBar;
	ProgressBarClickListener pcl;
	public boolean isLoading = false;
	public Activity context;
	private OnLoadingListener mLoadingListener;

	public void setNoDateTipImageResId(int noDateTipImageResId) {
		mNoDateTipImageResId = noDateTipImageResId;
	}

	private boolean isFromH5;

	public boolean isFromH5() {
		return isFromH5;
	}

	public void setFromH5(boolean isFromH5) {
		this.isFromH5 = isFromH5;
	}

	public void setProgressBarClickListener(ProgressBarClickListener pcl) {
		this.pcl = pcl;
	}

	public ProgressBarHelper(Activity context, View inView) {
		this.context = context;
		if (inView != null) {
			loading = inView;
		} else {
			if (context == null)
				return;
			loading = context.findViewById(R.id.ll_data_loading);
		}
		if (loading == null)
			return;
		tvTipText = (TextView) loading.findViewById(R.id.loading_tip_txt);
		progressBar = (ProgressBar) loading
				.findViewById(R.id.loading_progress_bar);
		image = (ImageView) loading.findViewById(R.id.loading_image);
		setLoading();
	}

	public ProgressBarHelper(Activity context, View inView, int mode) {
		this.context = context;
		this.mMode = mode;
		if (inView != null) {
			loading = inView;
		} else {
			if (context == null)
				return;
			loading = context.findViewById(R.id.ll_data_loading);
		}
		if (loading == null)
			return;
		tvTipText = (TextView) loading.findViewById(R.id.loading_tip_txt);
		progressBar = (ProgressBar) loading
				.findViewById(R.id.loading_progress_bar);
		image = (ImageView) loading.findViewById(R.id.loading_image);
		setLoading();
	}

	public ProgressBarHelper(Activity context, View inView, String str) {
		this.context = context;
		if (inView != null) {
			loading = inView;
		} else {
			if (context == null)
				return;
			loading = context.findViewById(R.id.ll_data_loading);
		}
		if (loading == null)
			return;
		tvTipText = (TextView) loading.findViewById(R.id.loading_tip_txt);
		image = (ImageView) loading.findViewById(R.id.loading_image);
		if (image.getDrawable() instanceof AnimationDrawable)
			((AnimationDrawable) image.getDrawable()).start();
		progressBar = (ProgressBar) loading
				.findViewById(R.id.loading_progress_bar);

		// if(str.equals("GONE")){
		// btn_camera.setVisibility(View.GONE);
		// }

	}

	public void showNetError() {
		context.runOnUiThread(new Runnable() {
			public void run() {
				isLoading = false;
				if (tvTipText != null) {
					setFailure();
				}
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
					if (loading != null) {
						loading.setVisibility(View.VISIBLE);
						loading.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (pcl != null
										&& !progressBar.isShown()
										&& progressBar.getVisibility() == View.GONE) {
									if (isFromH5()) {
										pcl.clickRefresh();
										setLoading();
									} else {
										pcl.clickRefresh();
										setLoading();
									}
								}
							}
						});
					}

				}
			}
		});
	}

	public void setLoadingListener(OnLoadingListener mLoadingListener) {
		this.mLoadingListener = mLoadingListener;
	}

	// 暂无数据
	public void showNoData() {
		context.runOnUiThread(new Runnable() {
			public void run() {
				isLoading = false;
				if (tvTipText != null) {
					setNoData();
				}
				if (progressBar != null)
					progressBar.setVisibility(View.GONE);
				if (loading != null) {
					loading.setVisibility(View.VISIBLE);
					loading.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (pcl != null && !progressBar.isShown()
									&& progressBar.getVisibility() == View.GONE) {
								pcl.clickRefresh();
								setLoading();
							}
						}
					});
				}
			}
		});
	}

	public void setFailureImageRes(int resId) {
		mNetErrorTipImageId = resId;
	}

	public void setNoDataImageRes(int resId) {
		mNoDateTipImageResId = resId;
	}


	private void setLoading() {
		isLoading = true;
		if (tvTipText != null) {
			tvTipText.setText("");
		}
		if (mMode == MODE_LOADING_PROGRESS) {
			progressBar.setVisibility(View.VISIBLE);
			image.setVisibility(View.GONE);
		} else {
			if (image != null) {
				image.setImageDrawable(context.getResources().getDrawable(
						R.drawable.common_loading_anim));
			}
			if (image != null && image.getDrawable() instanceof AnimationDrawable)
				((AnimationDrawable) image.getDrawable()).start();
		}
		if (loading != null) {
			loading.setEnabled(false);
			loading.setVisibility(View.VISIBLE);
		}
		if (mLoadingListener != null) {
			mLoadingListener.onLoading(STATE_LOADING);
		}
	}

	Runnable mOnFailureListener;

	public void setOnFailureListener(Runnable listener) {
		mOnFailureListener = listener;
	}

	private void setFailure() {
		isLoading = false;
		// tvTipText.setText(R.string.srploaded_nocontent);
		image.setVisibility(View.VISIBLE);
		image.setImageDrawable(context.getResources().getDrawable(
				mNetErrorTipImageId));
		loading.setEnabled(true);
		loading.setVisibility(View.VISIBLE);
		if (mOnFailureListener != null) {
			mOnFailureListener.run();
		}
		if (mLoadingListener != null) {
			mLoadingListener.onLoading(STATE_ERROR);
		}
	}

	Runnable mOnNoDateListener;

	public void setOoNoDateListener(Runnable listener) {
		mOnNoDateListener = listener;
	}

	private void setNoData() {
		isLoading = false;
		image.setVisibility(View.VISIBLE);
		// tvTipText.setText(R.string.nocontent);
		image.setImageDrawable(context.getResources().getDrawable(
				mNoDateTipImageResId));
		loading.setEnabled(true);
		loading.setVisibility(View.VISIBLE);
		if (mOnNoDateListener != null) {
			mOnNoDateListener.run();
		}
		if (mLoadingListener != null) {
			mLoadingListener.onLoading(STATE_EMPTY);
		}
	}

	public void goneLoading() {

		if (loading != null) {
			//			loading.setEnabled(false);
			context.runOnUiThread(new Runnable() {
				public void run() {
					isLoading = false;
					loading.setVisibility(View.GONE);
					if (mLoadingListener != null) {
						mLoadingListener.onLoading(STATE_FINISH);
					}
				}
			});

		}
	}

	public void showLoading() {
		if (isLoading) {
			return;
		}
		context.runOnUiThread(new Runnable() {
			public void run() {
				/*
                 * if (tvTipText != null) tvTipText.setText(R.string.loading_progress_hint);
				 * if (loading != null) { loading.setVisibility(View.VISIBLE);
				 * if (progressBar != null && !progressBar.isShown())
				 * progressBar.setVisibility(View.VISIBLE); }
				 */
				setLoading();
			}

		});
	}

	public void goneLoadingUI() {
		isLoading = false;
		if (loading != null) {
			loading.setVisibility(View.GONE);
		}
		if (mLoadingListener != null) {
			mLoadingListener.onLoading(STATE_FINISH);
		}
	}

	public void showLoadingUI() {
		if (isLoading) {
			return;
		}
		isLoading = true;
		setLoading();
	}

	public boolean isAllGone() {
		if (loading.getVisibility() == View.GONE)
			return true;
		return false;
	}

	public View getLoadingView() {
		return loading;
	}
}
