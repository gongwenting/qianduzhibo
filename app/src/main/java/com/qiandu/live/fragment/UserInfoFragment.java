package com.qiandu.live.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiandu.live.LiveApp;
import com.qiandu.live.R;
import com.qiandu.live.activity.BinderJiazuActivity;
import com.qiandu.live.activity.CaifuActivity;
import com.qiandu.live.activity.CoinExchangeActivity;
import com.qiandu.live.activity.DengjiActivity;
import com.qiandu.live.activity.GuanzhuActivity;
import com.qiandu.live.activity.JiazuActivity;
import com.qiandu.live.activity.LianxiActivity;
import com.qiandu.live.activity.LoginActivity;
import com.qiandu.live.activity.MyFanilyActivity;
import com.qiandu.live.activity.ProblemsActivity;
import com.qiandu.live.activity.ShezhiActivity;
import com.qiandu.live.activity.ShouyiActivity;
import com.qiandu.live.activity.UserActivity;
import com.qiandu.live.activity.ZhuboRenzhenActivity;
import com.qiandu.live.base.CustomPopWindow;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.FanilyListRequest;
import com.qiandu.live.http.request.JudgmentRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.JudgmentResp;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.logic.IUserInfoMgrListener;
import com.qiandu.live.logic.UserInfoMgr;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.AsimpleCache.CacheConstants;
import com.qiandu.live.utils.DeviceUtils;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.tencent.rtmp.TXRtmpApi;

/**
 * @description: 用户资料展示页面
 * @author: Andruby
 * @time: 2016/9/3 16:19
 */

/**
 * @description: 用户资料展示页面
 * @author: Andruby
 * @time: 2016/9/3 16:19
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "UserInfoFragment";
    private ImageView mHeadPic;
    private TextView mNickName;
    private TextView mUserId;
    private TextView mQianming;
    private LinearLayout layoutWdcaifu;
    private LinearLayout layoutWdshouyi;
    private LinearLayout layouWddengji;
    private LinearLayout layoutZbrenzhen;
    private LinearLayout layoutShezhi;
    private LinearLayout layoutLxwomen;
    private LinearLayout guanyu;
    private ImageView geren;
    private LinearLayout jiazu;
    private LinearLayout problems;
    private RelativeLayout layout_tuichu;
    private RelativeLayout parent;
    //弹出框
    private CustomPopWindow mCustomPopWindow;
    private int ok;

    public UserInfoFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView(View view) {
        layout_tuichu= (RelativeLayout) view.findViewById(R.id.layout_tuichu);
        mHeadPic = (ImageView) view.findViewById(R.id.iv_ui_head);
        mQianming=obtainView(R.id.tv_ui_user_qianming);
        mNickName = obtainView(R.id.tv_ui_nickname);
        mUserId = obtainView(R.id.tv_ui_user_id);
        geren= (ImageView) view.findViewById(R.id.img_geren);
        layoutWdcaifu= (LinearLayout) view.findViewById(R.id.layout_wdcaifu);
        layoutWdshouyi= (LinearLayout) view.findViewById(R.id.layout_wdshouyi);
        layouWddengji= (LinearLayout) view.findViewById(R.id.layou_wddengji);
        layoutZbrenzhen= (LinearLayout) view.findViewById(R.id.layout_zbrenzhen);
        layoutShezhi= (LinearLayout) view.findViewById(R.id.layout_shezhi);
        layoutLxwomen= (LinearLayout) view.findViewById(R.id.layout_lxwomen);
        jiazu= (LinearLayout) view.findViewById(R.id.layout_wdjiazu);
        guanyu= (LinearLayout) view.findViewById(R.id.guanyu);
        problems = obtainView(R.id.problems);
        parent= (RelativeLayout) view.findViewById(R.id.parent);
    }

    @Override
    protected void initData() {
        //透明状态栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    @Override
    protected void setListener(View view) {
        geren.setOnClickListener(this);
        layoutWdcaifu.setOnClickListener(this);
        layoutWdshouyi.setOnClickListener(this);
        layoutShezhi.setOnClickListener(this);
        layoutLxwomen.setOnClickListener(this);
        layouWddengji.setOnClickListener(this);
        layoutZbrenzhen.setOnClickListener(this);
        layout_tuichu.setOnClickListener(this);
        guanyu.setOnClickListener(this);
        problems.setOnClickListener(this);
        jiazu.setOnClickListener(this);
//        obtainView(R.id.lcv_ui_set).setOnClickListener(this);
//        obtainView(R.id.lcv_ui_logout).setOnClickListener(this);
//        obtainView(R.id.lcv_ui_version).setOnClickListener(this);
//        obtainView(R.id.fanceview).setOnClickListener(this);
//        obtainView(R.id.followView).setOnClickListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        //页面展示之前，更新一下用户信息
        UserInfoMgr.getInstance().queryUserInfo(new IUserInfoMgrListener() {
            @Override
            public void OnQueryUserInfo(int error, String errorMsg) {
//                if (0 == error) {
                if(mNickName==null){
                    mNickName.setText(ACache.get(getContext()).getAsString(CacheConstants.LOGIN_USERNAME));
                }else {
                    mNickName.setText(UserInfoCache.getNickname(LiveApp.getApplication()));
                }
                mUserId.setText(UserInfoCache.getUserId(LiveApp.getApplication()));
                OtherUtils.showPicWithUrl(getActivity(), mHeadPic, UserInfoCache.getHeadPic(LiveApp.getApplication()), R.drawable.default_head);
                mQianming.setText(UserInfoCache.getDesc(LiveApp.getApplication()));
                LogUtil.d("SHUJU++++++++++","mHeadPic"+UserInfoCache.getHeadPic(LiveApp.getApplication()));
//                }
            }

            @Override
            public void OnSetUserInfo(int error, String errorMsg) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void enterEditUserInfo() {
//        EditUseInfoActivity.invoke(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_tuichu:
                showLogout();
                break;
            case R.id.layout_wdcaifu:
                startActivity(new Intent(getActivity(), CaifuActivity.class));
                break;
            case R.id.layout_wdshouyi:
                startActivity(new Intent(getActivity(), CoinExchangeActivity.class));
                break;
            case R.id.layou_wddengji:
                startActivity(new Intent(getActivity(), DengjiActivity.class));
                break;
            case R.id.layout_zbrenzhen:
                ok("gain",ACache.get(getActivity()).getAsString("user_id"));
                break;
            case R.id.layout_shezhi:
                startActivity(new Intent(getActivity(), ShezhiActivity.class));
                break;
            case R.id.layout_lxwomen:
                startActivity(new Intent(getActivity(), LianxiActivity.class));
                break;
            case R.id.guanyu:
                startActivity(new Intent(getActivity(), GuanzhuActivity.class));
                break;
            case  R.id.layout_wdjiazu:

                fanilylist("list",ACache.get(getActivity()).getAsString("user_id"));

                break;
            case R.id.img_geren:
                startActivity(new Intent(getActivity(),UserActivity.class));
                break;
            case R.id.problems:
                startActivity(new Intent(getActivity(),ProblemsActivity.class));
                break;
//        switch (view.getId()) {
//            case R.id.lcv_ui_set: //设置用户信息
//                enterEditUserInfo();
//                break;
//            case R.id.lcv_ui_logout: //注销APP
//                showLogout();
//                break;
//            case R.id.lcv_ui_version: //显示 APP SDK 的版本信息
//                showSDKVersion();
//                break;
//            case R.id.fanceview:
////                FanceActivity.invoke(mContext);
//                break;
//            case R.id.followView:
////                FollowActivity.invoke(mContext);
//                break;
        }
    }

    private void ok(String gain, String userId) {
        JudgmentRequest judg=new JudgmentRequest(gain,userId);
        AsyncHttp.instance().post(judg, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {
                
            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    JudgmentResp jd = (JudgmentResp) response.data;
                    if (jd.getIs_liver()==1){
                        Toast.makeText(getContext(),"您已经是主播无需认证",Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(getActivity(), ZhuboRenzhenActivity.class));
                    }
                }
                
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }

    public void fanilylist(String act,String userid){
        FanilyListRequest fanilyListRequest =new FanilyListRequest(act,userid);
        AsyncHttp.instance().post(fanilyListRequest, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {
            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code== RequestComm.SUCCESS){
                    //跳入到我的家族有家族
                    startActivity(new Intent(getActivity(),MyFanilyActivity.class));
                }else if (response.code== RequestComm.ChangeCount){

                    //102没有家族
                    showPopTopWithDarkBg();
//                }if(response.code== RequestComm.UploadPic){
                }else{
                    //103家族正在创建中
                    showPopTop();
                }


            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });
    }
    private void showPopTop(){
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(R.layout.pop_layout2)
                .create();
        popWindow .showAsDropDown(jiazu,0,  - (jiazu.getHeight() + popWindow.getHeight()));
        //popWindow.showAtLocation(mButton1, Gravity.NO_GRAVITY,0,0);
    }
    /**
     * 显示PopupWindow 同时背景变暗
     */
    private void showPopTopWithDarkBg(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG","onDismiss");
                    }
                })
                .create()
                .showAsDropDown(jiazu, Gravity.RIGHT | Gravity.BOTTOM, 0);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                String showContent = "";
                switch (v.getId()){
                    case R.id.menu1:
                        startActivity(new Intent(getActivity(),JiazuActivity.class));
                        break;
                    case R.id.menu2:
                        startActivity(new Intent(getActivity(), BinderJiazuActivity.class));
                        break;

                }
                Toast.makeText(getActivity(),showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2).setOnClickListener(listener);

    }
    /**
     * 退出登录
     */
    private void showLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle("您确定要退出？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                IMLogin.getInstance().logout();
                LoginActivity.invoke(mContext);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * 显示 APP SDK 的版本信息
     */
    private void showSDKVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        int[] sdkver = TXRtmpApi.getSDKVersion();
        builder.setMessage(getString(R.string.app_name) + DeviceUtils.getAppVersion(mContext) + "\r\n"
        );
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100){
//            String path = data.getStringExtra("url");
//
//            LogUtil.i("userInfoFragment", path);
            OtherUtils.showPicWithUrl(getActivity(), mHeadPic, UserInfoCache.getHeadPic(mContext), R.drawable.default_head);
            mNickName.setText(UserInfoCache.getNickname(mContext));
            mQianming.setText(UserInfoCache.getDesc(mContext));
        }


    }
}