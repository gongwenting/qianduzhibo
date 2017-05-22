package com.qiandu.live.presenter;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.CoinCountReuqest;
import com.qiandu.live.http.request.GiftListReuqest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.request.SendGiftReuqest;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.CoinCount;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.LiveUserInfo;
import com.qiandu.live.presenter.ipresenter.ILiveGiftPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.CustomDialog;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.ToastUtils;

import java.util.ArrayList;

/**
 * @description: 礼物逻辑
 * @author: Andruby
 * @time: 2016/12/18 14:04
 */
public class LiveGiftPresenter extends ILiveGiftPresenter {
    private static final String TAG = "LiveGiftPresenter";
    public static final int GO_TO_PAY = 1;
    private CustomDialog mGotoPayDialog;
    private Handler mHandler = new Handler();
    private ILiveGiftView mLiveGiftView;

    public LiveGiftPresenter(ILiveGiftView baseView) {
        super(baseView);
        mLiveGiftView = baseView;
    }

    @Override
    public void sendGift(final GiftInfo sendGiftInfo, String hostId, String liveId) {
        SendGiftReuqest req = new SendGiftReuqest(hostId, liveId, sendGiftInfo.getId(), sendGiftInfo.getGiftCount());

        AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    GiftWithUerInfo giftWithUerInfo = new GiftWithUerInfo();
                    giftWithUerInfo.setGiftInfo(sendGiftInfo);
                    giftWithUerInfo.setUserInfo(new LiveUserInfo(ACache.get(mBaseView.getContext()).getAsString("user_id"),
                            ACache.get(mBaseView.getContext()).getAsString("nickname"),
                            ACache.get(mBaseView.getContext()).getAsString("head_pic")));

                    LogUtil.d("FFFFFFFFFFFFFFFFFFFFFFF", giftWithUerInfo.getUserInfo().getNickname());

                    mLiveGiftView.receiveGift(true, giftWithUerInfo);
                }
            }
            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {
                mLiveGiftView.sendGiftFailed();
            }
        });
    }

    @Override
    public void coinCount(String userId) {
        CoinCountReuqest req = new CoinCountReuqest(userId);
        AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    CoinCount coinCount = (CoinCount) response.data;
                    if (coinCount != null) {
                        mBaseView.onCoinCount(coinCount.getScore());

                        LogUtil.d("金额","coinCount.getScore()"+coinCount.getScore());

                    }
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {
                mBaseView.onCoinCount(-1);

            }
        });
    }

    @Override
    public void giftList(String userId) {
        GiftListReuqest req = new GiftListReuqest(userId);
        AsyncHttp.instance().post(req, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {
                if (response.code == RequestComm.SUCCESS) {
                    ResList<GiftInfo> resList = (ResList<GiftInfo>) response.data;
                    if (resList != null) {
                        ArrayList<GiftInfo> result = (ArrayList<GiftInfo>) resList.datas;
                        LogUtil.i("IMGING", result.size()+"");

                        mBaseView.onGiftList(result);
                    }


                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {
//                mBaseView.onGiftListFailed();
            }
        });
    }




    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }
    private void showToast(final int stringId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(mLiveGiftView.getContext(), mLiveGiftView.getContext().getString(stringId));
            }
        });
    }
    /**
     * 去充值的dialog
     */
    public void showPayDialog() {
        if (mGotoPayDialog == null) {
            mGotoPayDialog = new CustomDialog(mLiveGiftView.getContext(), R.layout.live_dialog);
            Button btnCancel = (Button) mGotoPayDialog.findViewById(R.id.dialog_cancel);
            Button btnConfirm = (Button) mGotoPayDialog.findViewById(R.id.dialog_confirm);
            TextView title = (TextView) mGotoPayDialog.findViewById(R.id.dialog_message_info);
            title.setText(mLiveGiftView.getContext().getResources().getString(R.string.live_syb_gottopay_dialog));
            mGotoPayDialog.setCanceledOnTouchOutside(false);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mGotoPayDialog.dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mGotoPayDialog.dismiss();
                    mLiveGiftView.gotoPay();
                }
            });
        }
        mGotoPayDialog.show();
    }
}

