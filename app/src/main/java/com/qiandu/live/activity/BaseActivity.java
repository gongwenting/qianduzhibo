package com.qiandu.live.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


/**
 * @Description:界面的抽取，包括标题、网络、图片加载
 * @author: Andruby
 * @date: 2016年7月7日 下午4:46:44
 *
 *
 *
 */
public abstract class BaseActivity extends FragmentActivity {
	protected Context mContext;
	protected Handler mHandler = new Handler();

	public ActionBar actionBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBeforeLayout();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext=this;
		if (getLayoutId() != 0) {
			setContentView(getLayoutId());
		}
//		else {
//			setContentView(getLayoutId());
//		}

		initView();
		initData();
		setListener();
	}

	protected void setBeforeLayout(){}
	/**
	 * 返回当前界面布局文件
	 */
	protected abstract int getLayoutId();

	/**
	 * 此方法描述的是： 初始化所有view
	 */
	protected abstract void initView();

	/**
	 * 此方法描述的是： 初始化所有数据的方法
	 */
	protected abstract void initData();

	/**
	 * 此方法描述的是： 设置所有事件监听
	 */
	protected abstract void setListener();

	@Override
	protected void onResume() {
		super.onResume();
	}


	public <T extends View> T obtainView(int resId) {
		return (T) findViewById(resId);
	}

	/**
	 * 显示toast
	 *
	 * @param resId
	 */
	public void showToast(final int resId) {
		showToast(getString(resId));
	}

	/**
	 * 显示toast
	 *
	 * @param resStr
	 * @return Toast对象，便于控制toast的显示与关闭
	 */
	public void showToast(final String resStr) {

		if (TextUtils.isEmpty(resStr)) {
			return ;
		}
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(BaseActivity.this, resStr,
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
