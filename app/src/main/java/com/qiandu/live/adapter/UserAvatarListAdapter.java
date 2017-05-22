package com.qiandu.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiandu.live.R;
import com.qiandu.live.model.SimpleUserInfo;
import com.qiandu.live.utils.OtherUtils;

import java.util.List;


/**
 * @Description: 直播头像列表Adapter
 * @author: Andruby
 * @date: 2016年7月15日
 */
public class UserAvatarListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<SimpleUserInfo> mUserAvatarList;
    Context mContext;
    //主播id
    private String mPusherId;
    //最大容纳量
    private final static int TOP_STORGE_MEMBER = 50;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public UserAvatarListAdapter(Context context, String pusherId, List<SimpleUserInfo> mlist) {
        this.mContext = context;
        this.mPusherId = pusherId;
        this.mUserAvatarList=mlist;
    }

    /**
     * 添加用户信息
     * @param userInfo 用户基本信息
     * @return 存在重复或头像为主播则返回false
     */
    public boolean addItem(SimpleUserInfo userInfo) {

        //去除主播头像
        if(userInfo.user_id.equals(mPusherId))
            return false;

        //去重操作
        for (SimpleUserInfo tcSimpleUserInfo : mUserAvatarList) {
            if(tcSimpleUserInfo.user_id.equals(userInfo.user_id))
                return false;
        }

        //始终显示新加入item为第一位
        mUserAvatarList.add(0, userInfo);
        //超出时删除末尾项
        if(mUserAvatarList.size() > TOP_STORGE_MEMBER)
            mUserAvatarList.remove(TOP_STORGE_MEMBER);
        notifyItemInserted(0);
        return true;
    }

    public void removeItem(String userId) {
        SimpleUserInfo tempUserInfo = null;

        for(SimpleUserInfo userInfo : mUserAvatarList)
            if(userInfo.user_id.equals(userId))
                tempUserInfo = userInfo;


        if(null != tempUserInfo) {
            mUserAvatarList.remove(tempUserInfo);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_user_avatar, parent, false);

        final AvatarViewHolder avatarViewHolder = new AvatarViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return avatarViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        OtherUtils.showPicWithUrl(mContext, ((AvatarViewHolder)holder).ivAvatar,mUserAvatarList.get(position).avatar,
                R.drawable.default_head);

//        Bitmap bitmap = BitmapFactory.decodeFile(mUserAvatarList.get(position).avatar);
//        ((AvatarViewHolder) holder).ivAvatar.setImageBitmap(bitmap);

//        ((AvatarViewHolder) holder).ivAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, "Item:"+position, Toast.LENGTH_SHORT).show();
//                showUserInfoDialog(mContext, mUserAvatarList.get(position));
//            }
//        });

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mUserAvatarList != null? mUserAvatarList.size(): 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    private class AvatarViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;

        public AvatarViewHolder(View itemView) {
            super(itemView);

            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    public void setmUserAvatarList(List<SimpleUserInfo> mUserAvatarList) {
        this.mUserAvatarList = mUserAvatarList;
        notifyDataSetChanged();
    }

    public List<SimpleUserInfo> getmUserAvatarList() {
        return mUserAvatarList;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
