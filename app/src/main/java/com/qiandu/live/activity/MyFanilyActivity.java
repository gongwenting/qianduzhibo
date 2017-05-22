package com.qiandu.live.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.Createfamily;
import com.qiandu.live.http.request.FanilyDetailRequest;
import com.qiandu.live.http.request.KeepFailyRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.FanilyDetailResp;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.presenter.FamilySettingPresenter;
import com.qiandu.live.presenter.ipresenter.IFamilySettingPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.utils.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.File;

public class MyFanilyActivity extends IMBaseActivity implements View.OnClickListener,
        IFamilySettingPresenter.IFamilySettingView, RadioGroup.OnCheckedChangeListener  {
    private ImageView cover;
    private EditText jiazuname;
    private TextView zuzhangname;
    private TextView renshu;
    private EditText jiazuxuanyan;
    private ImageView btngo;
    private int zuzhang;
    private ImageView chuanyuan;
    private TextView chenyuan;
    private RelativeLayout btn_houtui;
    //弹出框
    private Dialog mPickDialog;
    //相册
    private boolean mPermission = true;
    private FamilySettingPresenter familySettingPresenter;
    private Uri finalUri, cropUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fanily);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        intoData();
        infoData();
        detail(ACache.get(this).getAsString("user_id"));

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }


    private void detail(String userid) {
        FanilyDetailRequest fanilyrequ=new FanilyDetailRequest(userid);
        AsyncHttp.instance().post(fanilyrequ, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code== RequestComm.SUCCESS){
                    FanilyDetailResp fa=(FanilyDetailResp)response.data;
                    LogUtil.d("JIAZU",""+"============"+fa.getClanDesc()+"============"+fa.getClanLead()+"============"
                            +fa.getClanName()+"============"+fa.getClanId()+"============"+fa.getClanId()+"+++++++"
                            +fa.getLeadId()+"============"+ fa.getIsLeader());
                    ACache.get(MyFanilyActivity.this).put("is_leader",fa.getIsLeader());
                    Glide.with(MyFanilyActivity.this).load(fa.getClanLogo()).into(cover);
                    jiazuname.setText(fa.getClanName());
                    zuzhangname.setText(fa.getClanLead());
                    String shuzi=String.valueOf(fa.getCount());
                    renshu.setText(shuzi);
                    jiazuxuanyan.setText(fa.getClanDesc());
                    zuzhang=fa.getIsLeader();
                    if (fa.getIsLeader()==0){
                        btngo.setImageResource(R.mipmap.btn_lan);
                        chenyuan.setText("成员列表");
                        jiazuname.setFocusable(false);
                        jiazuxuanyan.setFocusable(false);
                        jiazuname.setFocusableInTouchMode(false);
                        jiazuxuanyan.setFocusableInTouchMode(false);
                    }else {
                        btngo.setImageResource(R.mipmap.btn_hong);
                        chenyuan.setText("家族成员");
                        jiazuname.setFocusable(true);
                        jiazuxuanyan.setFocusable(true);
                        jiazuname.setFocusableInTouchMode(true);
                        jiazuxuanyan.setFocusableInTouchMode(true);
                    }

                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    private void infoData() {

        cover.setOnClickListener(this);
        jiazuname.setOnClickListener(this);
        zuzhangname.setOnClickListener(this);
        renshu.setOnClickListener(this);
        jiazuxuanyan.setOnClickListener(this);
        chuanyuan.setOnClickListener(this);
        btngo.setOnClickListener(this);
        btn_houtui.setOnClickListener(this);
    }

    private void intoData() {

        familySettingPresenter = new FamilySettingPresenter(this);
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_pick, null);
        mPickDialog = new AlertDialog.Builder(this).setView(view).create();
        chenyuan= (TextView) findViewById(R.id.text_chengyuan);
        cover= (ImageView) findViewById(R.id.imgview_cover);
        jiazuname= (EditText) findViewById(R.id.ed_jiazuname);
        zuzhangname= (TextView) findViewById(R.id.text_zuzhangname);
        renshu= (TextView) findViewById(R.id.text_zuzhang);
        jiazuxuanyan= (EditText) findViewById(R.id.ed_jiazuxuanyan);
        btngo= (ImageView) findViewById(R.id.btn_go);
        chuanyuan= (ImageView) findViewById(R.id.img_chengyuan);
        btn_houtui= (RelativeLayout) findViewById(R.id.btn_houtui);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgview_cover:
                if (zuzhang==1){
                    mPickDialog.show();
                }else {
                    Toast.makeText(this,"您不是家族族长不能编辑头像....",Toast.LENGTH_SHORT).show();
                }

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
                if(zuzhang==0){
                    Toast.makeText(this,"您不是家族族长，不能编辑资料",Toast.LENGTH_SHORT).show();
                }else {
                    keep(ACache.get(this).getAsString("user_id"),ACache.get(this).getAsString("clan"),jiazuname.getText().toString().trim(),jiazuxuanyan.getText().toString().trim());
                    LogUtil.d("KEEP","user_id++++++++++++++++++"+ACache.get(this).getAsString("user_id")
                            +"clan++++++++++++++++++"+ACache.get(this).getAsString("clan")
                            +"jiazuname++++++++++++++++++"+jiazuname.getText().toString().trim()
                            +"jiazuxuanyan++++++++++++++++++"+jiazuxuanyan.getText().toString().trim());
                    Toast.makeText(MyFanilyActivity.this,"保存成功.....",Toast.LENGTH_SHORT);
                    finish();
                }
                break;
            case  R.id.btn_houtui:
                finish();
                break;
            case R.id.img_chengyuan:
                startActivity(new Intent(this,ChenyuanActivity.class));
                break;
        }
    }
    private void keep(String userId ,String clanLogo,String clanName,String clanDesc) {
        KeepFailyRequest keep= new KeepFailyRequest(userId,clanLogo,clanName,clanDesc);
        AsyncHttp.instance().post(keep,null );
        {
        }}

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

        Glide.with(this).load(url).into(cover);
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
                    Toast.makeText(MyFanilyActivity.this,"创建家族成功，请等待审核",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyFanilyActivity.this,"您已经提交过申请,请不要重复提交",Toast.LENGTH_SHORT).show();
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