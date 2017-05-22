package com.qiandu.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.model.LeaveMsgInfo;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;

import java.util.List;

/**
 * Created by admin on 2017/5/16.
 */
public class LeaveMessageAdapter extends RecyclerView.Adapter<LeaveMessageAdapter.LeaveMessageHolder> {

    private Context mContext;
    private List<LeaveMsgInfo> mLeaveMsgList;

    public LeaveMessageAdapter(Context mContext, List<LeaveMsgInfo> leaveMsgList) {
        this.mContext = mContext;
        mLeaveMsgList = leaveMsgList;
    }

    @Override
    public LeaveMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.leave_message_item, null);
        LeaveMessageHolder holder = new LeaveMessageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LeaveMessageHolder holder, int position) {

        LogUtil.i("GLM", "mLeaveMsgList:"+mLeaveMsgList.get(position).getCreate_time());

        holder.sendTime.setText(mLeaveMsgList.get(position).getCreate_time());
        //主播头像
        OtherUtils.showPicWithUrl(mContext, holder.userIcon, mLeaveMsgList.get(position).getAvatar(), R.drawable.default_head);
        holder.sendName.setText(mLeaveMsgList.get(position).getSend_id());
        holder.msgContent.setText(mLeaveMsgList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mLeaveMsgList.size();
    }

    public class LeaveMessageHolder extends RecyclerView.ViewHolder{

        public TextView sendTime;
        public ImageView userIcon;
        public TextView sendName;
        public TextView msgContent;

        public LeaveMessageHolder(View itemView) {
            super(itemView);
            sendTime = (TextView) itemView.findViewById(R.id.leave_message_time);
            userIcon = (ImageView) itemView.findViewById(R.id.send_user_icon);
            sendName = (TextView) itemView.findViewById(R.id.leave_message_name);
            msgContent = (TextView) itemView.findViewById(R.id.leave_message_content);
        }
    }
}
