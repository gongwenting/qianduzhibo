package com.qiandu.live.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.model.ChatEntity;
import com.qiandu.live.model.CurrentLiveInfo;
import com.qiandu.live.utils.CalcMemberColorUtil;
import com.qiandu.live.utils.Constants;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.UIUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @Description: 消息列表的Adapter
 * @author: Andruby
 * @date: 2016年7月9日
 */
public class ChatMsgListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private static String TAG = ChatMsgListAdapter.class.getSimpleName();
    private static final int ITEMCOUNT = 7;
    private List<ChatEntity> listMessage = null;
    private LayoutInflater inflater;
    private LinearLayout layout;
    public static final int TYPE_TEXT_SEND = 0;
    public static final int TYPE_TEXT_RECV = 1;
    private Context context;
    private ListView mListView;
    private ArrayList<ChatEntity> myArray = new ArrayList<ChatEntity>();
    private boolean isbLiveAnimator;

    class AnimatorInfo {
        long createTime;

        public AnimatorInfo(long uTime) {
            createTime = uTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }

    private static final int MAXANIMATORCOUNT = 8;
    private static final int ANIMATORDURING = 8000;
    private static final int MAXITEMCOUNT = 50;
    private LinkedList<AnimatorSet> mAnimatorSetList;
    private LinkedList<AnimatorInfo> mAnimatorInfoList;
    private boolean mScrolling = false;
    private boolean mCreateAnimator = false;

    private int colorType;

    public ChatMsgListAdapter(Context context, ListView listview, List<ChatEntity> objects) {
        this.context = context;
        mListView = listview;
        inflater = LayoutInflater.from(context);
        this.listMessage = objects;

        mAnimatorSetList = new LinkedList<AnimatorSet>();
        mAnimatorInfoList = new LinkedList<AnimatorInfo>();

        mListView.setOnScrollListener(this);
    }


    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        SpannableString spanString;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_chatmsg, null);
            holder.textItem = (LinearLayout) convertView.findViewById(R.id.text_item);
            holder.sendContext = (TextView) convertView.findViewById(R.id.sendcontext);
            convertView.setTag(R.id.tag_first, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.tag_first);
        }

        ChatEntity item = listMessage.get(position);

        if (mCreateAnimator && isbLiveAnimator) {
            playViewAnimator(convertView, position, item);
        }

       switch (item.getType()){
           case Constants.AVIMCMD_ENTER_LIVE:
               spanString = addSystemSpan(holder, item);
               holder.sendContext.setText(spanString);
               break;
           case Constants.AVIMCMD_EXIT_LIVE:
               spanString = addSystemSpan(holder, item);
               holder.sendContext.setText(spanString);
               break;
           case Constants.AVIMCMD_TEXT_TYPE:
               spanString = addNormalSpan(holder, item);
               holder.sendContext.setText(spanString);
               break;
       }



        return convertView;
    }

    /**
     * 赠送礼物消息
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addGiftSpan(ViewHolder holder, ChatEntity item) {
        //获取内容
        SpannableString spanString = new SpannableString(item.getSenderName() + item
                .getContext());
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_gift_back));
        return spanString;
    }

    /**
     * 关注
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addFollowSpan(ViewHolder holder, ChatEntity item) {
        //获取内容
        SpannableString spanString = new SpannableString(item.getSenderName() + item
                .getContext());
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_content_follow));
        return spanString;
    }

    /**
     * 指定颜色的消息
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addColorSpan(ViewHolder holder, ChatEntity item) {
        //获取内容
        String context = item.getContext();
        int i = context.lastIndexOf(",");
        String msg = context.substring(0, i);
        String color = context.substring(i + 1, context.length());
        SpannableString spanString = new SpannableString(msg);
        try {
            holder.sendContext.setTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                    .live_im_content_follow));
        }
        return spanString;
    }

    /**
     * 禁言
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addSilenceSpan(ViewHolder holder, ChatEntity item) {
        //获取内容
        String body = item.getContext();
        //禁言
       /*int silenceStringlength = (context.getString(R.string
                .live_member_silence_im)).length();*/
        /*spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color
                        .live_im_name)),
                0, body.length() - silenceStringlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        SpannableString spanString = new SpannableString(body);
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_gag_leave));
        return spanString;
    }

    /**
     * 被设为场控和取消场控
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addSetAdmin(ViewHolder holder, ChatEntity item) {
        //获取内容
        String body = item.getContext();
        SpannableString spanString = new SpannableString(body);
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_system));
        return spanString;
    }

    /**
     * 主播离开回来
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addHostLeaveBackSpan(ViewHolder holder, ChatEntity item) {
        String body = item.getContext();
        //暂离和回来不显示发送者名称
        SpannableString spanString = new SpannableString(body);
        if (TextUtils.equals(body, context.getString(R.string.live_host_leave))) {
            holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                    .live_im_gag_leave));
        } else {
            holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                    .live_im_gift_back));
        }
        return spanString;
    }

    /**
     * 观众进来
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addMemberEnterSpan(ViewHolder holder, ChatEntity item) {
        SpannableString spanString = new SpannableString(item.getSenderName() + context
                .getString(R.string
                        .live_join_live));
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_join));
        return spanString;
    }

    private SpannableString addMemberESpan(ViewHolder holder, ChatEntity item) {
        SpannableString spanString = new SpannableString(item.getSenderName() + context
                .getString(R.string
                        .live_quite_live));
        holder.sendContext.setTextColor(this.context.getResources().getColor(R.color
                .live_im_join));
        return spanString;
    }

    /**
     * 一般消息
     *
     * @param holder
     * @param item
     * @return
     */
    private SpannableString addNormalSpan(ViewHolder holder, ChatEntity item) {
        SpannableString spanString = new SpannableString(item.getSenderName() + " " + item
                .getContext());
       /* // 设置名称为粗体
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        //加粗用户名
        spanString.setSpan(boldStyle, 0, item.getSenderName().length(), Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);*/
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color
                        .msg_nickname)),
                0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        LogUtil.i("CMLA", item.getContext());

        if (item.getContext().equals("加入了直播间") ||
                item.getContext().equals("大驾光临") ||
                item.getContext().equals("离开了直播间")){
            holder.sendContext.setTextColor(context.getResources().getColor(R.color
                    .msg_text));
        }else{
            holder.sendContext.setTextColor(context.getResources().getColor(R.color
                    .white));
        }


        return spanString;
    }

    /**
     * 系统消息
     *
     * @param holder
     * @param item
     */
    private SpannableString addSystemSpan(ViewHolder holder, ChatEntity item) {
//        SpannableString spanString = new SpannableString(item.getSenderName() + item
//                .getContext());
        // 设置名称为粗体
        SpannableString spanString = new SpannableString(item.getSenderName() + " " + item
                .getContext());
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color
                        .msg_nickname)),
                0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.sendContext.setTextColor(context.getResources().getColor(R.color
                .msg_text));

//        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
//        spanString.setSpan(boldStyle, 0, item.getSenderName().length(), Spannable
//                .SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color
//                        .live_im_name)),
//                0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.sendContext.setTextColor(context.getResources().getColor(R.color
//                .live_im_system));
        return spanString;
    }

    /**
     * 观众点心
     *
     * @param holder
     * @param item
     */
    private SpannableString addHeartSpan(ViewHolder holder, ChatEntity item) {
        SpannableString spanString = new SpannableString(item.getSenderName() + " " + item
                .getContext() + "  ");
        int i = CalcMemberColorUtil.calcNameColor(item.getId(), CurrentLiveInfo.getRoomId());
        Drawable drawable = context.getResources().getDrawable(CalcMemberColorUtil.icons[i]);
        drawable.setBounds(0, 0, UIUtils.formatDipToPx(context, 13), UIUtils.formatDipToPx
                (context, 13));
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spanString.setSpan(span, item.getSenderName().length() + item.getContext().length() + 1 -
                        "[heart]".length(),
                item.getSenderName().length() + item.getContext().length() + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.sendContext.setTextColor(context.getResources().getColor(R.color
                .live_im_heart));
        return spanString;
    }


    static class ViewHolder {
        public LinearLayout textItem;
        public TextView sendContext;

    }

    /**
     * 停止View属性动画
     *
     * @param itemView
     */
    private void stopViewAnimator(View itemView) {
        AnimatorSet aniSet = (AnimatorSet) itemView.getTag(R.id.tag_second);
        if (null != aniSet) {
            aniSet.cancel();
            mAnimatorSetList.remove(aniSet);
        }
    }

    /**
     * 播放View属性动画
     *
     * @param itemView   动画对应View
     * @param startAlpha 初始透明度
     * @param duringTime 动画时长
     */
    private void playViewAnimator(View itemView, float startAlpha, long duringTime) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", startAlpha, 0f);
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(duringTime);
        aniSet.play(animator);
        aniSet.start();
        mAnimatorSetList.add(aniSet);
        itemView.setTag(R.id.tag_second, aniSet);
    }

    /**
     * 播放渐消动画
     *
     * @param pos
     * @param view
     */
    public void playDisappearAnimator(int pos, View view) {
        int firstVisable = mListView.getFirstVisiblePosition();
        if (firstVisable <= pos) {
            playViewAnimator(view, 1f, ANIMATORDURING);
        } else {
            LogUtil.e(TAG, "playDisappearAnimator->unexpect pos: " + pos + "/" + firstVisable);
        }
    }

    /**
     * 继续播放渐消动画
     *
     * @param itemView
     * @param position
     * @param item
     */
    private void continueAnimator(View itemView, int position, final ChatEntity item) {
        int animatorIdx = listMessage.size() - 1 - position;

        if (animatorIdx < MAXANIMATORCOUNT) {
            float startAlpha = 1f;
            long during = ANIMATORDURING;

            stopViewAnimator(itemView);

            // 播放动画
            if (position < mAnimatorInfoList.size()) {
                AnimatorInfo info = mAnimatorInfoList.get(position);
                long time = info.getCreateTime();  //  获取列表项加载的初始时间
                during = during - (System.currentTimeMillis() - time);     // 计算动画剩余时长
                startAlpha = 1f * during / ANIMATORDURING;                    // 计算动画初始透明度
                if (during < 0) {   // 剩余时长小于0直接设置透明度为0并返回
                    itemView.setAlpha(0f);
                    LogUtil.e(TAG, "continueAnimator->already end animator:" + position + "/" +
                            item.getContext() + "-" + during);
                    return;
                }
            }

            // 创建属性动画并播放
            LogUtil.e(TAG, "continueAnimator->pos: " + position + "/" + listMessage.size() + ", " +
                    "alpha:" + startAlpha + ", dur:" + during);
            playViewAnimator(itemView, startAlpha, during);
        } else {
            LogUtil.e(TAG, "continueAnimator->ignore pos: " + position + "/" + listMessage.size());
        }
    }

    /**
     * 播放消失动画
     */
    private void playDisappearAnimator() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View itemView = mListView.getChildAt(i);
            if (null == itemView) {
                LogUtil.e(TAG, "playDisappearAnimator->view not found: " + i + "/" + mListView
                        .getCount());
                break;
            }

            // 更新动画创建时间
            int position = mListView.getFirstVisiblePosition() + i;
            if (position < mAnimatorInfoList.size()) {
                mAnimatorInfoList.get(position).setCreateTime(System.currentTimeMillis());
            } else {
                LogUtil.e(TAG, "playDisappearAnimator->error: " + position + "/" +
                        mAnimatorInfoList.size());
            }

            playViewAnimator(itemView, 1f, ANIMATORDURING);
        }
    }

    /**
     * 播放列表项动画
     *
     * @param itemView 要播放动画的列表项
     * @param position 列表项的位置
     * @param item     列表数据
     */
    private void playViewAnimator(View itemView, int position, final ChatEntity item) {
        if (!myArray.contains(item)) {  // 首次加载的列表项动画
            myArray.add(item);
            mAnimatorInfoList.add(new AnimatorInfo(System.currentTimeMillis()));
        }

        if (mScrolling) {  // 滚动时不播放动画，设置透明度为1
            itemView.setAlpha(1f);
        } else {
            continueAnimator(itemView, position, item);
        }
    }

    /**
     * 删除超过上限(MAXITEMCOUNT)的列表项
     */
    private void clearFinishItem() {
        // 删除超过的列表项
        while (listMessage.size() > MAXITEMCOUNT) {
            listMessage.remove(0);
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            }
        }

        // 缓存列表延迟删除
        while (myArray.size() > (MAXITEMCOUNT << 1)) {
            myArray.remove(0);
        }

        while (mAnimatorInfoList.size() >= listMessage.size()) {
            LogUtil.e(TAG, "clearFinishItem->error size: " + mAnimatorInfoList.size() + "/" +
                    listMessage.size());
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            } else {
                break;
            }
        }
    }

    /**
     * 重新计算ITEMCOUNT条记录的高度，并动态调整ListView的高度
     */
    private void redrawListViewHeight() {
        int totalHeight = 0;
        int start = 0, lineCount = 0;

        if (listMessage.size() <= 0) {
            return;
        }

        // 计算底部ITEMCOUNT条记录的高度
        mCreateAnimator = false;    // 计算高度时不播放属性动画
        for (int i = listMessage.size() - 1; i >= start && lineCount < ITEMCOUNT; i--,
                lineCount++) {
            View listItem = getView(i, null, mListView);
            listItem.measure(0, 0);
            // add item height
            totalHeight = totalHeight + listItem.getMeasuredHeight();
        }
        mCreateAnimator = true;

        // 调整ListView高度
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (lineCount - 1));
        mListView.setLayoutParams(params);
    }

    /**
     * 停止当前所有属性动画并重置透明度
     */
    private void stopAnimator() {
        // 停止动画
        for (AnimatorSet anSet : mAnimatorSetList) {
            anSet.cancel();
        }
        mAnimatorSetList.clear();
    }

    /**
     * 重置透明度
     */
    private void resetAlpha() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            view.setAlpha(1f);
        }
    }

    /**
     * 继续可视范围内所有动画
     */
    private void continueAllAnimator() {
        int startPos = mListView.getFirstVisiblePosition();

        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            if (null != view && startPos + i < listMessage.size()) {
                continueAnimator(view, startPos + i, listMessage.get(startPos + i));
            }
        }
    }

    /**
     * 重载notifyDataSetChanged方法实现渐消动画并动态调整ListView高度
     */
    @Override
    public void notifyDataSetChanged() {
        LogUtil.e(TAG, "notifyDataSetChanged->scroll: " + mScrolling);
        if (mScrolling) {
            // 滑动过程中不刷新
            super.notifyDataSetChanged();
            return;
        }

        // 删除多余项
        clearFinishItem();

        if (isbLiveAnimator) {
            // 停止之前动画
            stopAnimator();
            // 清除动画
            mAnimatorSetList.clear();
        }

        super.notifyDataSetChanged();

        // 重置ListView高度
        //redrawListViewHeight();

        if (isbLiveAnimator && listMessage.size() >= MAXITEMCOUNT) {
            continueAllAnimator();
        }

        // 自动滚动到底部
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getCount() - 1);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                if (isbLiveAnimator) {
                    // 开始滚动时停止所有属性动画
                    stopAnimator();
                    resetAlpha();
                }
                mScrolling = true;
                break;
            case SCROLL_STATE_IDLE:
                mScrolling = false;
                if (isbLiveAnimator) {
                    // 停止滚动时播放渐消动画
                    playDisappearAnimator();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {

    }
}
