package com.qiandu.live.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.qiandu.live.LiveApp;
import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.UpUserInfoReuest;
import com.qiandu.live.http.request.UserInfoRequest;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.presenter.FamilySettingPresenter;
import com.qiandu.live.presenter.ipresenter.IFamilySettingPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.AsimpleCache.CacheConstants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

public class UserActivity extends IMBaseActivity implements View.OnClickListener,
        IFamilySettingPresenter.IFamilySettingView, RadioGroup.OnCheckedChangeListener {
    public static final String TAG=UserActivity.class.getSimpleName();
    private RelativeLayout touxaing;
    private EditText name;
    private Dialog nan;
    private TextView xingbie;
    private EditText gexingqianming;
    private RelativeLayout grend;
    private RelativeLayout houtui;
    private RelativeLayout shezhi;
    private TextView baocun;
    private Uri finalUri, cropUri;
    private boolean mPermission = true;
    private  String filePath;

    private String imageFileName;
    private int srx;
    private String ddddddd;
    private File  storageDir;
    private FamilySettingPresenter familySettingPresenter;
    //图片选择
    private String mCurrentPhotoPath;
    private Dialog mPickDialog;
    private RoundedImageView cover;
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String TV_TITLE_MAX_LEN = "public static final int TV_TITLE_MAX_LEN = 30;";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        dataInfo();
        Data();

    }

    private void detail(String userid,String path) throws FileNotFoundException {
             UserInfoRequest  user=new UserInfoRequest(userid,new File(path));
        AsyncHttp.instance().post(user,null);

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

    private void Data() {
        baocun.setOnClickListener(this);
        touxaing.setOnClickListener(this);
        name.setOnClickListener(this);
        houtui.setOnClickListener(this);
        xingbie.setOnClickListener(this);
        gexingqianming.setOnClickListener(this);
        shezhi.setOnClickListener(this);
    }

    private void dataInfo() {
        cover= (RoundedImageView) findViewById(R.id.imageView_cover);
        baocun= (TextView) findViewById(R.id.textview_baocun);
        shezhi= (RelativeLayout) findViewById(R.id.shezhi);
        houtui= (RelativeLayout) findViewById(R.id.btn_houtui);
        touxaing= (RelativeLayout) findViewById(R.id.relativeLayout_touxiang);
        name= (EditText) findViewById(R.id.editText_name);
        xingbie= (TextView) findViewById(R.id.text_xingbie);
        gexingqianming= (EditText) findViewById(R.id.editText_gerenqianming);
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_pick, null);
        mPickDialog = new AlertDialog.Builder(this).setView(view).create();
        View vi =getLayoutInflater().inflate(R.layout.layout_dialognan_pick, null);
        nan = new AlertDialog.Builder(this).setView(vi).create();
        familySettingPresenter = new FamilySettingPresenter(this);
        OtherUtils.showPicWithUrl(this, cover, UserInfoCache.getHeadPic(LiveApp.getApplication()), R.drawable.default_head);
        if(name==null){
            name.setText(ACache.get(getContext()).getAsString(CacheConstants.LOGIN_USERNAME));
        }else {
            name.setText(UserInfoCache.getNickname(LiveApp.getApplication()));
        }
        gexingqianming.setText(UserInfoCache.getDesc(LiveApp.getApplication()));
        xingbie.setText(UserInfoCache.getGender(LiveApp.getApplication()));
        srx=0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_houtui:
                finish();
                break;
            case R.id.relativeLayout_touxiang:
                mPickDialog.show();
                break;
            case R.id.shezhi:
                nan.show();
                break;
            case R.id.textview_baocun:
                if (srx==1||srx==4){
                    ddddddd="1";

                }else {
                    ddddddd="0";
                }
            upUserInfo(name.getText().toString().trim(),ddddddd,gexingqianming.getText().toString().trim(),ACache.get(UserActivity.this).getAsString("user_id"));

                break;
            
            case R.id.picture_dialog_pick:
                selectImage();
                mPickDialog.dismiss();
                break;
            case R.id.camera_dialog_pick:
                dispatchTakePictureIntent();
                mPickDialog.dismiss();
                break;
            case R.id.text_nan:
                xingbie.setText("男");
                srx=4;
                nan.dismiss();
                break;
            case R.id.text_muman:
                xingbie.setText("女");
                srx=5;
                nan.dismiss();
                break;
        }
    }

    private void upUserInfo(String userid, String nikename, String gender,String desc) {
       UpUserInfoReuest sup=new UpUserInfoReuest(userid,nikename,gender,desc);
        AsyncHttp.instance().post(sup, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code== RequestComm.SUCCESS){
                    Toast.makeText(UserActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                    ACache.get(getApplicationContext()).put("head_pic", filePath);
                    ACache.get(getApplicationContext()).put("nickname", name.getText().toString());
                    ACache.get(getApplicationContext()).put("gender", xingbie.getText().toString());
                    ACache.get(getApplicationContext()).put("desc", gexingqianming.getText().toString());

                }else {
                    Toast.makeText(UserActivity.this,"修改不成功",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });

    }

    /**
     * 从相册中获取
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            showToast("未找到图片查看器");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_IMAGE_GET) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
                LogUtil.d("DDDDDDDDDD",""+filePath);
                try {
                    detail(ACache.get(this).getAsString("user_id"),filePath);//filePath,ACache.get(this).getAsString("user_id")
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                    LogUtil.d("DDDDDDDDDD",""+filePath);
                    try {
                        detail(ACache.get(this).getAsString("user_id"),filePath);//filePath,ACache.get(this).getAsString("user_id")
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                cover.setImageBitmap(bitmap);
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断系统中是否有处理该Intent的Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 创建文件来保存拍的照片
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 异常处理
            }
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            showToast("无法启动相机");
        }
    }
    /**
     * 创建新文件
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 文件名 */
                ".jpg",         /* 后缀 */
                storageDir      /* 路径 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    /**
     * 获取小图片，防止OOM
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public void imageCut(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
    //开启裁剪功能
        intent.putExtra("crop", "true");
    //设定宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
    //设定裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
    //要求返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 100);
    }

    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void doUploadSuceess(String url) {

    }

    @Override
    public void doUploadFailed() {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void dofinily(String act, String userId, String logo, String title, String declaration) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showMsg(int msg) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}

