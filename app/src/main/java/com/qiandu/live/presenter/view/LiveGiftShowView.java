package com.qiandu.live.presenter.view;

import com.qiandu.live.model.GiftWithUerInfo;

/**
 * @description: 直播礼物显示
 *
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public interface LiveGiftShowView {
    /**
     * 接受获取礼物数据
     */
    void receiveGift(boolean flag, GiftWithUerInfo giftWithUerInfo);
}
