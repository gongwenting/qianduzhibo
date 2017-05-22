package com.qiandu.live.base;

import android.content.Context;

/**
 * @description: MVP中View的基本接口
 * @author: Andruby
 * @time: 2016/9/3 16:19
 */
public interface BaseView<T> {


	/**
	 * @description: 网络加载或耗时加载时界面显示
	 * @author: Andruby
	 * @time: 2016/9/8 23:08
	 */
	void showLoading();

	/**
	 * @description: 网络加载或耗时加载完成时界面显示
	 * @author: Andruby
	 * @time: 2016/9/8 23:08
	 */
	void dismissLoading();

	/**
	 * @description: 消息提示，如Toast，Dialog等
	 * @author: Andruby
	 * @time: 2016/9/8 23:08
	 * @param:  msg 提示内容
	 */
	void showMsg(String msg);
	void showMsg(int msg);

	/**
	 * @description: 获取Context
	 * @author: Andruby
	 * @time: 2016/9/8 23:08
	 */
	Context getContext();


//	void onNeber(String ooo);
}
