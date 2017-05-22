package com.qiandu.live.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.qiandu.live.R;
import com.qiandu.live.logic.IUserInfoMgrListener;
import com.qiandu.live.logic.UploadMgr;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.presenter.ipresenter.IFamilySettingPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;


import java.io.File;

/**
 * Created by admin on 2017/4/21.
 */
public class FamilySettingPresenter extends IFamilySettingPresenter {
    IFamilySettingPresenter.IFamilySettingView mFamilySettingView;
    public static final int PICK_IMAGE_CAMERA = 100;
    public static final int PICK_IMAGE_LOCAL = 200;
    public static final int CROP_CHOOSE = 10;
    private boolean mUploading = false;
    public FamilySettingPresenter(IFamilySettingView fbaseView) {
        super(fbaseView);
        mFamilySettingView=fbaseView;
    }

    @Override
    public Uri cropImage(Uri uri) {
        Uri cropUri = createCoverUri("_crop");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 550);
        intent.putExtra("aspectY", 550);
        intent.putExtra("outputX", 550);
        intent.putExtra("outputY", 550);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mFamilySettingView.getActivity().startActivityForResult(intent, CROP_CHOOSE);
        return cropUri;
    }
    public Uri createCoverUri(String type) {
        String filename = UserInfoMgr.getInstance().getUserId() + type + ".jpg";
        String path = Environment.getExternalStorageDirectory() + "/qiandu_jiazu";

        File outputImage = new File(path, filename);
        if (ContextCompat.checkSelfPermission(mFamilySettingView.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mFamilySettingView.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSION_REQ_CODE);
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
            mFamilySettingView.showMsg("生成封面失败");
        }
        return Uri.fromFile(outputImage);
    }
    @Override
    public Uri pickImage(boolean mPermission, int type) {
        Uri fileUri = null;
        if (!mPermission) {
            mFamilySettingView.showMsg(R.string.tip_no_permission);
            return null;
        }
        switch (type) {
            case PICK_IMAGE_CAMERA:
                fileUri = createCoverUri("_family");
                Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                mFamilySettingView.getActivity().startActivityForResult(intent_photo, PICK_IMAGE_CAMERA);
                break;
            case PICK_IMAGE_LOCAL:
                fileUri = createCoverUri("_selectfamily");
                Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
                intent_album.setType("image/*");
                mFamilySettingView.getActivity().startActivityForResult(intent_album, PICK_IMAGE_LOCAL);
                break;

        }
        return fileUri;
    }

    @Override
    public void doUploadPic(String path) {
        mUploading = true;
        new UploadMgr(mFamilySettingView.getContext(), new UploadMgr.OnUploadListener() {

            @Override
            public void onUploadResult(int code,String url) { //String idint code,

                    UserInfoMgr.getInstance().setUserCoverPic(url, new IUserInfoMgrListener() {
                        @Override
                        public void OnQueryUserInfo(int error, String errorMsg) {
                        }

                        @Override
                        public void OnSetUserInfo(int error, String errorMsg) {

                        }
                    });
                    mFamilySettingView.showMsg("上传封面成功");
                    mFamilySettingView.doUploadSuceess(url);


                mUploading = false;
            }
        }).upfinily(path, ACache.get(mFamilySettingView.getContext()).getAsString("user_id"));//UserInfoMgr.getInstance().getUserId()  获取参数
        ACache.get(mFamilySettingView.getContext()).put("clan",path);
        LogUtil.e("clan",""+path);
    }

    @Override
    public void dofamily(String title, String declaration) {
        //trim避免空格字符串
        if (TextUtils.isEmpty(title)) {
            mFamilySettingView.showMsg("请输入非空直播标题");
        } else if (OtherUtils.getCharacterNum(title) > Constants.TV_TITLE_MAX_LEN) {
            mFamilySettingView.showMsg("直播标题过长 ,最大长度为" + Constants.TV_TITLE_MAX_LEN / 2);
        }
        if (TextUtils.isEmpty(declaration)){
            mFamilySettingView.showMsg("请输入非空家族宣言");
        }else if (OtherUtils.getCharacterNum(title) > Constants.TV_TITLE_MAX_LEN) {
            mFamilySettingView.showMsg("直播标题过长 ,最大长度为" + Constants.NICKNAME_MAX_LEN);
        }
    }
    @Override
    public void start() {

    }

    @Override
    public void finish() {
mFamilySettingView.finishActivity();
    }
}
