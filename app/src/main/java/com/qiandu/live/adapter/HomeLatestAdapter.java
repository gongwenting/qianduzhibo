package com.qiandu.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.qiandu.live.R;
import com.qiandu.live.model.LiveInfo;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;

import java.util.List;

/**
 * Created by admin on 2017/5/7.
 */
public class HomeLatestAdapter extends RecyclerView.Adapter<HomeLatestAdapter.HomeLatestHolder> implements View.OnClickListener {
    private int resourceId;
    private Context mContext;
    private List<LiveInfo> mLiveInfoList;

    public HomeLatestAdapter(Context context, int resourceId, List<LiveInfo> liveInfoList) {
        this.mContext = context;
        this.resourceId = resourceId;
        this.mLiveInfoList = liveInfoList;
    }

    @Override
    public HomeLatestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resourceId, null);

        HomeLatestHolder holder = new HomeLatestHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeLatestHolder holder, int position) {
        LiveInfo data = mLiveInfoList.get(position);

        //直播封面
        String cover = data.userInfo.frontcover;
        if (TextUtils.isEmpty(cover)) {
            holder.frontcover.setImageResource(R.drawable.bg);
        } else {
            RequestManager req = Glide.with(mContext);
            req.load(cover).placeholder(R.drawable.bg).into(holder.frontcover);
        }

        //主播头像
        OtherUtils.showPicWithUrl(mContext, holder.ivAvatar, data.userInfo.headpic, R.drawable.default_head);
        //主播昵称
        if (TextUtils.isEmpty(data.userInfo.nickname)) {
            holder.tvHost.setText(OtherUtils.getLimitString(data.userId, 10));
        } else {
            holder.tvHost.setText(OtherUtils.getLimitString(data.userInfo.nickname, 10));
        }
        if (data.type == 0) {
            holder.ivLogo.setImageResource(R.mipmap.wqe2);
        } else {
            holder.ivLogo.setImageResource(R.mipmap.wqe2);
        }
        if (data.position==null){
            holder.position.setText("在地球");
        }else {
            holder.position.setText(data.position);
        }

        LogUtil.i("qinmidu", "qinmidu"+data.intimacy+"");
        //金额
        if (data.intimacy==0){
            holder.qinmidudu.setText("0");
        }else{
            holder.qinmidudu.setText(data.intimacy+"");
        }
//
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mLiveInfoList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public class HomeLatestHolder extends RecyclerView.ViewHolder{

        public TextView tvHost;
        public ImageView frontcover;
        public ImageView ivAvatar;
        public ImageView ivLogo;
        public TextView  qinmidudu;
        public TextView  position;

        public HomeLatestHolder(View itemView) {
            super(itemView);
            tvHost = (TextView) itemView.findViewById(R.id.host_name);
            frontcover = (ImageView) itemView.findViewById(R.id.cover);
            ivAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            ivLogo = (ImageView) itemView.findViewById(R.id.live_logo);
            qinmidudu= (TextView) itemView.findViewById(R.id.qinmidu);
            position= (TextView) itemView.findViewById(R.id.host_position);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
