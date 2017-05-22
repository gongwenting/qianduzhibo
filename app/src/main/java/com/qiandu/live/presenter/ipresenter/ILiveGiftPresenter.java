package com.qiandu.live.presenter.ipresenter;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.model.GiftWithUerInfo;
import com.tencent.imcore.MemberInfo;

import java.util.ArrayList;


/**
 * @description: 礼物逻辑
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class ILiveGiftPresenter implements BasePresenter {
    protected ILiveGiftView mBaseView;

    public ILiveGiftPresenter(ILiveGiftView baseView) {
        mBaseView = baseView;
    }

    /**
     * 礼物列表
     *
     * @param userId
//     * @param liveId
     */
    public abstract void giftList(String userId);
    /**
     * 发送礼物
     *
     * @param sendGiftInfo
     * @param hostId
     * @param liveId
     */
    public abstract void sendGift(GiftInfo sendGiftInfo, String hostId, String liveId);

    /**
     * 获取剩余金币
     *
     * @param userId
     */
    public abstract void coinCount(String userId);

    public interface ILiveGiftView extends BaseView {
        void receiveGift(boolean show, GiftWithUerInfo giftWithUerInfo);
        void sendGiftFailed();
        void gotoPay();

        void showSenderInfoCard(MemberInfo currentMember);

        void onCoinCount(int coinCount);

        void onGiftList(ArrayList<GiftInfo> giftList);

        void onGiftListFailed();






    }
}