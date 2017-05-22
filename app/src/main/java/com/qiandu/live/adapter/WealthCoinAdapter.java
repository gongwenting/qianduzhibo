package com.qiandu.live.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.http.response.WealthCoinInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/18.
 */

public class WealthCoinAdapter extends RecyclerView.Adapter<WealthCoinAdapter.WealthCoinHolder>{

    private Context mContext;
    private List<WealthCoinInfo> mWealthCoinInfoList;
    private List<Boolean> mIsClickList;

    public WealthCoinAdapter(Context context, List<WealthCoinInfo> wealthCoinInfoList) {
        this.mContext = context;
        this.mWealthCoinInfoList = wealthCoinInfoList;
        this.mIsClickList = new ArrayList<>();
        for(int i = 0;i<mWealthCoinInfoList.size();i++){
            mIsClickList.add(false);
        }
    }

    @Override
    public WealthCoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wealth_coin_list_item, null);

        WealthCoinHolder holder = new WealthCoinHolder(view);
//        //将创建的View注册点击事件
//        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final WealthCoinHolder holder, final int position) {
        holder.coin.setText(mWealthCoinInfoList.get(position).getCoin());
        holder.money.setText(mWealthCoinInfoList.get(position).getMoney());

        if (mIsClickList.get(position)){
            holder.coin.setTextColor(Color.parseColor("#D91C60"));
            holder.money.setTextColor(Color.parseColor("#D91C60"));
        }else{
            holder.coin.setTextColor(Color.BLACK);
            holder.money.setTextColor(Color.parseColor("#5F6164"));
        }

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i <mIsClickList.size();i++){
                        mIsClickList.set(i,false);
                    }
                    mIsClickList.set(position,true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mWealthCoinInfoList.size();
    }

//    @Override
//    public void onClick(View view) {
//        if (mOnItemClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemClickListener.onItemClick(view, (int)view.getTag());
//        }
//    }

    public class WealthCoinHolder extends RecyclerView.ViewHolder{

        public TextView coin;
        public TextView money;

        public WealthCoinHolder(View itemView) {
            super(itemView);
            coin = (TextView) itemView.findViewById(R.id.coin);
            money = (TextView) itemView.findViewById(R.id.money);
        }
    }

    private HomeHotAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(HomeHotAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
