package com.qiandu.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.utils.LogUtil;
import com.qiandu.live.utils.OtherUtils;
import com.qiandu.live.model.LiveInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;



/**
 * @Description: 直播列表的Adapter
 * 列表项布局格式: R.layout.listview_video_item
 * 列表项数据格式: LiveInfo
 * @author: Andruby
 * @date: 2016年7月15日
 */
public class LiveListAdapter extends ArrayAdapter<LiveInfo> {
    private int resourceId;
    private Activity mActivity;

    private class ViewHolder {
        TextView tvHost;
        ImageView frontcover;
        ImageView ivAvatar;
        ImageView ivLogo;
        TextView  qinmidudu;
        TextView  position;
    }

    public LiveListAdapter(Activity activity, ArrayList<LiveInfo> objects) {
        super(activity, R.layout.live_item_view, objects);
        resourceId = R.layout.live_item_view;
        mActivity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);

            holder = new ViewHolder();
            holder.frontcover = (ImageView) convertView.findViewById(R.id.cover);
            holder.tvHost = (TextView) convertView.findViewById(R.id.host_name);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.ivLogo = (ImageView) convertView.findViewById(R.id.live_logo);
            holder.qinmidudu= (TextView) convertView.findViewById(R.id.qinmidu);
            holder.position= (TextView) convertView.findViewById(R.id.host_position);
            convertView.setTag(holder);
        }

        LiveInfo data = getItem(position);

        //直播封面
        String cover = data.userInfo.frontcover;
        if (TextUtils.isEmpty(cover)) {
            holder.frontcover.setImageResource(R.drawable.bg);
        } else {
            RequestManager req = Glide.with(mActivity);
            req.load(cover).placeholder(R.drawable.bg).into(holder.frontcover);
        }

        //主播头像
        OtherUtils.showPicWithUrl(mActivity, holder.ivAvatar, data.userInfo.headpic, R.drawable.default_head);
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
        if(data.position==null){
            holder.position.setText("在地球");
        }else {
            holder.position.setText(data.position);
        }
        if (data.intimacy==0){
            holder.qinmidudu.setText("0");
        }else{
            holder.qinmidudu.setText(data.intimacy+"");
        }
        return convertView;
    }

}
