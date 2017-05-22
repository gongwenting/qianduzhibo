package com.qiandu.live.presenter.ipresenter;

import com.qiandu.live.base.BasePresenter;
import com.qiandu.live.base.BaseView;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.tencent.TIMMessage;


/**
 * @description: IM聊天管理
 * @author: Andruby
 * @time: 2016/12/15 11:54
 */
public abstract class IIMChatPresenter implements BasePresenter {
    protected IIMChatView mIMChatView;

    public IIMChatPresenter(IIMChatView baseView) {
        mIMChatView = baseView;
    }

    /**
     * 创建群
     */
    public abstract void createGroup();

    /**
     * 删除群
     */
    public abstract void deleteGroup();


    /**
     * 加入群
     *
     * @param roomId
     */
    public abstract void joinGroup(final String roomId);

    /**
     * 退出群
     *
     * @param roomId
     */
    public abstract void quitGroup(final String roomId);

    /**
     * 发送文本消息
     */
    public abstract void sendTextMsg(String msg);

    /**
     * 发送点赞消息
     */
    public abstract void sendPraiseMessage();

    /**
     * 第一次点击消息
     */
    public abstract void sendPraiseFirstMessage();

    public interface IIMChatView extends BaseView {
        /**
         * 加入群组回调
         *
         * @param code 错误码，成功时返回0，失败时返回相应错误码
         * @param msg  返回信息，成功时返回群组Id，失败时返回相应错误信息
         */
        void onJoinGroupResult(int code, String msg);

        /**
         * 群组删除回调，在主播群组解散时被调用
         */
        void onGroupDeleteResult();

        void handleTextMsg(SimpleUserInfo userInfo, String text);

        /**
         * 点赞消息处理
         * @param userInfo
         */
        void handlePraiseMsg(SimpleUserInfo userInfo);

        /**
         * 点赞每一次消息
         * @param userInfo
         */
        void handlePraiseFirstMsg(SimpleUserInfo userInfo);

        /**
         * 发送消息结果回调
         *
         * @param code       错误码，成功时返回0，失败时返回相应错误码
         * @param timMessage 发送的TIM消息
         */
        void onSendMsgResult(int code, TIMMessage timMessage);

        /**
         * 观众进入消息处理
         * @param userInfo
         */
        void handleEnterLiveMsg(SimpleUserInfo userInfo);

        /**
         * 观众离开消息处理
         * @param userInfo
         */
        void handleExitLiveMsg(SimpleUserInfo userInfo);
        /**
         * 直播结束
         * @param userInfo
         */
        void handleLiveEnd(SimpleUserInfo userInfo);

        /**
         * 发送礼物
         * @param giftWithUerInfo
         */
        void handleGift(GiftWithUerInfo giftWithUerInfo);
    }
}