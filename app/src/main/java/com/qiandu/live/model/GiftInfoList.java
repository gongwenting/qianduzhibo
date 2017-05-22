package com.qiandu.live.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * @description: 礼物列表
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class GiftInfoList {

    private ArrayList<GiftInfo> list;

    public GiftInfoList(JsonObject json) {
    }

    public ArrayList<GiftInfo> getList() {
        return list;
    }

    public void setList(ArrayList<GiftInfo> list) {
        this.list = list;
    }
}
