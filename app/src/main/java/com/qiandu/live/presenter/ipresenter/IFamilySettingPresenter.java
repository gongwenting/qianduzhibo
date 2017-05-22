package com.qiandu.live.presenter.ipresenter;

import android.app.Activity;
import android.net.Uri;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;

/**
 * Created by admin on 2017/4/21.
 */
public abstract class IFamilySettingPresenter implements BasePresenter {
    protected BaseView fBaseView;

    public IFamilySettingPresenter(BaseView baseView) {
        fBaseView = baseView;
    }
    /**
     * 截取图片
     *
     * @param uri
     * @return
     */

    public abstract Uri cropImage(Uri uri);
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
    /**
     * 开始直播
     *
     * @param title
     * @param declaration
     */
    public abstract void dofamily(String title,String declaration);

    public interface IFamilySettingView extends BaseView {
        Activity getActivity();
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
        /**
         * 创建家族信息
         */
        void dofinily(String act, String userId, String logo, String title, String declaration);
    }
}
