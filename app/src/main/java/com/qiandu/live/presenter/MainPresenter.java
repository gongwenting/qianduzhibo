package com.qiandu.live.presenter;

import android.text.TextUtils;

import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.presenter.ipresenter.IMainPresenter;
import com.tencent.TIMManager;

import tencent.tls.platform.TLSUserInfo;

/**
 * @description: 首页
 *
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class MainPresenter extends IMainPresenter {
    private IMainView mIMainView;

    public MainPresenter(IMainView baseView) {
        super(baseView);
        mIMainView = baseView;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void checkCacheAndLogin() {
        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
            final IMLogin tcLoginMgr = IMLogin.getInstance();
            final TLSUserInfo userInfo = IMLogin.getInstance().getLastUserInfo();
            tcLoginMgr.setIMLoginListener(new IMLogin.IMLoginListener() {
                @Override
                public void onSuccess() {
                    tcLoginMgr.removeIMLoginListener();
                    UserInfoMgr.getInstance().setUserId(userInfo.identifier, null);
                }

                @Override
                public void onFailure(int code, String msg) {
                    tcLoginMgr.removeIMLoginListener();
                }
            });
            tcLoginMgr.checkCacheAndLogin();
        }
    }
}
