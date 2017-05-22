package com.qiandu.live.base;

/**
 * @description: MVP中Presenter的基础接口
 * @author: Andruby
 * @time: 2016/9/3 16:18
 */

public interface BasePresenter {
	/**
	 * @description: Presenter的开始处理方法
	 * @author: Andruby
	 * @time: 2016/9/8 23:18
	 */
	void start();

	/**
	 * @description: 处理一些销毁工作，会在界面结束时候调用
	 * @author: Andruby
	 * @time: 2016/9/8 23:20
	 */
	void finish();
}
