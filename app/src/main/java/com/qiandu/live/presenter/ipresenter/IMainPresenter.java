package com.qiandu.live.presenter.ipresenter;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;


/**
 * @description: 主页面管理
 *
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class IMainPresenter implements BasePresenter {
    protected BaseView mBaseView;

    public IMainPresenter(BaseView baseView) {
        mBaseView = baseView;
    }

    /**
     * 监测缓存和登陆
     */
    protected abstract void checkCacheAndLogin();

    public interface IMainView extends BaseView {
    }
}