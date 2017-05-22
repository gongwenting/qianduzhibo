package com.qiandu.live.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.Createfamily;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.presenter.FamilySettingPresenter;
import com.qiandu.live.presenter.ipresenter.IFamilySettingPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.File;

public class BinderJiazuActivity extends IMBaseActivity implements View.OnClickListener,
        IFamilySettingPresenter.IFamilySettingView, RadioGroup.OnCheckedChangeListener {
    public static final String TAG=BinderJiazuActivity.class.getSimpleName();
    private ImageView mAvatar;
    private Dialog mPickDialog;
    private ImageView go;
    private EditText fainyname;
    private EditText xuanyanname;
    private RelativeLayout btn_houtui;
    //相册
    private boolean mPermission = true;
    private FamilySettingPresenter familySettingPresenter;
    private Uri finalUri, cropUri;
    private String onNuber;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binder_jiazu;
    }

    @Override
    protected void initView() {
        mAvatar = (ImageView) findViewById(R.id.imgview_cover);
        go= (ImageView) findViewById(R.id.btn_go);
        fainyname= (EditText) findViewById(R.id.ed_jiazuname);
        xuanyanname= (EditText) findViewById(R.id.ed_jiazuxuanyan);
        btn_houtui= (RelativeLayout) findViewById(R.id.btn_houtui);
    }

    @Override
    protected void initData() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        go.setOnClickListener(this);
        fainyname.setOnClickListener(this);
        xuanyanname.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        btn_houtui.setOnClickListener(this);
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_pick, null);
        mPickDialog = new AlertDialog.Builder(this).setView(view).create();
        familySettingPresenter = new FamilySettingPresenter(this);
        mAvatar.setImageResource(R.drawable.publish_background);
    }

    @Override
    protected void setListener() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.imgview_cover:
                mPickDialog.show();
                break;
            case R.id.picture_dialog_pick:
                finalUri = familySettingPresenter.pickImage(mPermission, FamilySettingPresenter.PICK_IMAGE_LOCAL);
                mPickDialog.dismiss();
                break;
            case R.id.camera_dialog_pick:
                finalUri = familySettingPresenter.pickImage(mPermission, FamilySettingPresenter.PICK_IMAGE_CAMERA);
                mPickDialog.dismiss();
                break;
            case R.id.btn_go:
                familySettingPresenter.dofamily(fainyname.getText().toString().trim(),xuanyanname.getText().toString().trim());
                dofinily("create", ACache.get(this).getAsString("user_id"),ACache.get(this).getAsString("clan_logo"),
                        fainyname.getText().toString().trim(),xuanyanname.getText().toString().trim());
                LogUtil.d("LOOK Create family","user_id++++"+ACache.get(this).getAsString("user_id")
                        +"clan+++++++"+ACache.get(this).getAsString("clan_logo")+"clan_nam++++++"+fainyname.getText().toString().trim()
                        +"clan_desc++++++"+xuanyanname.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FamilySettingPresenter.PICK_IMAGE_CAMERA:
                    cropUri = familySettingPresenter.cropImage(finalUri);
                    LogUtil.e("LOOKTHIS","");
                    break;
                case FamilySettingPresenter.PICK_IMAGE_LOCAL:
                    String path = OtherUtils.getPath(this, data.getData());
                    if (null != path) {
                        Log.d(TAG, "cropImage->path:" + path);
                        File file = new File(path);
                        cropUri = familySettingPresenter.cropImage(Uri.fromFile(file));
                    }
                    break;
                case FamilySettingPresenter.CROP_CHOOSE:
                    familySettingPresenter.doUploadPic(cropUri.getPath());

                    break;

            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void doUploadSuceess(String url) {

        Glide.with(this).load(url).into(mAvatar);
    }

    @Override
    public void doUploadFailed() {

        showMsg(getString(R.string.live_cover_upload_failed));

    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void dofinily(String act, String userId, String logo, String title, String declaration) {
        Createfamily createfamily=new Createfamily(act,userId,logo,title,declaration);
        AsyncHttp.instance().post(createfamily, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    Toast.makeText(BinderJiazuActivity.this,"创建家族成功，请等待审核",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BinderJiazuActivity.this,"您已经提交过申请,请不要重复提交",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showMsg(int msg) {
        ToastUtils.showShort(this, getString(msg));
    }

    @Override
    public Context getContext() {
        return this;
    }



    public static void invoke(Context context) {
        Intent intent = new Intent(context, PublishSettingActivity.class);
        context.startActivity(intent);
    }
}
