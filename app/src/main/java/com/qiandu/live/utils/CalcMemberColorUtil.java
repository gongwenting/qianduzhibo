package com.qiandu.live.utils;

import android.text.TextUtils;

import com.qiandu.live.R;

/**
 * @description: 桃心颜色管理
 *
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class CalcMemberColorUtil {
    public static int[] icons = new int[]{R.drawable.chat_heart_1, R.drawable.chat_heart_2, R.drawable
            .chat_heart_3, R.drawable.chat_heart_4, R.drawable.chat_heart_5, R.drawable
            .chat_heart_6, R.drawable.chat_heart_7, R.drawable.chat_heart_8, R.drawable
            .chat_heart_9, R.drawable.chat_heart_10, R.drawable.chat_heart_11, R.drawable
            .chat_heart_12};
    public static int[][] drawableIds = new int[][]{
            {R.mipmap.heart1, R.mipmap.heart2},
            {R.mipmap.heart3, R.mipmap.heart4},
            {R.mipmap.heart5, R.mipmap.heart6},
            {R.mipmap.heart7, R.mipmap.heart8},
            {R.mipmap.heart9, R.mipmap.heart10},
            {R.mipmap.heart11, R.mipmap.heart12},
            {R.mipmap.heart13, R.mipmap.heart14},
            {R.mipmap.heart15, R.mipmap.heart16},
            {R.mipmap.heart17, R.mipmap.heart18},
            {R.mipmap.heart19, R.mipmap.heart20},
            {R.mipmap.heart21, R.mipmap.heart22},
            {R.mipmap.heart23, R.mipmap.heart24}};
    /**
     * 通过名称计算颜色
     */
    public static int calcNameColor(String idx,String curLiveChatRoomId) {
        if(!TextUtils.isEmpty(idx) && !TextUtils.isEmpty(curLiveChatRoomId)){
            idx = idx.trim();
            curLiveChatRoomId = curLiveChatRoomId.trim();
        }else{
            return 0;
        }
        try {
            Integer id = Integer.valueOf(idx) + Integer.valueOf(curLiveChatRoomId);
            switch (id % 12) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return 5;
                case 6:
                    return 6;
                case 7:
                    return 7;
                case 8:
                    return 8;
                case 9:
                    return 9;
                case 10:
                    return 10;
                case 11:
                    return 11;
                case 0:
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
