package com.qiandu.live.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.qiandu.live.logic.IMLogin;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.model.GiftWithUerInfo;
import com.qiandu.live.model.LiveUserInfo;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.model.UserInfo;
import com.qiandu.live.model.UserInfoCache;
import com.qiandu.live.presenter.ipresenter.IIMChatPresenter;
import com.qiandu.live.utils.AsimpleCache.ACache;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.ListCacheUtils;
import com.qiandu.live.utils.LogUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Random;

/**
 * Created by zhao on 2017/3/13.
 */

public class IMChatPresenter extends IIMChatPresenter implements TIMMessageListener {


    private final static String TAG = IMChatPresenter.class.getSimpleName();
    private String mRoomId;

    private TIMConversation mGroupConversation;
    private IIMChatView mIMChatView;
    private Gson mGson = new Gson();
    private Context mContext;
    public IMChatPresenter(IIMChatView baseView) {
        super(baseView);
        mIMChatView = baseView;
        mContext = baseView.getContext();
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {
        TIMManager.getInstance().removeMessageListener(this);
        mGroupConversation = null;
    }


    @Override
    public void createGroup() {
        //在特殊情况下未接收到kick out消息下会导致创建群组失败，在登录前做监测
        checkLoginState(new IMLogin.IMLoginListener() {
            @Override
            public void onSuccess() {
                IMLogin.getInstance().removeIMLoginListener();
                //用户登录，创建直播间
                TIMGroupManager.getInstance().createAVChatroomGroup("live", new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int code, String msg) {
                        LogUtil.e(TAG, "create group failed. code: " + code + " errmsg: " + msg);
                    }

                    @Override
                    public void onSuccess(String roomId) {
                        LogUtil.e(TAG, "create group succ, groupId:" + roomId);
                        mRoomId = roomId;
                        mIMChatView.onJoinGroupResult(0, roomId);
                        //得到群组会话
                        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mRoomId);
                        //收取消息的监听
                        TIMManager.getInstance().addMessageListener(IMChatPresenter.this);
                    }
                });
            }

            @Override
            public void onFailure(int code, String msg) {
                IMLogin.getInstance().removeIMLoginListener();
            }
        });
    }

    private void checkLoginState(IMLogin.IMLoginListener loginListener) {

        IMLogin imLogin = IMLogin.getInstance();
        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
            imLogin.setIMLoginListener(loginListener);
            imLogin.checkCacheAndLogin();
        } else {
            //已经处于登录态直接进行回调
            if (null != loginListener)
                loginListener.onSuccess();
        }
    }

    @Override
    public void deleteGroup() {
        sendMessage(Constants.AVIMCMD_LIVE_END,"");
        TIMGroupManager.getInstance().deleteGroup(mRoomId, new TIMCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, String.format("delete group error code = %d,msg = %s", code, msg));
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "delete group success");
                finish();
            }
        });
    }

    @Override
    public void joinGroup(String roomId) {
        mRoomId = roomId;
        TIMGroupManager.getInstance().applyJoinGroup(roomId, "", new TIMCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, String.format("join group error code = %d,msg = %s", code, msg));
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "join group success");
                mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mRoomId);
                TIMManager.getInstance().addMessageListener(IMChatPresenter.this);
                mIMChatView.onJoinGroupResult(0, mRoomId);
                sendMessage(Constants.AVIMCMD_ENTER_LIVE, "");
            }
        });
    }

    @Override
    public void quitGroup(String roomId) {
        sendMessage(Constants.AVIMCMD_EXIT_LIVE, "");
        TIMGroupManager.getInstance().quitGroup(roomId, new TIMCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, String.format("quit group error code = %d,msg = %s", code, msg));
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "quit group success");
                finish();
            }
        });
    }

    @Override
    public void sendTextMsg(final String msg) {
        sendMessage(Constants.AVIMCMD_TEXT_TYPE, msg);
    }

    @Override
    public void sendPraiseMessage() {
        sendMessage(Constants.AVIMCMD_PRAISE, null);
    }

    public void sendPraiseFirstMessage() {
        sendMessage(Constants.AVIMCMD_PRAISE_FIRST, null);
    }

    public void sendMessage(int userAction, String msg) {
        JSONObject json = new JSONObject();
        try {
            json.put("userAction", userAction);
            json.put("userId", ACache.get(mIMChatView.getContext()).getAsString("user_id"));
            json.put("nickName", ACache.get(mIMChatView.getContext()).getAsString("nickname"));
            json.put("headPic", ACache.get(mIMChatView.getContext()).getAsString("head_pic"));
            if (msg != null) {
//                json.put("params", msg);
                json.put("msg", msg);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonMsg = json.toString();
        TIMMessage message = new TIMMessage();
        TIMTextElem textElem = new TIMTextElem();
        textElem.setText(jsonMsg);
        if (message.addElement(textElem) != 0) {
            return;
        }
        sendTIMMessage(message, new TIMValueCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.i(TAG, String.format("send message onError: code = %d,msg = %s", code, msg));
            }

            @Override
            public void onSuccess(Object o) {
                Log.i(TAG, "send message onSuccess: ");
            }
        });
    }

    private void sendTIMMessage(TIMMessage message, TIMValueCallBack callBack) {
        if (mGroupConversation != null) {
            mGroupConversation.sendMessage(message, callBack);
        }
    }

    public void sendGiftMessage(String msg) {
        sendMessage(Constants.AVIMCMD_GIFT, msg);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {

        parserMessage(list);
        return false;
    }

    private void parserMessage(List<TIMMessage> list) {
        Log.i(TAG, "getSender = " + list.get(0).getSender());
        Log.i(TAG, "getMsgId = " + list.get(0).getMsgId());
        Log.i(TAG, "getCustomStr = " + list.get(0).getCustomStr());
//        Log.i(TAG, "getUser = " + list.get(0).getSenderGroupMemberProfile().get);
//        Log.i(TAG, "getMsgId = " + list.get(0).getMsgId());
//        Log.i(TAG, "getCustomStr = " + list.get(0).getCustomStr());
        for (TIMMessage msg : list) {
            TIMElem elem = msg.getElement(0);

//            LogUtil.i(TAG, "TIMMessage="+elem.getType());
//            LogUtil.i(TAG, "TIMMessage="+elem.gett());
            if (elem.getType() == TIMElemType.Text) {
                TIMTextElem text = (TIMTextElem) elem;
//                msg.set
                Log.i(TAG, "onNewMessages: msg = " + text.getText());
                handleCustomTextMsg(text.getText());

//                String result = text.getText().replace("\\", "");
//                Log.i(TAG, "onNewMessages: newMsg = " + result);
//                handleCustomTextMsg(text.getText());

            } else if (elem.getType() == TIMElemType.GroupSystem) {
                TIMGroupSystemElem systemElem = (TIMGroupSystemElem) elem;
                Log.i(TAG, "parserMessage: group msg");
                if (systemElem.getSubtype() == TIMGroupSystemElemType.TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE) {
                    Log.i(TAG, "parserMessage: delete group msg");
                    //观众 退出直播观看
                    mIMChatView.onGroupDeleteResult();
                }
            }
        }
    }

    private void handleCustomTextMsg(String jsonMsg) {

        List<SimpleUserInfo> result = ListCacheUtils.getResult();

//        LogUtil.i(TAG, "username=" + result.get(0).username);

//        Random random = new Random();
//        int i =random.nextInt(result.size());
//        LogUtil.i(TAG, "Random="+i);
////        if(jsonMsg.equals("加入了直播间")){
////            String oo="{\"userAction\":0,\"msg\":\"加入了直播间\",\"headPic\":\""+result.get(i).avatar+"\",\"nickName\":\""+result.get(i).username+"\",\"userId\":\""+result.get(i).user_id+"\"}";
////            jsonMsg = oo;
////        }else if(jsonMsg.equals("离开了直播间")){
////            String oo="{\"userAction\":0,\"msg\":\"加入了直播间\",\"headPic\":\""+result.get(i).avatar+"\",\"nickName\":\""+result.get(i).username+"\",\"userId\":\""+result.get(i).user_id+"\"}";
////            jsonMsg = oo;
////        }else if(jsonMsg.equals("大驾光临")){
////            String oo="{\"userAction\":0,\"msg\":\"加入了直播间\",\"headPic\":\""+result.get(i).avatar+"\",\"nickName\":\""+result.get(i).username+"\",\"userId\":\""+result.get(i).user_id+"\"}";
////            jsonMsg = oo;
////        }

        JSONTokener jsonTokener = new JSONTokener(jsonMsg);
//        jsonTokener.more("/")
//       jsonTokener.replace("amp;","");
//        String gogogo=ii.replace("amp;","");

        Log.d("jsonTokener","jsonTokenerjsonTokener++++++++++++++++++++++++++++++++++++++++++"+jsonTokener);
        try {

            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();

            int userAction = jsonObject.getInt("userAction");
            String nickname = null;
            String userId = null;
            String headPic = null;
            String msg = null;
            if (jsonObject.has("userId")) {
                userId = jsonObject.getString("userId");
            }
            if (jsonObject.has("nickName")) {
                nickname = jsonObject.getString("nickName");
            }
            if (jsonObject.has("headPic")) {
                headPic = jsonObject.getString("headPic");
            }
            if (jsonObject.has("msg")) {
                msg = jsonObject.getString("msg");
                LogUtil.i("GIFTMSG", "msg:" + msg);
            }
            switch (userAction) {
                case Constants.AVIMCMD_TEXT_TYPE:
                    mIMChatView.handleTextMsg(new SimpleUserInfo(userId, nickname, headPic), msg);
                    break;
                case Constants.AVIMCMD_PRAISE_FIRST:
                    mIMChatView.handlePraiseFirstMsg(new SimpleUserInfo(userId, nickname, headPic));
                    break;
                case Constants.AVIMCMD_PRAISE:
                    mIMChatView.handlePraiseMsg(new SimpleUserInfo(userId, nickname, headPic));
                    break;
                case Constants.AVIMCMD_ENTER_LIVE:
                    mIMChatView.handleEnterLiveMsg(new SimpleUserInfo(userId, nickname, headPic));
                    break;
                case Constants.AVIMCMD_EXIT_LIVE:
                    mIMChatView.handleExitLiveMsg(new SimpleUserInfo(userId, nickname, headPic));
                    break;
                case Constants.AVIMCMD_SYSTEM_NOTIFY:
                    break;
                case Constants.AVIMCMD_HOST_LEAVE:
                    Log.i(TAG, "handleCustomTextMsg: AVIMCMD_HOST_LEAVE");
                    break;
//                case Constants.AVIMCMD_HOST_BACK:
//                    Log.i(TAG, "handleCustomTextMsg: AVIMCMD_HOST_BACK");
//                    break;
                case Constants.AVIMCMD_LIVE_END:
                    Log.i(TAG, "handleCustomTextMsg: AVIMCMD_LIVE_END");
                    mIMChatView.handleExitLiveMsg(new SimpleUserInfo(userId, nickname, headPic));
                    break;
                case Constants.AVIMCMD_GIFT:
//                    LogUtil .d("DDDDDDDDDDDD",jsonObject.getString("msg"));
                    String msgResult = jsonObject.getString("msg");
                    LogUtil.i("GIFTMSG", "MSG="+msgResult);

                    String[] sourse = msgResult.split("&");
                    for (int j = 0; j < sourse.length; j++){
                        LogUtil.i("GIFTMSG", "sourse[" + j + "]=" + sourse[j]);
                    }

                    GiftWithUerInfo giftWithUerInfo = new GiftWithUerInfo();
                    GiftInfo giftInfo = new GiftInfo();
                    giftInfo.setId(sourse[0]);
                    giftInfo.setName(sourse[1]);
                    giftInfo.setBigpicUrl(sourse[2]);
                    giftInfo.setGiftCount(Integer.parseInt(sourse[3]));
                    giftInfo.setCost(Integer.parseInt(sourse[4]));
                    giftWithUerInfo.setGiftInfo(giftInfo);
                    LiveUserInfo userInfo = new LiveUserInfo(userId, nickname, headPic);
//                    userInfo.setUserId(userId);;
//                    userInfo.setNickname(nickname);
//                    userInfo.setUserImage(headPic);
                    giftWithUerInfo.setUserInfo(userInfo);

//                    GiftWithUerInfo giftWithUerInfo = mGson.fromJson(jsonObject.getString("msg"), GiftWithUerInfo.class);
                    mIMChatView.handleGift(giftWithUerInfo);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
